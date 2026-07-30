package com.sonali.smartemailreply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyResponse {
    private String subject;
    private String reply;
}
