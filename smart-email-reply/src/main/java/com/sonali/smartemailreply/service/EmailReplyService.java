package com.sonali.smartemailreply.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class EmailReplyService {
    private final GeminiService geminiService;


    public EmailReplyService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    public String generateReply(String emailContent, String tone) {
        return geminiService.generateReply(emailContent, tone);
    }
}
