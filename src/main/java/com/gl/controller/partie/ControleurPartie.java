package com.gl.controller.partie;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.model.Partie;
import com.gl.model.Univers;
import com.gl.view.Vue;

public class ControleurPartie extends Controleur{
    private Partie partie;

    public ControleurPartie(Routeur routeur, Vue vue) {
        super(routeur, vue);
        //TODO Auto-generated constructor stub
    }

    // public ControleurPartie(Partie partie) {
    //     this.partie = partie;
    // }

    public Partie creerPartie(String titre, Univers univers, String resume) {
        return new Partie(titre, univers, resume);
    }

    public void modifierPartie(Partie partie) {
        
    }

    @Override
    protected void handleLocalInput(String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleLocalInput'");
    }
}
