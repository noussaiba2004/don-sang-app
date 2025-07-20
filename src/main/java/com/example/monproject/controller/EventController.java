package com.example.monproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

    @GetMapping("event")
    public String eventPage(){
        return "event";
    }
}
