package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Creneau {

    @Getter
    @Setter
    @Id
    @GeneratedValue
    private Long id;
    @Getter
    @Setter
    private LocalDate date;
    @Getter
    @Setter
    private LocalTime heure;
    @Getter
    @Setter
    private int duree; // en minutes
    @Getter
    @Setter
    private String disponible;
    @Getter
    @Setter
    @ManyToOne
    private Centre centre;
    @Getter
    @Setter
    @OneToOne(mappedBy = "creneau", orphanRemoval = true)
    private Reservation reservation;

}

