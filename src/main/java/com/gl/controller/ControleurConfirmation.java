package com.gl.controller;

import com.gl.Routeur;
import com.gl.view.Vue;

public class ControleurConfirmation extends Controleur {
    private Runnable handleAccept;
    // private enum commande {ACCEPTER("o"), REFUSER}

    public ControleurConfirmation(Routeur routeur, Vue vue, Runnable handleAccept) {
        super(routeur, vue);
        this.handleAccept = handleAccept;
    }

    @Override
    public void handleLocalInput(String input) {
        if(input.equals("o")) {
            routeur.pop();
            handleAccept.run();
        } else if (input.equals("n")) {
            routeur.pop();
        } else {
            System.out.println("Commande inconnue. Veuillez choisir une commande valide \n > ");
        }
    }    
}