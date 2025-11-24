package com.firearms.firearmcollectionjee.event.chat;

import com.firearms.firearmcollectionjee.dto.chat.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatEvent {
    private final ChatMessageDto message;
}
