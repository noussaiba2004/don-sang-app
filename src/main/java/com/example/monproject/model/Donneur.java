package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Entity
public class Donneur {
    @Id
    @GeneratedValue
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String email;
    private String groupeSanguin;
    private String localisation;
    @OneToMany(mappedBy = "donneur")
    private List<Reservation> historiqueDons;
    @OneToOne(mappedBy = "donneur")
    private User user;

}

