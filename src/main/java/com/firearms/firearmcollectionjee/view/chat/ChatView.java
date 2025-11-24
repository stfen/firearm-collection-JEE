package com.firearms.firearmcollectionjee.view.chat;

import com.firearms.firearmcollectionjee.dto.chat.ChatMessageDto;
import com.firearms.firearmcollectionjee.event.chat.ChatEvent;
import com.firearms.firearmcollectionjee.service.UserService;

import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ViewScoped
@Named
@Log
public class ChatView implements Serializable {

    private final UserService userService;
    private final Event<ChatEvent> chatEvent;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String recipient;

    @Inject
    public ChatView(UserService userService, Event<ChatEvent> chatEvent) {
        this.userService = userService;
        this.chatEvent = chatEvent;
    }

    public List<String> getUsers() {
        String currentUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        return userService.getUsersForChat().stream()
                .map(user -> user.getLogin())
                .filter(login -> !login.equals(currentUser))
                .collect(Collectors.toList());
    }

    public void sendMessage() {
        if (message != null && !message.trim().isEmpty()) {
            String sender = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

            ChatMessageDto msg = ChatMessageDto.builder()
                    .senderLogin(sender)
                    .recipientLogin(recipient)
                    .content(message)
                    .timestamp(LocalDateTime.now())
                    .build();

            chatEvent.fire(new ChatEvent(msg));

            // Clear message after sending
            message = "";
        }
    }
}
