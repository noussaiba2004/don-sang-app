package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Donneur associé
    @ManyToOne
    @JoinColumn(name = "donneur_id")
    private Donneur donneur;

    // Centre associé
    @ManyToOne
    @JoinColumn(name = "centre_id")
    private Centre centre;

    // Créneau choisi
    @OneToOne(optional = true)
    @JoinColumn(name = "creneau_id")
    private Creneau creneau;

    // ✅ Type de collecte unique (remplace l'ancienne liste)
    @Enumerated(EnumType.STRING)
    @Column(name = "type_collecte", nullable = false)
    private TypeCollecte typeCollecte;

    //Statut
    @Column(name = "status", nullable = false)
    private String status = "EN ATTENTE";

    @Column(nullable = false)
    private boolean confirme = false;

}

