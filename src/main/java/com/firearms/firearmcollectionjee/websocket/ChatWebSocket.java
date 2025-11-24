package com.firearms.firearmcollectionjee.websocket;

import jakarta.inject.Inject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.java.Log;

import java.security.Principal;

@ServerEndpoint("/ws/chat")
@Log
public class ChatWebSocket {

    @Inject
    private ChatSessionManager sessionManager;

    @OnOpen
    public void onOpen(Session session) {
        Principal principal = session.getUserPrincipal();
        if (principal != null) {
            String login = principal.getName();
            sessionManager.addSession(login, session);
            log.info("WebSocket opened for user: " + login);
        } else {
            log.warning("WebSocket opened without principal");
        }
    }

    @OnClose
    public void onClose(Session session) {
        Principal principal = session.getUserPrincipal();
        if (principal != null) {
            sessionManager.removeSession(principal.getName());
            log.info("WebSocket closed for user: " + principal.getName());
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.severe("WebSocket error: " + throwable.getMessage());
    }
}
