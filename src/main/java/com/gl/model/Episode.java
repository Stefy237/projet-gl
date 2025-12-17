package com.gl.model;

import java.util.ArrayList;
import java.util.List;


public class Episode extends Sujet {
    private int id;
    private int relatedBiographieId;
    private int relatedAventureId;
    private String titre;
    private boolean joueurValide;
    private boolean mjValide;
    private String dateCreation;
    private List<Paragraphe> paragraphes = new ArrayList<>();

    public Episode(String titre) {
        this.titre = titre;
        joueurValide = false;
        mjValide = false;
    }

    public void ajouterParagraphe(Paragraphe p) {
        paragraphes.add(p);
        informe();
    }

    public boolean isValider() {
        if (joueurValide && mjValide) return joueurValide == mjValide;

        return false;
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

    public List<Paragraphe> getParagraphes() {
        return paragraphes;
    }

    public void setParagraphes(List<Paragraphe> paragraphes) {
        this.paragraphes = paragraphes;
    }

    public boolean isJoueurValide() {
        return joueurValide;
    }

    public void joueurValide() {
        this.joueurValide = true;
    }

    public boolean isMjValide() {
        return mjValide;
    }

    public void mjValide() {
        this.mjValide = true;
    }

    public int getRelatedBiographieId() {
        return relatedBiographieId;
    }

    public void setRelatedBiographieId(int relatedBiographieId) {
        this.relatedBiographieId = relatedBiographieId;
    }

    public int getRelatedAventureId() {
        return relatedAventureId;
    }

    public void setRelatedAventureId(int relatedAventureId) {
        this.relatedAventureId = relatedAventureId;
    }

    public void setJoueurValide(boolean joueurValide) {
        this.joueurValide = joueurValide;
    }

    public void setMjValide(boolean mjValide) {
        this.mjValide = mjValide;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
}
