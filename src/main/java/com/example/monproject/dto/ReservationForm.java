package com.example.monproject.dto;

import com.example.monproject.model.TypeCollecte;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class ReservationForm {
    private Long centreId;
    private Long creneauId;
    // ✅ Type unique (Enum)
    private TypeCollecte typeCollecte;
    private String nom;
    private String prenom;
    private String email;
    private String groupeSanguin;
    private String localisation;
    private LocalDate dateNaissance;
    private String sexe;
    private boolean confirme = false;


}

