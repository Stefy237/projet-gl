package com.gl.model;

public class Personnage extends Sujet {
    private int id; // pour les requêtes SQL

    private String nom;
    private String profession;
    private String naissanceDate;
    private String image;

    private Univers univers;

    private int biographieId;
    private int partieId;
    private int relatedJoueurId;
    private int relatedMjId;

    public Personnage(String nom, String profession, String naissanceDate, Univers univers, int biographieId, int relatedJoueurId) {
        this.nom = nom;
        this.profession = profession;
        this.naissanceDate = naissanceDate;
        this.univers = univers;
        this.biographieId = biographieId;
        this.relatedJoueurId = relatedJoueurId;
    }

    // Getters / Setters
    public String getNom() { 
        return nom; 
    }

    public void setProfession(String profession) {
        this.profession = profession;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNaissanceDate(String naissanceDate) {
        this.naissanceDate = naissanceDate;
    }

    public void setUnivers(Univers univers) {
        this.univers = univers;
    }

    public int getBiographieId() {
        return biographieId;
    }

    public void setBiographieId(int biographieId) {
        this.biographieId = biographieId;
    }

    public int getRelatedJoueurId() {
        return relatedJoueurId;
    }

    public void setRelatedJoueurId(int relatedJoueurId) {
        this.relatedJoueurId = relatedJoueurId;
    }

    public int getRelatedMjId() {
        return relatedMjId;
    }

    public void setRelatedMjId(int relatedMjId) {
        this.relatedMjId = relatedMjId;
    }

    public int getPartieId() {
        return partieId;
    }

    public void setPartieId(int partieId) {
        this.partieId = partieId;
    }
}
