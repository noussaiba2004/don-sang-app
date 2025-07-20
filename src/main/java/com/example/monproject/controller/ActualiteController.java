package com.example.monproject.controller;

import com.example.monproject.model.Actualite;
import com.example.monproject.model.BesoinSang;
import com.example.monproject.repository.ActualiteRepository;
import com.example.monproject.repository.BesoinSangRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ActualiteController {

    private final ActualiteRepository actualiteRepository;
    private final BesoinSangRepository besoinSangRepository;
    public ActualiteController(ActualiteRepository actualiteRepository, BesoinSangRepository besoinSangRepository) {
        this.actualiteRepository = actualiteRepository;
        this.besoinSangRepository = besoinSangRepository;
    }

    @GetMapping("/actuality")
    public String afficherActualites(Model model) {
        List<Actualite> actualites = actualiteRepository.findAll();
        List<BesoinSang> besoins = besoinSangRepository.findAll();
        model.addAttribute("actualites", actualites);
        model.addAttribute("besoins", besoins);
        return "actuality";
    }
}
