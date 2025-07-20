package com.example.monproject.repository;

import com.example.monproject.model.Centre;
import com.example.monproject.model.Donneur;
import com.example.monproject.model.Reservation;
import com.example.monproject.model.TypeCollecte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Pour vérifier si un donneur a déjà réservé récemment selon le type de collecte

    List<Reservation> findByCreneau_Centre(Centre centre);

    List<Reservation> findByCreneau_CentreAndCreneau_Date(Centre centre, LocalDate date);

    List<Reservation> findByCreneau_Centre_IdAndStatus(Long centreId, String status);

    List<Reservation> findByDonneurAndStatus(Donneur donneur, String status);

    @Query(
            value = "SELECT EXTRACT(MONTH FROM c.date) AS month, COUNT(r.id) " +
                    "FROM reservation r " +
                    "JOIN creneau c ON c.id = r.creneau_id " +
                    "GROUP BY month " +
                    "ORDER BY month",
            nativeQuery = true
    )
    List<Object[]> countReservationsByMonth();

    Optional<Reservation> findTopByDonneurOrderByCreneau_DateDesc(Donneur donneur);
    int countByDonneurAndTypeCollecteAndCreneau_DateBetween(Donneur donneur, TypeCollecte typeCollecte, LocalDate start, LocalDate end);


}
