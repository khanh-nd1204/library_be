package com.project.library.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class MailDTO {
    private String name;
    private String subject;
    private String template;
    private String to;
    private int otp;
    private String type;
}
