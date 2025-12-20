package com.gl.view.personnage;

import com.gl.model.Personnage;
import com.gl.persistence.JoueurDAO;
import com.gl.persistence.PartieDAO;
import com.gl.view.Vue;
import com.gl.App;
import com.gl.model.Observateur;
import com.gl.model.Partie;

public class VuePersonnage extends Vue implements Observateur {
    private Personnage personnage;
    private JoueurDAO joueurDAO = new JoueurDAO();
    private PartieDAO partieDAO = new PartieDAO();

    public VuePersonnage(Personnage personnage) {
        this.personnage = personnage;
        personnage.attache(this);
    }

    @Override
    public void miseAJour() {
        System.out.println("[VuePersonnage] Mise à jour de l’affichage ... ");
    }

    @Override
    public void afficher() {
        Partie partie = partieDAO.findById(personnage.getPartieId());
        partie.attache(this);

        System.out.println("========================== DETAILS DU PERSONNAGE " + personnage.getNom() + " =================================");
        System.out.println("\n >>>>>>>>>>>> MENEUR DE JEU : " + joueurDAO.findById(personnage.getRelatedMjId()).getPseudo());

        System.out.println("\n >>>>>>>>>>>>> PARTIE ASSOCIEE : " + partie.getTitre());
        
        System.out.println("\n");

        String texte;
        if(App.getjoueurConnecte().getId() == personnage.getRelatedJoueurId()) {
            texte = """ 
                    Entrez (en respectant le format le cas échéant): 

                    - Pour transférer ce personnage à un autre joueur
                        t,id du joueur à qui l'on veut transférer le personnage

                    - Pour changer de le meneur de jeu de ce joueur
                        c,id du meneur de jeu

                    - Pour afficher la biographie publique du personnage
                        bio_pub

                    - Pour affihcer la biographie intégrale (publique + privée) du personnage
                        bio

                    - Pour accéder à la page de la partie relative à ce personnage
                        go_partie
                    """;
        } else if(App.getjoueurConnecte().getId() == personnage.getRelatedMjId()) {
            texte = """
                    Entrez (en respectant le format le cas échéant): 

                    - Pour afficher la biographie publique du personnage
                        bio_pub

                    - Pour affihcer la biographie intégrale (publique + privée) du personnage
                        bio

                    - Pour accéder à la page de la partie relative à ce personnage
                        go_partie
                    """;

        } else {
            texte = """
                    Entrez (en respectant le format le cas échéant): 

                    - Pour afficher la biographie du personnage
                        bio_pub

                    - Pour accéder à la page de la partie relative à ce personnage
                        go_partie
                    """;
        }

        System.out.println(texte);
    }
}
