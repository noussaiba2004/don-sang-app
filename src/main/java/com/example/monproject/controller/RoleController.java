package com.example.monproject.controller;

import com.example.monproject.model.User;
import com.example.monproject.service.AdminService;
import com.example.monproject.service.UserService;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class RoleController {

    private final UserService userService;
    private final AdminService adminService;

    public RoleController(UserService userService,AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {

        model.addAttribute("totalDonneurs", adminService.getTotalDonneurs());
        model.addAttribute("groupesSanguins", adminService.getGroupesSanguins());
        model.addAttribute("reservationsParMois", adminService.getReservationsParMois());
        model.addAttribute("repartitionAge", adminService.getRepartitionParAge());
        model.addAttribute("repartitionSexe", adminService.getRepartitionParSexe());

        return "dashboard/admin";
    }

    @GetMapping("/donneur")
    public String donneurDashboard(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);

        if (user != null && user.getDonneur() != null) {
            model.addAttribute("donneur", user.getDonneur());
        } else {
            model.addAttribute("donneur", null);
            model.addAttribute("error", "Impossible de récupérer les données du donneur.");
        }

        return "dashboard/donneur";
    }


}

