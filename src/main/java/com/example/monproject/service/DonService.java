package com.example.monproject.service;

import com.example.monproject.model.Centre;
import com.example.monproject.model.Don;
import com.example.monproject.model.Donneur;
import com.example.monproject.model.Reservation;
import com.example.monproject.repository.DonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonService {

    private final DonRepository donRepository;
    private final EmailService emailService;

    public void creerDonPourReservation(Reservation reservation) {
        Don don = new Don();
        don.setReservation(reservation);
        don.setDonneur(reservation.getDonneur());
        don.setType(reservation.getTypeCollecte().name()); // si un seul type
        don.setDateCreation(reservation.getCreneau().getDate());
        don.setCentre(reservation.getCentre());
        don.setStatut("en_attente"); // ou "En attente", selon ton modèle

        donRepository.save(don);
    }


    public void mettreAJourStatut(Long id, String statut) {
        Don don = donRepository.findById(id).orElseThrow();
        don.setStatut(statut);
        donRepository.save(don);
    }

    public List<Don> getDonsEnAttenteParCentre(Centre centre) {
        return donRepository.findByReservation_Creneau_CentreAndStatut(centre, "en_attente");
    }

    public Don findById(Long id) {
        return donRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Don non trouvé avec l'id : " + id));
    }

    public void save(Don don) {
        donRepository.save(don);
    }
    public void supprimer(Don don) {
        donRepository.delete(don);
    }

    @Transactional
    public Don assurerDon(Long donId) {
        Don don = donRepository.findById(donId)
                .orElseThrow(() -> new IllegalArgumentException("Don introuvable"));
        don.setStatut("assuré"); // ou autre champ pour marquer comme historique
        donRepository.save(don);
        // ✅ Envoi de l’email de remerciement ici
        envoyerEmailRemerciement(donId);
        return don;
    }

    public void envoyerEmailRemerciement(Long donId) {
        Don don = donRepository.findById(donId).orElseThrow();
        Reservation reservation = don.getReservation();
        Donneur donneur = reservation.getDonneur();

        emailService.sendRemerciementEmail(
                donneur,
                String.valueOf(reservation.getTypeCollecte()),
                reservation.getCreneau().getDate(),
                reservation.getCreneau().getHeure()
        );
    }

    public List<Don> getDonsAssuresByDonneur(Donneur donneur) {
        return donRepository.findByReservation_Donneur_IdAndStatut(donneur.getId(), "assuré");
    }

}
