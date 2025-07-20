package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Centre {
    @Id
    @GeneratedValue
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


    @OneToMany(mappedBy = "centre")
    private List<Creneau> creneaux;

    private String email;
    private String password;
    @OneToOne(mappedBy = "centre", cascade = CascadeType.ALL)
    private User user;


}

