package com.gl.view.partie;

import com.gl.controller.partie.ControleurPartie;
import com.gl.model.Observateur;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.view.Vue;

public class VuePartie implements Observateur,Vue {
    private ControleurPartie controleur;
    private Partie partie;
    
    public VuePartie(Partie partie) {
        this.partie = partie;
        this.partie.attache(this);
    }
    @Override
    public void miseAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'miseAJour'");
        
    }
    @Override
    public void afficher() {
        System.out.println("======================= BIENVENUE sur la partie: " + partie.getTitre() + " ========================");

        System.out.println("Resume de la partie: " + partie.getResume());


        System.out.println(" Personnage | Personnage id");

        for(Personnage personnage : partie.getPersonnages()) {
            System.out.println(  personnage.getNom() + " | " + personnage.getId());
        }

        System.out.println("""
                Entrez : 
                1 - Pour modifier la partie
                2 - Pour afficher un personnage en particulier
                """);
    }
    
}
