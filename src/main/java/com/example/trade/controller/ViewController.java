package com.example.trade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ViewController {

    @GetMapping("/chat")
    public String getChat(){
        return "chat";
    }

}
