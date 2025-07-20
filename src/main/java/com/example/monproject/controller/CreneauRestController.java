package com.example.monproject.controller;

import com.example.monproject.dto.CreneauDto;
import com.example.monproject.repository.CreneauRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CreneauRestController {


    private final CreneauRepository creneauRepository;
    public CreneauRestController(CreneauRepository creneauRepository) {
        this.creneauRepository = creneauRepository;
    }

    @GetMapping("/creneaux/{centreId}/{typeCollecte}")
    public List<CreneauDto> getCreneauxParType(@PathVariable Long centreId, @PathVariable String typeCollecte) {
        int dureeMinutes = switch (typeCollecte.toLowerCase()) {
            case "plasma" -> 90;
            case "plaquettes" -> 120;
            default -> 60;
        };

        return creneauRepository.findByCentreIdAndDisponible(centreId, "libre").stream()
                .filter(c -> c.getDate().isAfter(LocalDate.now()))
                .filter(c -> c.getDuree() == dureeMinutes)
                .map(c -> {
                    CreneauDto dto = new CreneauDto();
                    dto.setId(c.getId());
                    dto.setDate(c.getDate());
                    dto.setHeure(c.getHeure());
                    dto.setDisponible(c.getDisponible());
                    dto.setCentreId(centreId);
                    return dto;
                }).toList();
    }

}
