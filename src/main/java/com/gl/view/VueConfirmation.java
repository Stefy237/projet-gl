package com.gl.view;

import com.gl.controller.Commande;

public class VueConfirmation implements Vue {
    private String texteConfirmation;

    public VueConfirmation(String text) {
        this.texteConfirmation = text;
    }

    @Override
    public void afficher() {
        System.out.printf("""
            %s
                - %s : pour confirmer
                - %s : pour annuler
            """, texteConfirmation, Commande.ACCEPTER.getCmd(), Commande.REFUSER.getCmd()
        );
    }
    
}
