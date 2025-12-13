package com.gl.view.personnage;

import com.gl.model.Personnage;
import com.gl.view.Vue;
import com.gl.model.Observateur;

public class VuePersonnage implements Observateur, Vue {
    private Personnage personnage;

    public VuePersonnage(Personnage personnage) {
        this.personnage = personnage;
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

    public Personnage getPersonnage() {
        return personnage;
    }

    public void setPersonnage(Personnage personnage) {
        this.personnage = personnage;
    }
}
