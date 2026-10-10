package com.firearms.firearmcollectionjee.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto implements Serializable {
    private String senderLogin;
    private String recipientLogin; // null means broadcast to all
    private String content;
    private LocalDateTime timestamp;
}
