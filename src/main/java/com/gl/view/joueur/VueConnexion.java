package com.gl.view.joueur;

import com.gl.view.Vue;

public class VueConnexion extends Vue {
    
    @Override
    public void afficher() {
        System.out.println("""
                =============================== CONNEXION ===================================
                Veuillez entrer votre nom/pseudo
        """);
    }
}
