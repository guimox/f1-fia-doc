package com.f1.rag.api.service;

import com.f1.rag.dto.AiResponseDto;
import com.f1.rag.dto.QueryRequestDto;
import com.f1.rag.utils.PromptUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RagService {
    private final VectorStore vectorStore;
    private final PromptUtils promptUtils;

    private final ChatClient aiClient;

    public RagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore, PromptUtils promptUtils) {
        this.aiClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
        this.promptUtils = promptUtils;
    }

    public AiResponseDto retrieveInformation(String query) {
        try {
            String sanitizedQuery = promptUtils.sanitizeContext(query);

            if (sanitizedQuery.contains("REMOVED") || sanitizedQuery.trim().isEmpty()) {
                throw new RuntimeException("Prompt injection or something related");
            }

            var searchRequest = SearchRequest.builder().query(sanitizedQuery).topK(15);

            List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest.build());

            StringBuilder rawInformation = new StringBuilder();
            for (Document doc : similarDocuments) {
                rawInformation.append(doc.getFormattedContent()).append(System.lineSeparator());
            }

            SystemPromptTemplate systemPromptTemplate = promptUtils.getPromptTemplate();
            var systemMessage = systemPromptTemplate.createMessage(Map.of("information", rawInformation));
            var userPromptTemplate = new PromptTemplate("{query}");
            var userMessage = userPromptTemplate.createMessage(Map.of("query", sanitizedQuery));
            var prompt = new Prompt(List.of(systemMessage, userMessage));

            var response = aiClient.prompt(prompt);
            String returnAiString = response.call().content();
            return new AiResponseDto(returnAiString);

        } catch (Exception e) {
            System.err.println("Error retrieving information: " + e.getMessage());
            throw new RuntimeException("Error when trying to generate a response", e);
        }
    }
}
