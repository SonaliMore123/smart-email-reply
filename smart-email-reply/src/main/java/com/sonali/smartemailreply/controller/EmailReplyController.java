package com.sonali.smartemailreply.controller;

import com.sonali.smartemailreply.dto.ReplyRequest;
import com.sonali.smartemailreply.dto.ReplyResponse;
import com.sonali.smartemailreply.service.EmailReplyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailReplyController{
    private final EmailReplyService emailReplyService;

    public EmailReplyController(EmailReplyService emailReplyService) {
        this.emailReplyService = emailReplyService;
    }

    @PostMapping("/reply")
    public ReplyResponse generateReply(
            @Valid @RequestBody ReplyRequest request
    ) {

        String aiResponse = emailReplyService.generateReply(
                request.getEmailContent(),
                request.getTone()
        );

        String subject = "Re: Your Email";
        String reply = aiResponse;

        try {

            if (aiResponse != null
                    && aiResponse.contains("Subject:")
                    && aiResponse.contains("Reply:")) {

                String[] parts = aiResponse.split("Reply:", 2);

                subject = parts[0]
                        .replace("Subject:", "")
                        .trim();

                reply = parts[1].trim();
            }

        } catch (Exception e) {

            System.err.println("Error parsing Gemini response: " + e.getMessage());

            subject = "Re: Your Email";
            reply = aiResponse;
        }

        return new ReplyResponse(subject, reply);
    }

}
