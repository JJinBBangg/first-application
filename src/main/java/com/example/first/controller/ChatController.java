package com.example.first.controller;

import com.example.first.entity.UserSession;
import com.example.first.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @GetMapping("/chats")
    public void get (){
        // 참가한 대화 목록 조회
    }

    @PostMapping("/chats")
    public void add(){
        // 새로운 대화 생성
    }

    @MessageMapping("/createRoom")
    public void createRoom(UserSession userSession, String roomName){
        log.info(">>{}", roomName);
        chatService.createRoom(userSession.getUserId(), roomName);
    }
    @MessageMapping("/send")
    public void sendMessage(UserSession userSession, String message){
        chatService.sendMessage(userSession.getUserId(), message);
    }
}
