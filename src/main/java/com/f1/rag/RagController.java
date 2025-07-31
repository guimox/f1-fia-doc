package com.f1.rag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final ChatClient aiClient;
    private final VectorStore vectorStore;

    public RagController(ChatClient aiClient, VectorStore vectorStore) {
        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/rag")
    public ResponseEntity<String> generateAnswer(@RequestParam String query) {
        var searchRequest = SearchRequest
                .defaults()                      // uses default filter and vectorization logic
                .withQuery(query)               // your user input
                .withTopK(15);                  // how many chunks to return

        List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);

        String information = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

        var systemPromptTemplate = new SystemPromptTemplate(
                """
                You are an expert assistant specializing in the provided domain.
                
                INSTRUCTIONS:
                - Use ONLY the information provided below
                - Synthesize information from multiple sources when relevant
                - If information is incomplete, state what you know and what's missing
                - Cite specific sections when possible
                - If no relevant information exists, respond with "Unknown"
                
                CONTEXT INFORMATION:
                {information}
                
                Answer based solely on this context.
                """
        );
        var systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
        var userPromptTemplate = new PromptTemplate("{query}");
        var userMessage = userPromptTemplate.createMessage(Map.of("query", query));
        var prompt = new Prompt(List.of(systemMessage, userMessage));
        return ResponseEntity.ok(aiClient.call(prompt).getResult().getOutput().getContent());
    }
}