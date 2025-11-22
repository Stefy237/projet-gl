package com.gl.model;

import com.gl.persistence.ParagrapheDAO;
import com.gl.persistence.PersonnageDAO;

public class Personnage {
    private String nom;
    private String profession;
    private String naissanceDate;
    private String image;

    private Univers univers;
    private Biographie biographie;


    public Personnage(String nom, String profession, String naissanceDate, Univers univers, Biographie biographie) {
        this.nom = nom;
        this.profession = profession;
        this.naissanceDate = naissanceDate;
        this.univers = univers;
        this.biographie = biographie;
    }

    public void setProfession(String newProfession) {
        this.profession = newProfession;
    }

    public void supprimer() {
        // Logique de suppression
    }

    // Getters / Setters
    public String getNom() { 
        return nom; 
    }

    public String getProfession() { 
        return profession; 
    }
    public String getNaissanceDate() { 
        return naissanceDate; 
    }
    public Univers getUnivers() { 
        return univers; 
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Biographie getBiographie() {
        return biographie;
    }

    public void setBiographie(Biographie biographie) {
        this.biographie = biographie;
    }
}
