package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Actualite {

    @Id
    @GeneratedValue
    private Long id;

    private String titre;

    @Column(length = 5000)
    private String contenu;

    private LocalDateTime datePublication;

    @ManyToOne
    @JoinColumn(name = "centre_id")
    private Centre centre;
}

