package com.gl.controller.joueur;

import com.gl.App;
import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.model.Joueur;
import com.gl.persistence.JoueurDAO;
import com.gl.view.Vue;
import com.gl.view.joueur.VueJoueur;

public class ControleurConnexion extends Controleur {
    JoueurDAO joueurDAO = new JoueurDAO();

    public ControleurConnexion(Routeur routeur, Vue vue) {
        super(routeur, vue);
    }

    @Override
    protected void handleLocalInput(String input) {
        Joueur joueur = joueurDAO.findByName(input.trim());
        if(joueur == null) {
            System.out.println("Ce joueur n'existe pas. Veuillez entrer un nom/pseudo valide");
            processInput();
        }

        App.setJoueurConnecte(joueur);
        routeur.push(new ControleurJoueur(routeur, new VueJoueur(joueur), joueur));
    }
    
}
