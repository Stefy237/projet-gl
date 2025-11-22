package com.gl.controller.personnage;

import com.gl.model.Joueur;
import com.gl.model.Personnage;
import com.gl.model.Univers;
import com.gl.model.Biographie;

public class ControleurPersonnage {
    private Joueur joueur;

    public ControleurPersonnage(Joueur joueur) {
        this.joueur = joueur;
    }

    public Personnage creerPersonnage(String nom, String naissance, String profession, Univers univers, Biographie bio) {
        return joueur.creerPersonnage(nom, naissance, profession, univers, bio);
    }

    public void modifierPersonnage(Personnage p) {
        
    }
}
