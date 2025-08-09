package com.f1.rag.api.controller;

import com.f1.rag.api.service.QueryService;
import com.f1.rag.dto.AiResponseDto;
import com.f1.rag.api.service.RagService;
import com.f1.rag.dto.QueryRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RagController {

    private final RagService ragService;
    private final QueryService queryService;

    public RagController(RagService ragService, QueryService queryService) {
        this.ragService = ragService;
        this.queryService = queryService;
    }

    @PostMapping("/rag")
    public ResponseEntity<AiResponseDto> generateAnswer(@RequestBody QueryRequestDto queryRequestDto) {
            try {
                String sanitizedQuery = queryService.saveQueryFromUser(queryRequestDto);
                AiResponseDto response = ragService.retrieveInformation(sanitizedQuery);
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new AiResponseDto("Error processing the request: " + e.getMessage()));
            }
        }
}