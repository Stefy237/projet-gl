package com.gl.view.partie;

import com.gl.App;
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
        // Logique de mise à jour 
    }

    @Override
    public void afficher() {
        String texte;

        System.out.println("======================= BIENVENUE sur la partie: " + partie.getTitre() + " ========================");

        System.out.println("Situation initiale de la partie: \n " +  partie.getSituationInitiale());

        System.out.println(">>>>>>>>> Participants de la partie");
        System.out.println(" Personnage | Personnage id");

        for(Personnage personnage : partie.getPersonnages()) {
            System.out.println(  personnage.getNom() + " | " + personnage.getId());
        }

        System.out.println(">>>>>>>>> Personnages en attente de validation");
        System.out.println(" Personnage | Personnage id");

        if(partie.getMjId() == App.getjoueurConnecte().getId()) {
            texte = """
                    Entrez : 
                    - Pour modifier la partie
                        1
                    - Pour accéder à un personnage
                        2,id du personnage
                    - Pour marquer la partie comme terminée
                        3,resumé de la partie
                    - Pour accepter un personnage
                        4,id du personnage
                    - Pour supprimer un personnage
                        5,id du personnage
                    """;
        } else {
            texte = "Entrer r pour revenir au menu précédent";
        }

        System.out.println(texte);
    }
    
}
