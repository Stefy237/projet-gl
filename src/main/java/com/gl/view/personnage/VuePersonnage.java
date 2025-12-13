package com.gl.view.personnage;

import com.gl.model.Personnage;
import com.gl.view.Vue;
import com.gl.controller.personnage.ControleurPersonnage;
import com.gl.model.Observateur;

public class VuePersonnage implements Observateur,Vue {
    private ControleurPersonnage controleur;

    public VuePersonnage() {

    }

    public void afficherDetails(Personnage p) {
        System.out.println("Nom: " + p.getNom() + " - Profession: " + p.getProfession());
    }

    @Override
    public void miseAJour() {
        System.out.println("[VuePersonnage] Mise à jour de l’affichage.");
    }

    @Override
    public void afficher() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficher'");
    }
}
