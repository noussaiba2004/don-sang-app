package com.example.monproject.controller;

import com.example.monproject.model.*;
import com.example.monproject.repository.ActualiteRepository;
import com.example.monproject.repository.BesoinSangRepository;
import com.example.monproject.repository.CreneauRepository;
import com.example.monproject.repository.ReservationRepository;
import com.example.monproject.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/centre")
@RequiredArgsConstructor
public class CentreController {

    private final UserService userService;
    private final CreneauRepository creneauRepository;
    private final ReservationRepository reservationRepository;
    private final ActualiteRepository actualiteRepository;
    private final BesoinSangRepository besoinSangRepository;
    private final CentreService centreService;
    private final ReservationService reservationService;
    private final DonService donService;
    private final EmailService emailService;

    /**
     * Page d'accueil du centre connecté
     */
    @GetMapping
    public String centreDashboard(Model model, Principal principal, RedirectAttributes redirectAttributes) {

        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Veuillez vous connecter pour accéder à cet espace.");
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        List<Creneau> creneaux = creneauRepository.findByCentre(centre);
        List<Reservation> reservations = reservationRepository.findByCreneau_Centre(centre);
        List<BesoinSang> besoins = besoinSangRepository.findByCentre(centre);
        List<Actualite> actualites = actualiteRepository.findByCentre(centre);


        model.addAttribute("centre", centre);
        model.addAttribute("creneaux", creneaux);
        model.addAttribute("reservations", reservations);
        model.addAttribute("besoins", besoins);
        model.addAttribute("actualites", actualites);

        return "dashboard/centre";
    }

    /**
     * Ajouter un créneau
     */
    @PostMapping("/creer-creneau")
    public String ajouterCreneau(@RequestParam("date") String date,
                                 @RequestParam("heure") String heure,
                                 Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        Creneau creneau = new Creneau();
        creneau.setCentre(centre);
        creneau.setDate(LocalDate.parse(date));
        creneau.setHeure(LocalTime.parse(heure));
        creneau.setDisponible("libre");

        creneauRepository.save(creneau);

        return "redirect:/centre";
    }

    /**
     * Supprimer un créneau
     */
    @PostMapping("/supprimer-creneau/{id}")
    @Transactional
    public String supprimerCreneau(@PathVariable Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        Creneau creneau = creneauRepository.findById(id).orElse(null);
        if (creneau != null && creneau.getCentre().getId().equals(centre.getId())) {
            creneauRepository.deleteById(id);
        }

        return "redirect:/centre";
    }

    //  publier actualités

    @PostMapping("/publier-actualite")
    public String ajouterActualite(@RequestParam String titre,
                                   @RequestParam String contenu,
                                   Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        Actualite actualite = new Actualite();
        actualite.setTitre(titre);
        actualite.setContenu(contenu);
        actualite.setDatePublication(LocalDateTime.now());
        actualite.setCentre(centre);

        actualiteRepository.save(actualite);

        // ✅ Email à tous les donneurs
        String sujet = "📰 Nouvelle actualité de " + centre.getNom();
        String html = "<h2>" + titre + "</h2>" +
                "<p><strong>Centre :</strong> " + centre.getNom() + "</p>" +
                "<p>" + contenu + "</p>";

        emailService.sendHtmlEmailToMultipleRecipients(
                userService.getEmailsDesDonneurs(), sujet, html
        );

        return "redirect:/centre";
    }

    /**
     * Supprimer une actualité (depuis l’espace centre)
     */
    @PostMapping("/supprimer-actualite/{id}")
    public String supprimerActualite(@PathVariable Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        Optional<Actualite> actualiteOpt = actualiteRepository.findById(id);
        if (actualiteOpt.isPresent()) {
            Actualite actualite = actualiteOpt.get();
            if (actualite.getCentre().getId().equals(centre.getId())) {
                actualiteRepository.deleteById(id);
            }
        }

        return "redirect:/centre";
    }

