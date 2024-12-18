package com.aleksandartokarev.spring_boot_websocket_artillery_load_test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class UserMessageHandler implements WebSocketHandler {

    // Code used for logic where randomly a message is sent to one of the users
    private List<String> usernames = Arrays.asList("user1", "user2", "user3");
    private int max = 2;
    private int min = 0;
    private final Random random = new Random();
    // End
    private final static String JWT_TOKEN_HEADER = "x-jwt-token";
    private final Map<String, CopyOnWriteArraySet<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 333)
    public void sendUpdates() throws IOException {
        // Simulate receiving message from Kafka/WS and sending it to the actual user
//        final int i = random.nextInt(max - min + 1) + min;
//        if (sessions.containsKey(usernames.get(i))) {
//            for (WebSocketSession session : sessions.get(usernames.get(i))) {
//                session.sendMessage(new TextMessage("Completed processing message: " + usernames.get(i)));
//            }
//        }

        // Send messages to all subscribed users
        for (String key : sessions.keySet()) {
            for (WebSocketSession session : sessions.get(key)) {
                session.sendMessage(new TextMessage("Completed processing message: " + key));
            }
        }
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established on session: {}", session.getId());
        log.info("Size:" + session.getHandshakeHeaders().size());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String parsedMessage = (String) message.getPayload();
        String username = getUsernameFromToken(session.getHandshakeHeaders());
        if (username == null) {
            return;
        }
        sessions.computeIfAbsent(username, v -> new CopyOnWriteArraySet<>());
        sessions.get(username).add(session);
        log.info("Message: {}", parsedMessage);
        session.sendMessage(new TextMessage("Started processing message: " + parsedMessage));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occurred: {} on session: {}", exception.getMessage(), session.getId());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
        String username = getUsernameFromToken(session.getHandshakeHeaders());
        if (username == null) {
            return;
        }
        sessions.get(username).remove(session);
        if (sessions.get(username).isEmpty()) {
            sessions.remove(username);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String getUsernameFromToken(HttpHeaders httpHeaders) {
        if (httpHeaders.containsKey(JWT_TOKEN_HEADER)) {
            try {
                Map<String, String> jwtPayload =
                        new ObjectMapper().readValue(new String(Base64.getUrlDecoder()
                                .decode(httpHeaders.get(JWT_TOKEN_HEADER)
                                        .get(0).split("\\.")[1])), new TypeReference<>() {});
                return jwtPayload.get("sub");
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
