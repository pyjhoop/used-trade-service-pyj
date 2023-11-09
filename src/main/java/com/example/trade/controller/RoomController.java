package com.example.trade.controller;

import com.example.trade.domain.ChatRoom;
import com.example.trade.repository.ChatRoomRepository;
import com.example.trade.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms")
    public String chatRooms(Model model){
        List<ChatRoom> rooms = roomService.getRoomList();
        model.addAttribute("list",rooms);
        log.info("채팅방 리스트 입장");
        return "rooms";
    }

    @PostMapping("/room")
    public String createRoom(@RequestParam String name){

        roomService.createRoom(name);
        log.info("{} 이름으로 채팅방이 생성되었습니다.", name);

        return "redirect:/chat/rooms";
    }

    @GetMapping("/room")
    public String getRoom(String roomId, Model model){
        ChatRoom room = roomService.getRoom(roomId);
        model.addAttribute("room", room);
        log.info("{} 번호의 채팅방에 입장하였습니다.", roomId);

        return "room";
    }

}
