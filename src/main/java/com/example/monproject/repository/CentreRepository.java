package com.example.monproject.repository;

import com.example.monproject.model.Centre;
import com.example.monproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CentreRepository extends JpaRepository<Centre, Long> {
    // Optionnel : récupérer les centres par ville
    List<Centre> findTop3ByVilleContainingOrCodePostalContaining(String ville, String codePostal);

    Optional<Centre> findByEmail(String email);

    Optional<Centre> findByUser(User user);
}

