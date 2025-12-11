package com.gl.controller.joueur;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.ControleurConfirmation;
import com.gl.model.Joueur;
import com.gl.persistence.JoueurDAO;
import com.gl.view.Vue;
import com.gl.view.VueConfirmation;
import com.gl.view.joueur.VueJoueur;

public class ControleurInscription extends Controleur {
    JoueurDAO joueurDAO = new JoueurDAO();

    public ControleurInscription(Routeur routeur, Vue vue) {
        super(routeur, vue);
    }

    @Override
    protected void handleLocalInput(String input) {
        if (input.isEmpty()) {
            System.out.println("Veuillez entrer un nom/pseudo valide");
            processInput();
        }
        if(joueurDAO.findByName(input) != null) {
            System.out.println("Pseudo déja utilisé. Veuillez un choisir un nouveau");
            processInput();
        }

        Runnable handleSave = () -> {
            Joueur joueur = new Joueur(input);
            joueurDAO.save(joueur);
            routeur.pop();
            routeur.push(new ControleurJoueur(routeur, new VueJoueur(), joueur));
        };

        routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes-vous sûre de vouloir enregistrer cet utilisateur ?"), handleSave));
    }
    
}
