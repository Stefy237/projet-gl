package com.gl.controller;

import com.gl.Routeur;
import com.gl.view.Vue;

public class ControleurConfirmation extends Controleur {
    private Runnable handleAccept;

    public ControleurConfirmation(Routeur routeur, Vue vue, Runnable handleAccept) {
        super(routeur, vue);
        this.handleAccept = handleAccept;
    }

    @Override
    public void handleLocalInput(String input) {
        Commande cmd =  Commande.fromString(input);

        if(cmd == Commande.ACCEPTER) {
            routeur.pop();
            handleAccept.run();
        } else if (cmd == Commande.REFUSER) {
            routeur.pop();
        }
    }    
}
