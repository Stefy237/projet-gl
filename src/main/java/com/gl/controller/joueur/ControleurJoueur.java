package com.gl.controller.joueur;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.model.Joueur;
import com.gl.view.Vue;

public class ControleurJoueur extends Controleur {
    private Joueur joueur;
    
    public ControleurJoueur(Routeur routeur, Vue vue, Joueur joueur) {
        super(routeur, vue);
        this.joueur = joueur;
    }

    @Override
    protected void handleLocalInput(String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleLocalInput'");
    }
}
