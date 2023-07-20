package com.example.first.service;

import com.example.first.config.ChatHandler;
import com.example.first.repository.MybatisChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MybatisChatRepository chatRepository;
    private final ChatHandler chatHandler;

    public void sendMessage(Long userId, String message){

    }

    public void createRoom(Long userId, String roomName) {
    }
}
