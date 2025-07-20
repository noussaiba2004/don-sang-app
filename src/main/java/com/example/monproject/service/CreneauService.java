package com.example.monproject.service;

import com.example.monproject.model.Creneau;
import com.example.monproject.repository.CreneauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreneauService {

    private final CreneauRepository creneauRepository;

    /**
     * Retourne tous les créneaux disponibles (si nécessaire pour l'administration).
     */
    public List<Creneau> getTousLesCreneaux() {
        return creneauRepository.findAll();
    }

    /**
     * Retourne les créneaux d'un centre spécifique.
     *
     * @param centreId identifiant du centre
     * @return liste des créneaux
     */
    public List<Creneau> getCreneauxByCentreId(Long centreId) {
        return creneauRepository.findByCentreId(centreId);
    }

    /**
     * Retourne les créneaux disponibles d'un centre spécifique avec filtre sur disponibilité et date >= aujourd'hui si nécessaire.
     * Peut être adaptée selon les besoins d'affichage.
     */
    public List<Creneau> getCreneauxDisponiblesByCentre(Long centreId) {
        return creneauRepository.findByCentreIdAndDisponible(centreId, "libre");
    }
}
