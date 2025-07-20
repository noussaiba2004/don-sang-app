package com.example.monproject.repository;

import com.example.monproject.model.BesoinSang;
import com.example.monproject.model.Centre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BesoinSangRepository extends JpaRepository<BesoinSang, Long> {
    List<BesoinSang> findByCentre(Centre centre);
}
