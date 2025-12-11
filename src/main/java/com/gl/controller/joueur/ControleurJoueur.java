package com.gl.controller.joueur;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.partie.ControleurAjouterPartie;
import com.gl.controller.partie.ControleurPartie;
import com.gl.controller.personnage.ControleurAjouterPersonnage;
import com.gl.model.Joueur;
import com.gl.model.Partie;
import com.gl.view.Vue;
import com.gl.view.partie.VueAjouterPartie;
import com.gl.view.personnage.VueAjouterPersonnage;

public class ControleurJoueur extends Controleur {
    private Joueur joueur;
    
    public ControleurJoueur(Routeur routeur, Vue vue, Joueur joueur) {
        super(routeur, vue);
        this.joueur = joueur;
    }

    @Override
    protected void handleLocalInput(String input) {
        switch (input) {
            case "1":
                System.out.println("Entrez l'id de la partie");
                // Partie partie = joueur.
                // routeur.push(new ControleurPartie(null));
                break;
            
            case "2":
                System.out.println("Entrez l'id du personnage");
                
                break;

            case "3":
                
                break;

            case "4":
                routeur.push(new ControleurAjouterPartie(routeur, new VueAjouterPartie()));
                break;

            case "5":
                routeur.push(new ControleurAjouterPersonnage(routeur, new VueAjouterPersonnage()));
                break;
        
            default:
                System.out.println("Veullez entrer une entrée valide");
                processInput();
        }
    }
}
