package com.gl.view.joueur;

import com.gl.view.Vue;

public class VueAccueil extends Vue {

    String texteAccueil = """
             ==================== Bienvenue sur notre plateforme de roliste ======================= 
            ---------------------------------------------------------------------------------------
            COMMANDES GLOBABLES : Ces commandes fonctionne tout le long du programme
                - x : pour fermer l'application
                - q : pour se deconnecter l'application -> revenir sur la page d'accueil
                - h : pour revenir à la page du joueur
                - r : pour revenir à la page précedente
            ---------------------------------------------------------------------------------------
            - Pour vous connecter taper 1
            - Pour inscrire un nouveau joueur taper 2
            """;

    @Override
    public void afficher() {
        System.out.println("\n" + texteAccueil + "\n");
    }
    
}
