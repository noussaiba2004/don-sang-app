package com.example.monproject.repository;

import com.example.monproject.model.Actualite;
import com.example.monproject.model.Centre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActualiteRepository extends JpaRepository<Actualite, Long> {
    List<Actualite> findByCentre(Centre centre);
}

