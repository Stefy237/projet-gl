package com.gl.view.joueur;

import com.gl.view.Vue;

public class VueInscription extends Vue {

    @Override
    public void afficher() {
        System.out.println("""
                ================================= INSCRIPTION =================================
                Veuillez entrer le nom/pseudo du joueur à enregistrer
                """);
    }
    
}
