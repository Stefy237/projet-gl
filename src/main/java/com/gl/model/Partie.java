package com.gl.model;

import java.util.ArrayList;
import java.util.List;

public class Partie extends Sujet {
    private int id;
    private String titre;
    private Univers univers;
    private String resume;
    private String situationInitiale;
    private String date;
    private String lieu;
    private int mjId;
    private boolean dejaJouee;
    private boolean validee;
    private List<Personnage> personnages = new ArrayList<>();

    public Partie(){}
    
    public Partie(String titre, Univers univers, String situationInitiale, int mjId) { 
        this.titre = titre;
        this.univers = univers;
        this.situationInitiale = situationInitiale;
        this.mjId = mjId;
        this.dejaJouee = false;
        this.validee = false;
    }

    public void valider() {
        this.validee = true;
        informe();
    }

    public void ajouterPersonnage(Personnage p) {
        personnages.add(p);
        informe();
    }

    public void supprimerPersonnage(Personnage p) {
        personnages.remove(p);
        informe();
    }

    public void jouer() {
        this.dejaJouee = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Univers getUnivers() {
        return univers;
    }

    public void setUnivers(Univers univers) {
        this.univers = univers;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public boolean isDejaJouee() {
        return dejaJouee;
    }

    public void setDejaJouee(boolean dejaJouee) {
        this.dejaJouee = dejaJouee;
    }

    public boolean isValidee() {
        return validee;
    }

    public void setValidee(boolean validee) {
        this.validee = validee;
    }

    public List<Personnage> getPersonnages() {
        return personnages;
    }

    public void setPersonnages(List<Personnage> personnages) {
        this.personnages = personnages;
    }

    public String getSituationInitiale() {
        return situationInitiale;
    }

    public void setSituationInitiale(String situationInitiale) {
        this.situationInitiale = situationInitiale;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getMjId() {
        return mjId;
    }

    public void setMjId(int mjId) {
        this.mjId = mjId;
    }
}
