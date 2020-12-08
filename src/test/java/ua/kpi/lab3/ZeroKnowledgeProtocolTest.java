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

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZeroKnowledgeProtocolTest {

    ObjectMapper mapper = new ObjectMapper();

    String serverUrl = "http://asymcryptwebservice.appspot.com/znp/";
    String serverKeyUrl = serverUrl + "serverKey";
    String challengeUrl = serverUrl + "challenge";

    BasicHttpContext httpContext = new BasicHttpContext();
    CloseableHttpClient httpClient = HttpClients.createDefault();

    ZeroKnowledgeProtocol alice = new ZeroKnowledgeProtocol();

    int yBitLength = 512;

    public JsonNode getRequestJson(String url) throws IOException {
        System.out.println("\nGET " + url);
        try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url), httpContext)) {
            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("RESPONSE: " + json + "\n");
            return mapper.readValue(json, JsonNode.class);
        }
    }


    @Test
    public void challenge() throws IOException {

        JsonNode jsonKey = getRequestJson(serverKeyUrl);
        BigInteger serverN = new BigInteger(jsonKey.get("modulus").asText(), 16);

        JsonNode json = getRequestJson(challengeUrl + "?y=" + alice.zeroKnowledgeProtocolSendY(serverN, yBitLength).toString(16));
        BigInteger root = new BigInteger(json.get("root").asText(), 16);

        assertTrue(alice.zeroKnowledgeProtocolReceiveZVerify(root, serverN));

    }

    @RepeatedTest(5)
    public void attack() throws IOException {

        JsonNode jsonKey = getRequestJson(serverKeyUrl);
        BigInteger serverN = new BigInteger(jsonKey.get("modulus").asText(), 16);

        BigInteger z;
        Optional<BigInteger> probableN;
        int k = 0;
        do {
            JsonNode json = getRequestJson(challengeUrl + "?y=" + alice.zeroKnowledgeProtocolAttackSendT(serverN, yBitLength).toString(16));
            z = new BigInteger(json.get("root").asText(), 16);

            probableN = alice.zeroKnowledgeProtocolAttackReceiveZVerify(z, serverN);
            k++;
        } while (!probableN.isPresent());

        System.out.println("Found: " + probableN.get().toString(16));
        System.out.println("Iterations: " + k);

    }


}
