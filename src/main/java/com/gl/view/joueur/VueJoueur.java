package com.gl.view.joueur;

import com.gl.model.Joueur;
import com.gl.model.Observateur;
import com.gl.model.Personnage;
import com.gl.view.Vue;

public class VueJoueur implements Observateur, Vue {
    private Joueur joueur;

    public VueJoueur() {
    }

    public VueJoueur(Joueur joueur) {
        this.joueur = joueur;
        this.joueur.attache(this);  
    }

    @Override
    public void miseAJour() {
        
    }

    @Override
    public void afficher() {
        System.out.println("======================= BIENVENUE " + joueur.getPseudo() + " ========================");
        System.out.println("Partie | Partie id | Personnage | Personnage id");

        for(Personnage personnage : joueur.getPersonnages()) {
            System.out.println(personnage.getPartie().getTitre() + " | " + personnage.getPartie().getId() + " | " + personnage.getNom() + " | " + personnage.getId());
        }

        System.out.println("""
                Entrez : 
                1 - Pour afficher une partie en particulier
                2 - Pour afficher un personnage en particulier
                3 - Pour afficher les notifications
                4 - Pour creer une partie
                5 - Pour creer un personnage
                """);
    }

    public Joueur getJoueur() {
        return joueur;
    }

    // public void setJoueur(Joueur joueur) {
    //     this.joueur = joueur;
    // }
    
}
