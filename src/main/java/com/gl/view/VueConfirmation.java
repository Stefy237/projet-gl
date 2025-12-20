package com.gl.view;

import com.gl.controller.Commande;

public class VueConfirmation extends Vue {
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
            """, texteConfirmation, "o", "n"
        );
    }
    
}
