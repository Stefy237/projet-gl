package com.gl.view.joueur;

import com.gl.model.Joueur;
import com.gl.model.Observateur;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.persistence.PartieDAO;
import com.gl.view.Vue;

public class VueJoueur implements Observateur, Vue {
    private Joueur joueur;
    private PartieDAO partieDAO = new PartieDAO();

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

        System.out.println(">>>>>>>>>>> Liste des parties dans lequel vous avez un personnage");
        System.out.println("Partie | Partie id | Personnage | Personnage id");

        for(Personnage personnage : joueur.getPersonnages()) {
            Partie partie = partieDAO.findById(personnage.getPartieId());
            partie.attache(this);
            System.out.println(partie.getTitre() + " | " + partie.getId() + " | " + personnage.getNom() + " | " + personnage.getId());
        }

        if(!joueur.getPartieMJ().isEmpty()) {
            System.out.println(">>>>>>>>>>> Liste des parties dans lequel vous êtes meneur de jeu");
            System.out.println("Partie | Partie id");

            for(Partie partie : joueur.getPartieMJ()) {
                System.out.println(partie.getTitre() + " | " + partie.getId());
            }
        }

        System.out.println(">>>>>>>>>>> Liste des parties des propositions de partie");

        System.out.println("""
                Entrez : 
                1 - Pour afficher une partie en particulier
                2 - Pour afficher un personnage en particulier
                3 - Pour creer une partie
                4 - Pour creer un personnage
                """);
    }
    
}
