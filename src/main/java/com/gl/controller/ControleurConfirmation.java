package com.gl.controller;

import com.gl.Routeur;
import com.gl.view.Vue;

public class ControleurConfirmation extends Controleur {
    private Runnable handleAccept;
    private boolean confirmed = false;

    public ControleurConfirmation(Routeur routeur, Vue vue, Runnable handleAccept) {
        super(routeur, vue);
        this.handleAccept = handleAccept;
    }

    @Override
    public void handleLocalInput(String input) {
        Commande cmd = Commande.fromString(input);

        if(cmd == Commande.ACCEPTER) {
            confirmed = true;
            routeur.pop();
            handleAccept.run();
            return;
        } else if (cmd == Commande.REFUSER) {
            confirmed = true;
            routeur.pop();
            return;
        }
        
        // Si la réponse n'est pas valide, ne pas pop et laisser le routeur redemander
        System.out.println("Veuillez répondre par 'o' (oui) ou 'n' (non)");
        // Ne pas appeler processInput() ici, laisser la boucle du routeur le faire
    }    
}