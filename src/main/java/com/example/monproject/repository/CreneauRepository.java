package com.example.monproject.repository;

import com.example.monproject.model.Centre;
import com.example.monproject.model.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.util.Arrays;
import java.util.List;

@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Long> {
    // Récupérer les créneaux disponibles pour un centre

    List<Creneau> findByCentreIdAndDisponible(Long centreId, String libre);

    List<Creneau> findByCentreId(Long centreId);

    List<Creneau> findByCentre(Centre centre);

    boolean existsByCentreIdAndDate(Long centreId, LocalDate date);

    List<Creneau> findByDateBefore(LocalDate date);
}

