package com.example.monproject.controller;

import com.example.monproject.model.Centre;
import com.example.monproject.model.Donneur;
import com.example.monproject.model.User;
import com.example.monproject.repository.CentreRepository;
import com.example.monproject.repository.DonneurRepository;
import com.example.monproject.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CentreRepository centreRepository;
    private final DonneurRepository donneurRepository;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,CentreRepository centreRepository, DonneurRepository donneurRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.centreRepository = centreRepository;
        this.donneurRepository = donneurRepository;
    }

    // Formulaire de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Formulaire d'inscription
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Traitement du formulaire d'inscription
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Cet email est déjà utilisé.");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("DONNEUR");

        userRepository.save(user);

            Donneur donneur = new Donneur();
            donneur.setEmail(user.getEmail());
            donneurRepository.save(donneur);

            user.setDonneur(donneur);
            userRepository.save(user);


        return "redirect:/login?success";
    }


}

