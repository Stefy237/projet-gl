package com.gl.model;

import java.util.ArrayList;
import java.util.List;

public class Joueur extends Sujet {
    private int id;

    private String pseudo;
    private List<Personnage> personnages = new ArrayList<>();
    private List<Partie> partieMJ = new ArrayList<>();  // Les parties dans lesquels il est MJ

    public Joueur(String pseudo) {
        this.pseudo = pseudo;
    }

    public Personnage creerPersonnage(String nom, String naissanceDate, String profession, Univers univers,  Biographie bioInitiale) {
        Personnage p = new Personnage(nom, naissanceDate, profession, univers, bioInitiale.getId(), this.id);
        informe(); 
        return p;
    }

    public Partie proposerPartie(String titre, Univers univers, String resume) {
        Partie partie = new Partie(titre, univers, resume, this.id);
        informe();
        return partie;
    }

    public String getPseudo() { 
        return pseudo; 
    }

    public void validerEpisode(Episode episode) {
        //verifier si le joueur courant est propriétaire du joueur dont c'est l'épisode ou le MJ; appliquer la méthode correspondante
        // if(episode.getRelatedBiographie().ge)
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Personnage> getPersonnages() {
        return personnages;
    }

    public void setPersonnages(List<Personnage> personnages) {
        this.personnages = personnages;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public List<Partie> getPartieMJ() {
        return partieMJ;
    }

    public void setPartieMJ(List<Partie> partieMJ) {
        this.partieMJ = partieMJ;
    }
}
