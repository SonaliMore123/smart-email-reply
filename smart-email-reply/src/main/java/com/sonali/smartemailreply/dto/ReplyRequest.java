package com.sonali.smartemailreply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReplyRequest {
    @NotBlank(message = "Email content is required")
    private String emailContent;

    @NotBlank(message = "Tone is required")
    private String tone;
}
