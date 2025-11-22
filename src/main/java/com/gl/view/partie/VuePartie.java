package com.gl.view.partie;

import com.gl.controller.partie.ControleurPartie;
import com.gl.model.Observateur;

public class VuePartie implements Observateur {
    private ControleurPartie controleur;
    
    public VuePartie(ControleurPartie controleur) {
        this.controleur = controleur;
    }
    @Override
    public void miseAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'miseAJour'");
    }
    
}
