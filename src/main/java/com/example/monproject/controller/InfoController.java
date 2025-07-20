package com.example.monproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/info")
    public String infoPage() {
        return "info"; // ce fichier doit exister dans src/main/resources/templates/info.html
    }
}