    //mise a jours besoin de sang
    @PostMapping("/update-besoins")
    public String mettreAJourBesoins(@RequestParam String groupeSanguin,
                                     @RequestParam int quantiteNecessaire,
                                     @RequestParam(required = false) Long id,
                                     @RequestParam(required = false) boolean urgence,
                                     Principal principal) {

        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();
        BesoinSang besoin;

        if (id != null) {
            besoin = besoinSangRepository.findById(id).orElse(null);
            if (besoin == null || !besoin.getCentre().getId().equals(centre.getId())) {
                return "redirect:/centre";
            }
        } else {
            besoin = besoinSangRepository.findByCentre(centre).stream()
                    .filter(b -> b.getGroupeSanguin().equals(groupeSanguin))
                    .findFirst()
                    .orElse(new BesoinSang());
            besoin.setCentre(centre);
            besoin.setGroupeSanguin(groupeSanguin);
        }

        besoin.setQuantite(quantiteNecessaire);
        besoin.setUrgence(urgence);
        besoinSangRepository.save(besoin);

        // ✅ Email à tous les donneurs
        String sujet = "🩸 Besoin urgent en sang - " + centre.getNom();
        String contenu = "<h2>Un besoin de sang vient d'être mis à jour</h2>" +
                "<p>Centre : <strong>" + centre.getNom() + "</strong></p>" +
                "<p>Groupe : <strong>" + groupeSanguin + "</strong></p>" +
                "<p>Quantité : <strong>" + quantiteNecessaire + " unités</strong></p>" +
                "<p>Urgence : " + (urgence ? "<span style='color:red;'>Oui</span>" : "Non") + "</p>";

        emailService.sendHtmlEmailToMultipleRecipients(
                userService.getEmailsDesDonneurs(), sujet, contenu
        );

        return "redirect:/centre";
    }

    @PostMapping("/supprimer-besoin/{id}")
    public String supprimerBesoin(@PathVariable Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        BesoinSang besoin = besoinSangRepository.findById(id).orElse(null);
        if (besoin != null && besoin.getCentre().getId().equals(centre.getId())) {
            besoinSangRepository.delete(besoin);
        }

        return "redirect:/centre";
    }

    @GetMapping("/editer-besoin/{id}")
    public String editerBesoin(@PathVariable Long id, Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        BesoinSang besoin = besoinSangRepository.findById(id).orElse(null);
        if (besoin == null || !besoin.getCentre().getId().equals(centre.getId())) {
            return "redirect:/centre"; // sécurité : accès interdit
        }

        model.addAttribute("besoin", besoin);
        model.addAttribute("centre", centre); // si tu as besoin d'afficher le nom du centre
        return "dashboard/editer-besoin";
    }

    @PostMapping("/notifier")
    public String notifierDonneurs(@RequestParam String message, Principal principal,RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());
        Centre centre = user.getCentre();

        String sujet = "🩸 Message du centre de don : " + centre.getNom();

        String html = "<h2>Notification du centre " + centre.getNom() + "</h2>" +
                "<p>" + message.replace("\n", "<br>") + "</p>";

        try {
            emailService.sendHtmlEmailToMultipleRecipients(
                    userService.getEmailsDesDonneurs(), sujet, html
            );
            redirectAttributes.addFlashAttribute("notifie", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("notifie", "fail");
        }
        return "redirect:/centre";
    }


    @GetMapping("/reservations")
    public String afficherReservationsCentre(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                             Model model,
                                             Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        Centre centre = centreService.findByUser(user).orElseThrow();

        List<Reservation> reservations;

        if (date != null) {
            reservations = reservationService.getReservationsByCentreAndDate(centre, date);
        } else {
            reservations = reservationService.getReservationsByCentre(centre);
        }

        List<Don> donsEnAttente = donService.getDonsEnAttenteParCentre(centre);

        model.addAttribute("reservations", reservations);
        model.addAttribute("dons", donsEnAttente);
        model.addAttribute("date", date);
        return "centre/reservations";
    }

    @PostMapping("/reservation/confirmer/{id}")
    public String confirmerReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Reservation reservation = reservationService.findById(id); // à implémenter
            reservationService.confirmerReservation(id);
            donService.creerDonPourReservation(reservation);
            redirectAttributes.addFlashAttribute("popupMessage", "Réservation confirmée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/centre/reservations";
    }

    @PostMapping("/reservation/annuler/{id}")
    public String annulerReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Reservation reservation = reservationService.findById(id);
            reservationService.supprimerReservationAvecNotification(reservation);
            redirectAttributes.addFlashAttribute("popupMessage", "Réservation annulée avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/centre/reservations";
    }

    @PostMapping("/don/assurer/{id}")
    public String assurerDon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Don don = donService.assurerDon(id); // crée le don
            donService.envoyerEmailRemerciement(id);
            redirectAttributes.addFlashAttribute("popupMessage", "Don assuré avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l’assurance : " + e.getMessage());
        }
        return "redirect:/centre/reservations"; // reste sur la même page
    }


    @PostMapping("/don/annuler/{id}")
    public String annulerDon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Don don = donService.findById(id);
            donService.supprimer(don);
            redirectAttributes.addFlashAttribute("popupMessage", "Don annulé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'annulation : " + e.getMessage());
        }
        return "redirect:/centre/reservations";
    }


}
