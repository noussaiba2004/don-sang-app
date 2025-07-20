package com.example.monproject.service;

import com.example.monproject.dto.ReservationForm;
import com.example.monproject.model.*;
import com.example.monproject.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DonneurRepository donneurRepository;
    private final CreneauRepository creneauRepository;
    private final SpringTemplateEngine templateEngine;
    private final CentreRepository centreRepository;

    private EmailService emailService;

    @Autowired
    public void setEmailService(@Lazy EmailService emailService) {
        this.emailService = emailService;
    }

    @Transactional
    public void reserver(ReservationForm form, Donneur donneur)
    {
        Optional<Centre> centreOpt = centreRepository.findById(form.getCentreId());
        Optional<Creneau> creneauOpt = creneauRepository.findById(form.getCreneauId());
        System.out.println("Donneur connecté : " + donneur.getEmail());
        System.out.println("ID Créneau reçu : " + form.getCreneauId());

        if (creneauOpt.isEmpty()) {
            throw new IllegalStateException("Créneau introuvable (id = " + form.getCreneauId() + ")");
        }

        if (centreOpt.isEmpty()) {
            throw new IllegalStateException("Centre introuvable (id = " + form.getCentreId() + ")");
        }
        if (form.getCentreId() == null) {
            throw new IllegalArgumentException("L'identifiant du centre est requis.");
        }
        if (form.getCreneauId() == null) {
            throw new IllegalArgumentException("Veuillez choisir un créneau avant de réserver.");
        }

        if (donneur.getNom() == null || donneur.getNom().isEmpty()) {
            donneur.setNom(form.getNom());
            donneur.setPrenom(form.getPrenom());
            donneur.setSexe(form.getSexe());
            donneur.setDateNaissance(form.getDateNaissance());
            donneur.setGroupeSanguin(form.getGroupeSanguin());
            donneur.setLocalisation(form.getLocalisation());
            donneurRepository.save(donneur); // mise à jour
        }
        // Vérification de l'âge (18 - 65 ans)
        LocalDate dateNaissance = donneur.getDateNaissance();
        if (dateNaissance == null) {
            throw new IllegalArgumentException("La date de naissance est requise pour vérifier l'âge.");
        }

        int age = Period.between(dateNaissance, LocalDate.now()).getYears();
        if (age < 18 || age > 65) {
            throw new IllegalArgumentException("Vous devez avoir entre 18 et 65 ans pour pouvoir donner votre sang.");
        }

        Centre centre = centreOpt.get();
        Creneau creneau = creneauOpt.get();
        creneau.setDisponible("réservé"); // Marquer comme réservé

        // Récupérer le dernier don
        Optional<Reservation> lastReservationOpt = reservationRepository.findTopByDonneurOrderByCreneau_DateDesc(donneur);

        LocalDate today = LocalDate.now();

        if (lastReservationOpt.isPresent()) {
            Reservation last = lastReservationOpt.get();
            LocalDate lastDate = last.getCreneau().getDate();
            TypeCollecte lastType = last.getTypeCollecte();
            TypeCollecte currentType = form.getTypeCollecte();

            // 1. Cas du sang total
            if (currentType == TypeCollecte.SANG) {
                if (lastType == TypeCollecte.PLASMA || lastType == TypeCollecte.PLAQUETTES) {
                    if (lastDate.plusWeeks(2).isAfter(today)) {
                        throw new IllegalArgumentException("Vous devez attendre 2 semaines après un don de plasma ou de plaquettes avant de donner du sang.");
                    }
                } else if (lastType == TypeCollecte.SANG) {
                    int mois = donneur.getSexe().equalsIgnoreCase("HOMME") ? 2 : 3;
                    if (lastDate.plusMonths(mois).isAfter(today)) {
                        throw new IllegalArgumentException("Vous devez attendre " + mois + " mois entre deux dons de sang.");
                    }
                }
            }

            //  2. Cas du plasma ou plaquettes
            if (currentType == TypeCollecte.PLASMA || currentType == TypeCollecte.PLAQUETTES) {
                if (lastType == TypeCollecte.SANG) {
                    if (lastDate.plusMonths(1).isAfter(today)) {
                        throw new IllegalArgumentException("Vous devez attendre 1 mois après un don de sang pour donner plasma ou plaquettes.");
                    }
                } else {
                    if (lastDate.plusWeeks(2).isAfter(today)) {
                        throw new IllegalArgumentException("Vous devez attendre 2 semaines entre deux dons de plasma ou de plaquettes.");
                    }
                }
            }
        }

        // Vérifier les intervalles de temps spécifiques et le nombre maximum annuel
        LocalDate oneYearAgo = today.minusYears(1);
        TypeCollecte typeCollecte = form.getTypeCollecte();

        int donationsThisYear = reservationRepository.countByDonneurAndTypeCollecteAndCreneau_DateBetween(
                donneur, typeCollecte, oneYearAgo, today);

        if (typeCollecte == TypeCollecte.SANG) {
            if ("HOMME".equalsIgnoreCase(donneur.getSexe())) {
                if (donationsThisYear >= 6) {
                    throw new IllegalArgumentException("Vous avez atteint le maximum de 6 dons de sang total cette année.");
                }
                if (lastReservationOpt.isPresent()
                        && lastReservationOpt.get().getTypeCollecte() == TypeCollecte.SANG
                        && lastReservationOpt.get().getCreneau().getDate().plusMonths(2).isAfter(today)) {
                    throw new IllegalArgumentException("Vous devez attendre 2 mois entre deux dons de sang total.");
                }
            } else if ("FEMME".equalsIgnoreCase(donneur.getSexe())) {
                if (donationsThisYear >= 4) {
                    throw new IllegalArgumentException("Vous avez atteint le maximum de 4 dons de sang total cette année.");
                }
                if (lastReservationOpt.isPresent()
                        && lastReservationOpt.get().getTypeCollecte() == TypeCollecte.SANG
                        && lastReservationOpt.get().getCreneau().getDate().plusMonths(3).isAfter(today)) {
                    throw new IllegalArgumentException("Vous devez attendre 3 mois entre deux dons de sang total.");
                }
            }
        } else if (typeCollecte == TypeCollecte.PLASMA) {
            if (donationsThisYear >= 24) {
                throw new IllegalArgumentException("Vous avez atteint le maximum de 24 dons de plasma cette année.");
            }
            if (lastReservationOpt.isPresent()
                    && lastReservationOpt.get().getTypeCollecte() == TypeCollecte.PLASMA
                    && lastReservationOpt.get().getCreneau().getDate().plusWeeks(2).isAfter(today)) {
                throw new IllegalArgumentException("Vous devez attendre 2 semaines entre deux dons de plasma.");
            }
        } else if (typeCollecte == TypeCollecte.PLAQUETTES) {
            if (donationsThisYear >= 12) {
                throw new IllegalArgumentException("Vous avez atteint le maximum de 12 dons de plaquettes cette année.");
            }
            if (lastReservationOpt.isPresent()
                    && lastReservationOpt.get().getTypeCollecte() == TypeCollecte.PLAQUETTES
                    && lastReservationOpt.get().getCreneau().getDate().plusWeeks(2).isAfter(today)) {
                throw new IllegalArgumentException("Vous devez attendre 2 semaines entre deux dons de plaquettes.");
            }
        }

        Reservation reservation = new Reservation();
        reservation.setDonneur(donneur);
        reservation.setCreneau(creneau);
        reservation.setCentre(centre);
        reservation.setTypeCollecte(form.getTypeCollecte());
        reservation.setStatus("EN ATTENTE");
        reservation.setConfirme(false);
        reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByCentre(Centre centre) {
        return reservationRepository.findByCreneau_Centre_IdAndStatus(centre.getId(), "EN ATTENTE");
    }


    public void confirmerReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalStateException("Réservation introuvable"));
        reservation.setConfirme(true);
        reservation.setStatus("CONFIRMÉE");
        reservationRepository.save(reservation);

        envoyerEmailConfirmation(reservation);
    }


    private void envoyerEmailConfirmation(Reservation reservation) {
        Donneur donneur = reservation.getDonneur();
        Creneau creneau = reservation.getCreneau();

        Context context = new Context();
        context.setVariable("nom", donneur.getNom());
        context.setVariable("date", creneau.getDate().toString());
        context.setVariable("heure", creneau.getHeure().toString());
        context.setVariable("statut", "CONFIRMÉE");

        String htmlContent = templateEngine.process("mail/confirmation", context);
        emailService.sendHtmlEmail(donneur.getEmail(), "Confirmation de votre don", htmlContent);
    }

    private void envoyerEmailAnnulation(Reservation reservation) {
        Donneur donneur = reservation.getDonneur();
        Creneau creneau = reservation.getCreneau();

        Context context = new Context();
        context.setVariable("nom", donneur.getNom());
        context.setVariable("date", creneau.getDate().toString());
        context.setVariable("heure", creneau.getHeure().toString());
        context.setVariable("statut", "ANNULÉE");

        String htmlContent = templateEngine.process("mail/confirmation", context);
        emailService.sendHtmlEmail(donneur.getEmail(), "Annulation de votre réservation", htmlContent);
    }

    @Transactional
    public void supprimerReservationAvecNotification(Reservation reservation) {
        // Envoi de l'email
        envoyerEmailAnnulation(reservation);

        // Rompre la relation du côté Reservation
        Creneau creneau = reservation.getCreneau();
        if (creneau != null) {
            // Remettre le créneau comme disponible
            creneau.setDisponible("Libre");

            // Casser les liens bidirectionnels
            creneau.setReservation(null);
            reservation.setCreneau(null);
        }

        reservationRepository.delete(reservation);
    }


    public List<Reservation> getReservationsByCentreAndDate(Centre centre, LocalDate date) {
        return reservationRepository.findByCreneau_CentreAndCreneau_Date(centre, date);
    }
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Réservation non trouvée avec l'id : " + id));
    }

    public List<Reservation> getReservationsByDonneurAndStatus(Donneur donneur, String confirmee) {
        return reservationRepository.findByDonneurAndStatus(donneur, confirmee);
    }
}
