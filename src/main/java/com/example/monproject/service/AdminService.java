package com.example.monproject.service;

import com.example.monproject.repository.DonneurRepository;
import com.example.monproject.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class AdminService {

    private final DonneurRepository donneurRepository;
    private final ReservationRepository reservationRepository;

    public AdminService(DonneurRepository donneurRepository, ReservationRepository reservationRepository) {
        this.donneurRepository = donneurRepository;
        this.reservationRepository = reservationRepository;
    }

    public long getTotalDonneurs() {
        return donneurRepository.count();
    }
    public List<Integer> getGroupesSanguins() {
        List<String> groupes = Arrays.asList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        List<Integer> result = new ArrayList<>();
        for (String groupe : groupes) {
            result.add((int) donneurRepository.countByGroupeSanguin(groupe));
        }
        return result;
    }
    public List<Integer> getReservationsParMois() {
        // Initialise une liste avec 12 mois à zéro
        List<Integer> mois = new ArrayList<>(Collections.nCopies(12, 0));

        for (Object[] row : reservationRepository.countReservationsByMonth()) {
            int moisIndex = ((Number) row[0]).intValue(); // 1 = Janvier
            Long count = (Long) row[1];
            mois.set(moisIndex - 1, count.intValue());
        }

        return mois.subList(0, 6); // Affiche Jan à Juin
    }

    public List<Integer> getRepartitionParSexe() {
        int hommes = (int) donneurRepository.countBySexe("Homme");
        int femmes = (int) donneurRepository.countBySexe("Femme");
        return Arrays.asList(hommes, femmes);
    }

    public List<Integer> getRepartitionParAge() {
        int age18_25 = donneurRepository.countByAgeBetween(18, 25);
        int age26_35 = donneurRepository.countByAgeBetween(26, 35);
        int age36_45 = donneurRepository.countByAgeBetween(36, 45);
        int age46_55 = donneurRepository.countByAgeBetween(46, 55);
        int age56Plus = donneurRepository.countByAgeGreaterThanEqual(56);

        return Arrays.asList(age18_25, age26_35, age36_45, age46_55, age56Plus);
    }

}




