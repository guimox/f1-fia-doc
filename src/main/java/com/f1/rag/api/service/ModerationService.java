package com.f1.rag.api.service;

import org.springframework.ai.moderation.Moderation;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.ai.openai.OpenAiModerationOptions;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {

    private final OpenAiModerationModel openAiModerationModel;

    public ModerationService(OpenAiModerationModel openAiModerationModel) {
        this.openAiModerationModel = openAiModerationModel;
    }

    public void moderateText(String text) {
        OpenAiModerationOptions moderationOptions = OpenAiModerationOptions.builder()
                .model("text-moderation-latest")
                .build();

        ModerationPrompt moderationPrompt = new ModerationPrompt(text, moderationOptions);
        ModerationResponse response = openAiModerationModel.call(moderationPrompt);
        Moderation moderation = response.getResult().getOutput();

        System.out.println("TESTING HERE " + moderation);
    }
}
