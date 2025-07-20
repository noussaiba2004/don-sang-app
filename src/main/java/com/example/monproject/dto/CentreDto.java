package com.example.monproject.dto;

import com.example.monproject.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CentreDto {

    private Long id;
    private String nom;
    private String adresse;
    private String ville;
    private String codePostal;
    private String etat;
    private Double latitude;
    private Double longitude;
    private String horaire;
    private String typeCollecte;

    private String email; // utilisé comme identifiant de connexion
    private String password;
    private Long userId;

    // Optionnel : liste des IDs des créneaux associés
    private List<Long> creneauIds;

}
