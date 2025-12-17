package com.gl.view.partie;

import com.gl.model.Observateur;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.view.Vue;

public class VueModifierPartie implements Vue,Observateur {

    private Partie partie;

    @Override
    public void afficher() {
        System.out.println("======================= BIENVENUE sur la partie: " + partie.getTitre() + " ========================");

        System.out.println(" Personnage | Personnage id");

        for(Personnage personnage : partie.getPersonnages()) {
            System.out.println(  personnage.getNom() + " | " + personnage.getId());
        }

        System.out.println("""
                Entrez : 
                1 - Pour Supprimer la partie
                3 - Pour terminer la partie
                5 - Pour supprimer un personnage
                6 - Pour valider un personnage
                """);

    }

    @Override
    public void miseAJour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'miseAJour'");
    }
    
}
