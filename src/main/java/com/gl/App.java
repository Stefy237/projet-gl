package com.gl;

import com.gl.controller.joueur.ControleurAccueil;
import com.gl.persistence.SQLiteManager;
import com.gl.view.joueur.VueAccueil;

public class App {
    public static void main(String[] args) {
        // SQLiteManager.initialize();

        // Pour peupler automatiquement les tables via le code java -- Veuillez décommenter cette commande
        // ATTENTION : À NE FAIRE QU'UNE SEULE FOIS
        // SQLiteManager.loadTestData();

        Routeur routeur = Routeur.getInstance();
        routeur.start(new ControleurAccueil(routeur, new VueAccueil()));
    }
}
