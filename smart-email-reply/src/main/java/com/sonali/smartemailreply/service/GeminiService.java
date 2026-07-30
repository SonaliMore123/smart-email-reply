package com.sonali.smartemailreply.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
    private final RestClient restClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public GeminiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String generateReply(String emailContent, String tone) {

        String prompt = """
                You are an AI Email Assistant.

                Generate BOTH an email subject and an email reply.

                Original Email:
                %s

                Tone:
                %s

                Rules:
                1. Return ONLY in the following format.
                2. Do NOT add explanations.
                3. Do NOT use Markdown.

                Subject: <Email Subject>

                Reply:
                <Email Reply>
                """.formatted(emailContent, tone);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        try {

            @SuppressWarnings("unchecked")
            Map<String, Object> response =
                    (Map<String, Object>) restClient.post()
                            .uri(GEMINI_URL + "?key=" + apiKey)
                            .body(requestBody)
                            .retrieve()
                            .body(Map.class);

            return extractReply(response);

        } catch (Exception e) {

            System.err.println("Gemini API Error: " + e.getMessage());

            return """
                    Subject: Re: Your Email

                    Reply:
                    Gemini is currently unavailable or experiencing high demand.
                    Please try again after a few minutes.
                    """;
        }
    }

    @SuppressWarnings("unchecked")
    private String extractReply(Map<String, Object> response) {

        try {

            List<Map<String, Object>> candidates =
                    (List<Map<String, Object>>) response.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return """
                        Subject: Re: Your Email

                        Reply:
                        Unable to generate reply.
                        """;
            }

            Map<String, Object> content =
                    (Map<String, Object>) candidates.get(0).get("content");

            List<Map<String, Object>> parts =
                    (List<Map<String, Object>>) content.get("parts");

            if (parts == null || parts.isEmpty()) {
                return """
                        Subject: Re: Your Email

                        Reply:
                        Unable to generate reply.
                        """;
            }

            return parts.get(0).get("text").toString();

        } catch (Exception e) {

            System.err.println("Response Parsing Error: " + e.getMessage());

            return """
                    Subject: Re: Your Email

                    Reply:
                    Unable to process Gemini response.
                    """;
        }
    }
}
