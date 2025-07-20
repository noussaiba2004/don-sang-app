package com.example.monproject.service;

import com.example.monproject.model.Donneur;
import com.example.monproject.repository.DonneurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonneurService {

    private final DonneurRepository donneurRepository;

    public Donneur findByEmail(String email) {
        return donneurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Donneur non trouvé avec l'email : " + email));
    }
}
