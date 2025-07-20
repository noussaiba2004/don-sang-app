package com.example.monproject.controller;


import com.example.monproject.dto.ReservationForm;
import com.example.monproject.model.*;
import com.example.monproject.dto.CentreMapDTO;
import com.example.monproject.repository.DonneurRepository;
import com.example.monproject.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/donneur") // préfixe commun à toutes les méthodes
public class DonneurController {


    private final CentreService centreService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final DonneurRepository  donneurRepository;
    private final DonneurService donneurService;
    private final DonService donService;

    public DonneurController(CentreService centreService,ReservationService reservationService,UserService userService,DonneurRepository donneurRepository,DonneurService donneurService,DonService donService) {
        this.centreService = centreService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.donneurRepository = donneurRepository;
        this.donneurService = donneurService;
        this.donService=donService;
    }

    @GetMapping("/page") // devient /donneur/page
    public String afficherPageDonneur(Model model, Principal principal) {

        String email = principal.getName(); // Récupère l'email de l'utilisateur connecté
        User user = userService.findByEmail(email);
        Donneur donneur = user.getDonneur();

        if (donneur != null) {
            model.addAttribute("donneur", donneur);
        }
        model.addAttribute("centres", List.of()); // vide au départ
        return "dashboard/donneur";
    }

    @GetMapping("/rechercher")
    public String rechercherCentres(@RequestParam("q") String query, Model model, Principal principal) {
        try {
            List<Centre> centres = centreService.findTop3CentresProches(query);

            if (centres.isEmpty()) {
                model.addAttribute("message", "Aucun centre trouvé pour la localisation : " + query);
            }

            // Ajout de la liste pour affichage Thymeleaf classique
            model.addAttribute("centres", centres);

            // ✅ Mapper vers DTO (pas les entités Hibernate)
            List<CentreMapDTO> centreDtos = centres.stream().map(c -> new CentreMapDTO(
                    c.getId(),
                    c.getNom(),
                    c.getAdresse(),
                    c.getLatitude(),
                    c.getLongitude(),
                    c.getEtat()
            )).toList();

            // ✅ Sérialiser avec le module JavaTime (même si ici on a plus de LocalDate car DTO = pur)
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            String centresJson = mapper.writeValueAsString(centreDtos);
            model.addAttribute("centresJson", centresJson);

            // Ajout du donneur
            String email = principal.getName();
            User user = userService.findByEmail(email);
            Donneur donneur = user.getDonneur();
            if (donneur != null) {
                model.addAttribute("donneur", donneur);
            }

            return "dashboard/donneur";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de la recherche : " + e.getMessage());
            return "dashboard/donneur";
        }
    }

    @GetMapping("/historique")
    public String afficherHistoriqueDons(Model model, Principal principal) {
        Donneur donneur = donneurService.findByEmail(principal.getName());

        // Récupérer les dons liés au donneur connecté ET assurés
        List<Don> dons = donService.getDonsAssuresByDonneur(donneur);

        model.addAttribute("dons", dons);
        return "donneur/historique";
    }

    @GetMapping("/reservation")
    public String afficherFormulaireReservation(Model model) {
        List<Centre> centres = centreService.getAllCentres();
        model.addAttribute("centres", centres);
        model.addAttribute("reservation", new ReservationForm());
        model.addAttribute("typeCollectes", TypeCollecte.values());
        return "donneur/reservation";
    }

    @PostMapping("/reserver")
    public String traiterReservation(@ModelAttribute("reservation") ReservationForm form,
                                     Model model,
                                     RedirectAttributes redirectAttributes,Principal principal) {
    try {
            Donneur donneur = donneurRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalStateException("Donneur introuvable avec l'email : " + principal.getName()));

            reservationService.reserver(form, donneur);
            redirectAttributes.addFlashAttribute("popupMessage", "Réservation enregistrée avec succès !");
            return "redirect:/donneur/reservation";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
            model.addAttribute("centres", centreService.getAllCentres());
            model.addAttribute("reservation", form); // important pour éviter IllegalStateException
            model.addAttribute("typeCollectes", TypeCollecte.values());
            return "donneur/reservation";
        }
    }




}

