package com.gl.model;

public class Joueur extends Sujet {
    private String pseudo;

    public Joueur(String pseudo) {
        this.pseudo = pseudo;
    }

    public Personnage creerPersonnage(String nom, String naissanceDate, String profession, Univers univers,  Biographie bioInitiale) {
        Personnage p = new Personnage(nom, naissanceDate, profession, univers, bioInitiale);
        informe(); 
        return p;
    }

    public Partie proposerPartie(String titre, Univers univers, String resume) {
        Partie partie = new Partie(titre, univers, resume);
        informe();
        return partie;
    }

    public String getPseudo() { 
        return pseudo; 
    }

    /**
     * Uniquement pour les joueur qui ont le rôle de meneur de jeu dans une partie 
     * @param p : Partie
     */
    public void accepterPartie(Partie p) {
        // logique d’acceptation
    }

    /**
     * Uniquement Uniquement pour les joueur qui ont le rôle de meneur de jeu pour des personnages
     * @param personnage
     */
    public void accepterPersonnage(Personnage personnage) {
        // logique d’acceptation
    }

    public void validerEpisode(Episode e) {
    }
}
