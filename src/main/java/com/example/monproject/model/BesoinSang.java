package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BesoinSang {

    @Id
    @GeneratedValue
    private Long id;

    private String groupeSanguin;

    private int quantite;
    private boolean urgence;

    @ManyToOne
    @JoinColumn(name = "centre_id")
    private Centre centre;
}

