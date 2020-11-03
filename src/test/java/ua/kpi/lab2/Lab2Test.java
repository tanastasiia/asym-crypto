package ua.kpi.lab2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import ua.kpi.lab1.generators.util.Util;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Lab2Test {

    RSA server = Mockito.mock(RSA.class);
    ObjectMapper mapper = new ObjectMapper();

    String serverUrl = "http://asymcryptwebservice.appspot.com/rsa/";
    String serverKeyUrl = serverUrl + "serverKey";
    String encryptUrl = serverUrl + "encrypt";
    String decryptUrl = serverUrl + "decrypt";
    String signUrl = serverUrl + "sign";
    String verifyUrl = serverUrl + "verify";
    String sendKeyUrl = serverUrl + "sendKey";
    String receiveKeyUrl = serverUrl + "receiveKey";

    BasicHttpContext httpContext = new BasicHttpContext();
    CloseableHttpClient httpClient = HttpClients.createDefault();

    int primeSizeBytes = 32;

    RSA alice = new RSA(primeSizeBytes);
    RSA bob = new RSA(primeSizeBytes);

    BigInteger m;

    public void generateMessage() {
        int keySize = alice.getPublicKeyN().bitLength();
        m = Util.randomBigInteger(BigInteger.ONE, BigInteger.ONE.shiftLeft(keySize - 1));
    }

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
        m = Util.randomBigInteger(BigInteger.ONE, BigInteger.ONE.shiftLeft(keySize - 1));

        System.out.println("alice N = " + alice.getPublicKeyN().toString(16).toUpperCase() + " (" + alice.getPublicKeyN().bitLength() + " bits)");
        System.out.println("alice E = " + alice.getPublicKeyE().toString(16).toUpperCase());
        System.out.println("alice M = " + m.toString(16).toUpperCase() + " (" + m.bitLength() + " bits)");

        JsonNode json = getRequestJson(serverKeyUrl + "?keySize=" + keySize);

        BigInteger serverN = new BigInteger(json.get("modulus").asText(), 16);
        BigInteger serverE = new BigInteger(json.get("publicExponent").asText(), 16);

        when(server.getPublicKeyN()).thenReturn(serverN);
        when(server.getPublicKeyE()).thenReturn(serverE);

        System.out.println("server N = " + server.getPublicKeyN().toString(16).toUpperCase() + " (" + server.getPublicKeyN().bitLength() + " bits)");
        System.out.println("server E = " + server.getPublicKeyE().toString(16).toUpperCase());
    }

    @Test
    public void encryptionTest() throws IOException {

        bob.generateKeyPair();

        JsonNode json = getRequestJson(encryptUrl + formParamString(new HashMap<String, String>() {{
            put("modulus", alice.getPublicKeyN().toString(16));
            put("publicExponent", alice.getPublicKeyE().toString(16));
            put("message", m.toString(16));
        }}));

        BigInteger serverEncryptedForAlice = new BigInteger(json.get("cipherText").asText(), 16);
        BigInteger bobEncryptedForAlice = bob.encrypt(m, alice);

        System.out.println("bobEncryptedForAlice: " + bobEncryptedForAlice.toString(16).toUpperCase());

        assertEquals(serverEncryptedForAlice, bobEncryptedForAlice);
    }

    @Test
    public void decryptionTest() throws IOException {
        String encryptedForServer = alice.encrypt(m, server).toString(16);

        JsonNode json = getRequestJson(decryptUrl + formParamString(new HashMap<String, String>() {{
            put("cipherText", encryptedForServer);
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

        assertTrue(alice.verify(new RSA.SignedMsg(m, new BigInteger(serverSignature, 16)), server));
    }

    @Test
    public void verificationTest() throws IOException {

        BigInteger signature = alice.sign(m);

        JsonNode json = getRequestJson(verifyUrl + formParamString(new HashMap<String, String>() {{
            put("modulus", alice.getPublicKeyN().toString(16));
            put("publicExponent", alice.getPublicKeyE().toString(16));
            put("message", m.toString(16));
            put("signature", signature.toString(16));
        }}));

        assertTrue(json.get("verified").asBoolean());
    }

    @Test
    public void sendKeyTest() throws IOException, RSA.VerificationException {
        JsonNode json = getRequestJson(sendKeyUrl + formParamString(new HashMap<String, String>() {{
            put("modulus", alice.getPublicKeyN().toString(16));
            put("publicExponent", alice.getPublicKeyE().toString(16));
        }}));

        BigInteger key = new BigInteger(json.get("key").asText(), 16);
        BigInteger signature = new BigInteger(json.get("signature").asText(), 16);

        BigInteger aliceReceivedKey = alice.receiveKey(new RSA.SignedMsg(key, signature), server);
        System.out.println("aliceReceivedKey: " + aliceReceivedKey.toString(16).toUpperCase());
    }

    @Test
    public void receiveKeyTest() throws IOException {

        while (alice.getPublicKeyN().compareTo(server.getPublicKeyN()) > 0) {
            alice.generateKeyPair();
            generateMessage();
        }

        RSA.SignedMsg sm = alice.sendKey(m, server);
        System.out.println("Encrypted key:" + sm.getM().toString(16).toUpperCase() + " (" + sm.getM().bitLength() + " bits)");
        System.out.println("Signature:" + sm.getS().toString(16).toUpperCase() + " (" + sm.getS().bitLength() + " bits)");

        JsonNode json = getRequestJson(receiveKeyUrl + formParamString(new HashMap<String, String>() {{
            put("key", sm.getM().toString(16));
            put("signature", sm.getS().toString(16));
            put("modulus", alice.getPublicKeyN().toString(16));
            put("publicExponent", alice.getPublicKeyE().toString(16));
        }}));

        BigInteger receivedKey = new BigInteger(json.get("key").asText(), 16);
        boolean isVerified = json.get("verified").asBoolean();

        assertEquals(m, receivedKey);
        assertTrue(isVerified);
    }
}
