package com.gl.controller.partie;

import com.gl.model.Partie;
import com.gl.model.Univers;

public class ControleurPartie {
    private Partie partie;

    public ControleurPartie(Partie partie) {
        this.partie = partie;
    }

    public Partie creerPartie(String titre, Univers univers, String resume) {
        return new Partie(titre, univers, resume);
    }

    public void modifierPartie(Partie partie) {
        
    }
}
