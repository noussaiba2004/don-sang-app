package com.example.monproject.service;

import com.example.monproject.model.Creneau;
import com.example.monproject.model.Centre;
import com.example.monproject.repository.CreneauRepository;
import com.example.monproject.repository.CentreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreneauSchedulerService {

    private final CreneauRepository creneauRepository;
    private final CentreRepository centreRepository;

    /**
     * Génère automatiquement les créneaux pour les 7 prochains jours si non existants.
     */
    @Scheduled(cron = "0 50 15 * * *") // chaque jour à 02:00
    public void genererCreneauxGlissants() {
        List<Centre> centres = centreRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Centre centre : centres) {
            for (int d = 0; d < 7; d++) {
                LocalDate date = today.plusDays(d);

                if (creneauRepository.existsByCentreIdAndDate(centre.getId(), date)) {
                    continue; // Skip si déjà généré
                }

                genererPourCentreEtDate(centre, date, 60);   // Don de sang
                genererPourCentreEtDate(centre, date, 90);   // Plasma
                genererPourCentreEtDate(centre, date, 120);  // Plaquettes

                log.info("[Scheduler] Créneaux générés pour le centre {} à la date {}", centre.getNom(), date);
            }
        }
    }

    private void genererPourCentreEtDate(Centre centre, LocalDate date, int dureeMinutes) {
        List<Creneau> creneaux = new ArrayList<>();
        LocalTime heure = LocalTime.of(9, 0);
        LocalTime fin = LocalTime.of(17, 0);

        while (heure.plusMinutes(dureeMinutes).isBefore(fin.plusMinutes(1))) {
            Creneau creneau = new Creneau();
            creneau.setCentre(centre);
            creneau.setDate(date);
            creneau.setHeure(heure);
            creneau.setDuree(dureeMinutes);
            creneau.setDisponible("libre");

            creneaux.add(creneau);
            heure = heure.plusMinutes(dureeMinutes);
        }

        creneauRepository.saveAll(creneaux);
    }

    /**
     * Purge automatique des créneaux expirés sans réservation.
     */
    @Scheduled(cron = "0 50 15 * * *") // chaque jour à 03:00
    public void purgeCreneauxExpiresSansReservation() {
        LocalDate today = LocalDate.now();
        List<Creneau> creneauxADelete = creneauRepository.findByDateBefore(today)
                .stream()
                .filter(c -> c.getReservation() == null)
                .toList();

        creneauRepository.deleteAll(creneauxADelete);
        log.info("[Scheduler] {} créneaux expirés sans réservation supprimés.", creneauxADelete.size());
    }
}
