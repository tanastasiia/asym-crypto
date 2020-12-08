package ua.kpi.lab3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ua.kpi.util.SignedMsg;
import ua.kpi.util.Util;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class RabinTest {

    Rabin server = Mockito.mock(Rabin.class);
    ObjectMapper mapper = new ObjectMapper();
    Random random = new Random();

    String serverUrl = "http://asymcryptwebservice.appspot.com/rabin/";
    String serverKeyUrl = serverUrl + "serverKey";
    String encryptUrl = serverUrl + "encrypt";
    String decryptUrl = serverUrl + "decrypt";
    String signUrl = serverUrl + "sign";
    String verifyUrl = serverUrl + "verify";

    BasicHttpContext httpContext = new BasicHttpContext();
    CloseableHttpClient httpClient = HttpClients.createDefault();

    int primeSizeBytes = 32;

    Rabin alice = new Rabin(primeSizeBytes);

    BigInteger m;

    public String formParamString(Map<String, String> params) {
        StringBuffer sb = new StringBuffer("?");
        params.forEach((key, value) -> sb.append(key).append("=").append(value).append("&"));
        return sb.substring(0, sb.length() - 1);
    }

    public JsonNode getRequestJson(String url) throws IOException {
        System.out.println("\nGET " + url);
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url), httpContext)) {
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("RESPONSE: " + json + "\n");
            return mapper.readValue(json, JsonNode.class);
        }
    }

    @BeforeEach
    public void init() throws IOException {

        alice.generateKeyPair();

        int keySize = alice.getPublicKeyN().bitLength();
        m = new BigInteger((Util.byteLength(alice.getPublicKeyN())-11) * 8, random);

        System.out.println("alice N = " + alice.getPublicKeyN().toString(16).toUpperCase() + " (" + alice.getPublicKeyN().bitLength() + " bits)");
        System.out.println("alice B = " + alice.getPublicKeyB().toString(16).toUpperCase() + " (" + alice.getPublicKeyB().bitLength() + " bits)");
        System.out.println("alice M = " + m.toString(16).toUpperCase() + " (" + m.bitLength() + " bits)");

        JsonNode json = getRequestJson(serverKeyUrl + "?keySize=" + keySize);

        BigInteger serverN = new BigInteger(json.get("modulus").asText(), 16);
        BigInteger serverB = new BigInteger(json.get("b").asText(), 16);

        when(server.getPublicKeyN()).thenReturn(serverN);
        when(server.getPublicKeyB()).thenReturn(serverB);

        System.out.println("server N = " + server.getPublicKeyN().toString(16).toUpperCase() + " (" + server.getPublicKeyN().bitLength() + " bits)");
        System.out.println("server B = " + server.getPublicKeyB().toString(16).toUpperCase() + " (" + server.getPublicKeyB().bitLength() + " bits)");
    }

    @Test
    public void encryptionTest() throws IOException {

        JsonNode json = getRequestJson(encryptUrl + formParamString(new HashMap<String, String>() {{
            put("modulus", alice.getPublicKeyN().toString(16));
            put("b", alice.getPublicKeyB().toString(16));
            put("message", m.toString(16));
        }}));

        Rabin.CypherText serverEncryptedForAlice =  new Rabin.CypherText(new BigInteger(json.get("cipherText").asText(), 16),
                        json.get("parity").asInt(), json.get("jacobiSymbol").asInt());

        assertEquals(m, alice.decrypt(serverEncryptedForAlice));
    }

    @Test
    public void decryptionTest() throws IOException {
        Rabin.CypherText encryptedForServer = alice.encrypt(m, server);

        JsonNode json = getRequestJson(decryptUrl + formParamString(new HashMap<String, String>() {{
            put("cipherText", encryptedForServer.getY().toString(16));
            put("parity", Integer.toString(encryptedForServer.getC1()));
            put("jacobiSymbol", Integer.toString(encryptedForServer.getC2()));
        }}));
        BigInteger serverDecrypted = new BigInteger(json.get("message").asText(), 16);

        assertEquals(m, serverDecrypted);
    }

    @Test
    public void signatureTest() throws IOException {
        JsonNode json = getRequestJson(signUrl + formParamString(new HashMap<String, String>() {{
            put("message", m.toString(16));
        }}));

        String serverSignature = json.get("signature").asText();

        assertTrue(alice.verify(new SignedMsg(m, new BigInteger(serverSignature, 16)), server));
    }

    @Test
    public void verificationTest() throws IOException {

        BigInteger signature = alice.sign(m);

        JsonNode json = getRequestJson(verifyUrl + formParamString(new HashMap<String, String>() {{
            put("modulus", alice.getPublicKeyN().toString(16));
            put("message", m.toString(16));
            put("signature", signature.toString(16));
        }}));

        assertTrue(json.get("verified").asBoolean());
    }
}
