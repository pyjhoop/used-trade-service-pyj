package com.example.trade.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketHandler implements WebSocketHandler {

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENT = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENT.put(session.getId(), session);

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msg = (String) message.getPayload();

        CLIENT.forEach((key, value) -> {
            try {
                value.sendMessage(new TextMessage(msg));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        CLIENT.remove(session.getId());
        CLIENT.entrySet().forEach(arg -> {
            if(!arg.getKey().equals(session.getId())){
                try {
                    arg.getValue().sendMessage(new TextMessage(session.getId()+"님이 나가셨습니다."));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
