package com.gl.view.joueur;

import com.gl.view.Vue;

public class VueAccueil implements Vue {

    String texteAccueil = """
            ==================== Bienvenue sur notre plateforme de roliste =======================
            ---------------------------------------------------------------------------------------
            COMMANDES GLOBABLES : Ces commandes fonctionne tout le long du programme
                - x : pour fermer l'application
                - q : pour se deconnecter l'application -> revenir sur la page d'accueil
                - r : pour revenir à la page précedente
                - o : pour confirmer votre choix
                - n : pour rejeter votre choix
            ---------------------------------------------------------------------------------------
            - Pour vous connecter taper 1
            - Pour inscrire un nouveau joueur taper 2
            """;

    @Override
    public void afficher() {
        System.out.println(texteAccueil);
    }
    
}
