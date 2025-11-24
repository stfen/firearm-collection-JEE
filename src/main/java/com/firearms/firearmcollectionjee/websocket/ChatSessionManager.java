package com.firearms.firearmcollectionjee.websocket;

import com.firearms.firearmcollectionjee.dto.chat.ChatMessageDto;
import com.firearms.firearmcollectionjee.event.chat.ChatEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.websocket.Session;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@Log
public class ChatSessionManager {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public void addSession(String login, Session session) {
        sessions.put(login, session);
    }

    public void removeSession(String login) {
        sessions.remove(login);
    }

    public void onChatEvent(@Observes ChatEvent event) {
        ChatMessageDto message = event.getMessage();
        String jsonMessage = String.format("{\"sender\": \"%s\", \"content\": \"%s\", \"timestamp\": \"%s\"}",
                message.getSenderLogin(),
                message.getContent(),
                message.getTimestamp().toString());

        if (message.getRecipientLogin() == null || message.getRecipientLogin().isEmpty()) {
            // Broadcast
            sessions.values().forEach(session -> sendToSession(session, jsonMessage));
        } else {
            // Private message
            Session recipientSession = sessions.get(message.getRecipientLogin());
            if (recipientSession != null) {
                sendToSession(recipientSession, jsonMessage);
            }
            // Also send back to sender so they see their own private message
            Session senderSession = sessions.get(message.getSenderLogin());
            if (senderSession != null && !message.getSenderLogin().equals(message.getRecipientLogin())) {
                sendToSession(senderSession, jsonMessage);
            }
        }
    }

    private void sendToSession(Session session, String message) {
        if (session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.severe("Error sending message to session: " + e.getMessage());
            }
        }
    }
}
