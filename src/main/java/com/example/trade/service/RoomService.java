package com.example.trade.service;

import com.example.trade.domain.ChatRoom;
import com.example.trade.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> getRoomList(){
        return chatRoomRepository.findAll();
    }

    public ChatRoom createRoom(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getRoom(String roomId){
        return chatRoomRepository.findById(Long.valueOf(roomId))
                .orElseThrow(()-> new RuntimeException(roomId+"에 해당하는 채팅룸을 찾지 못했습니다."));
    }

}
