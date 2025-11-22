package com.gl.model;

import java.util.ArrayList;
import java.util.List;

import com.gl.persistence.PartieDAO;

public class Partie extends Sujet {
    String titre;
    Univers univers;
    String resume;
    private boolean dejaJouee;
    private boolean validee;
    private List<Personnage> personnages = new ArrayList<>();


    public Partie(String titre, Univers univers, String resume) {
        this.titre = titre;
        this.univers = univers;
        this.resume = resume;
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
}
