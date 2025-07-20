package com.example.monproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String email;
    @Setter
    @Getter
    private String password;
    @Setter
    @Getter
    private String role; // ex: "DONNEUR", "ADMIN", "CENTRE"
    @Setter
    @Getter
    // Lien optionnel avec un centre
    @OneToOne
    @JoinColumn(name = "centre_id")
    private Centre centre;

    @OneToOne
    @JoinColumn(name = "donneur_id")
    @Getter @Setter
    private Donneur donneur;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

}

