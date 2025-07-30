package com.f1.rag;

import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAiEmbeddingService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.embedding.model}")
    private String model;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public float[] getEmbedding(String input) throws Exception {
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("input", input);
        payload.put("model", model);
        String json = objectMapper.writeValueAsString(payload);

        MediaType mediaType = MediaType.get("application/json");
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/embeddings")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new RuntimeException("OpenAI Error: " + response);

            JsonNode root = objectMapper.readTree(response.body().string());
            JsonNode data = root.get("data").get(0).get("embedding");

            float[] embedding = new float[data.size()];
            for (int i = 0; i < data.size(); i++) {
                embedding[i] = (float) data.get(i).asDouble();
            }
            return embedding;
        }
    }
}
