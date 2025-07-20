package com.example.monproject.dto;

public class CentreMapDTO {
    private Long id;
    private String nom;
    private String adresse;
    private Double latitude;
    private Double longitude;
    private String etat;

    // Constructeurs
    public CentreMapDTO() {}

    public CentreMapDTO(Long id, String nom, String adresse, Double latitude, Double longitude, String etat) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.etat = etat;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}
