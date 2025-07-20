package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Don {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateCreation;
    private String Type;
    private String statut; // "en_attente", "assuré", "annulé"
    @ManyToOne
    private Centre centre;
    @ManyToOne
    private Donneur donneur;
    @ManyToOne
    private Reservation reservation;

}

