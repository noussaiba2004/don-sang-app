package com.example.monproject.repository;

import com.example.monproject.model.Centre;
import com.example.monproject.model.Don;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DonRepository extends JpaRepository<Don, Long> {
    List<Don> findByReservation_Creneau_CentreAndStatut(Centre centre, String statut);
    List<Don> findByReservation_Donneur_IdAndStatut(Long donneurId, String statut);

}

