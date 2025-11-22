package com.gl.view.personnage;

import com.gl.model.Personnage;
import com.gl.controller.personnage.ControleurPersonnage;
import com.gl.model.Observateur;

public class VuePersonnage implements Observateur {
    private ControleurPersonnage controleur;

    public VuePersonnage(ControleurPersonnage c) {
        this.controleur = c;
    }

    public void afficherDetails(Personnage p) {
        System.out.println("Nom: " + p.getNom() + " - Profession: " + p.getProfession());
    }

    @Override
    public void miseAJour() {
        System.out.println("[VuePersonnage] Mise à jour de l’affichage.");
    }
}
