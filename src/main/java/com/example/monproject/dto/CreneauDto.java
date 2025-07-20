package com.example.monproject.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CreneauDto {

    private Long id;
    private LocalDate date;
    private LocalTime heure;
    private int duree;
    private String disponible;
    private Long centreId;

    // Optionnel : ajouter reservationId si vous voulez afficher l'état de réservation
    private Long reservationId;


}



