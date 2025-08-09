package com.f1.rag.dto;

public class AiResponseDto {

    private String tokenUsage;
    private final String response;

    public String getTokenUsage() {
        return tokenUsage;
    }

    public String getResponse() {
        return response;
    }

    public AiResponseDto(String response) {
        this.response = response;
    }
}
