package com.gl.controller.joueur;

import com.gl.controller.Controleur;
import com.gl.controller.Routeur;
import com.gl.view.Vue;
import com.gl.view.joueur.VueConnexion;
import com.gl.view.joueur.VueInscription;

public class ControleurAccueil extends Controleur {

    public ControleurAccueil(Routeur routeur, Vue vue) {
        super(routeur, vue);
    }

    @Override
    protected void handleLocalInput(String input) {
        switch (input) {
            case "1" : 
            routeur.push(new ControleurConnexion(routeur, new VueConnexion()));
            break;
            case "2" : 
            routeur.push(new ControleurInscription(routeur, new VueInscription()));
            break;
            default : 
                System.out.println("Commande inconnue. Veuillez choisir une commande valide \n >");
                break;
        }
    }
    
}
