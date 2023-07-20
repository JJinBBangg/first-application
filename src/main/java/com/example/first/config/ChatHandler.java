package com.example.first.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private static final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private final ObjectMapper objectMapper;
    // client 접속 시 호출 되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("start socket");
        sessions.add(session);
        //접속됬을 때 방생성 또는 초기값 db 에서 불러와 세팅
        Message message1 = Message.builder()
                .message("하이")
                .build();
        List<Message> messages = new ArrayList<>();
        messages.add(message1);


        for (Message message : messages) {
            ObjectMapper mapper = new ObjectMapper();
            String s = mapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(s));

        }

    }

    //클라이언트로부터 메세지를 수신 할 때 호출되는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("message = " + message.getPayload());
        String sendMessage = objectMapper.writeValueAsString(message);
        session.sendMessage(message);
    }

    //접속 해제 시 호출 되는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("session = " + session.toString());
        System.out.println("status = " + status.toString());
    }


    @Data
    private static class Message {
        private String message;

        @Builder
        public Message(String message) {
            this.message = message;
        }
    }
}
