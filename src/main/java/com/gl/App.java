package com.gl;

import com.gl.controller.joueur.ControleurAccueil;
import com.gl.model.Joueur;
import com.gl.persistence.SQLiteManager;
import com.gl.view.joueur.VueAccueil;

public class App {
    private static Joueur joueurConnecte;
    public static void main(String[] args) {

        // Pour construire et peupler automatiquement les tables via le code java -- Veuillez décommenter cette commande
        // ATTENTION : À NE FAIRE QU'UNE SEULE FOIS
        // SQLiteManager.initialize();
        // SQLiteManager.loadTestData();

        Routeur routeur = Routeur.getInstance();
        routeur.start(new ControleurAccueil(routeur, new VueAccueil()));
    }

    public static Joueur getjoueurConnecte() {
        return joueurConnecte;
    }
    public static void setJoueurConnecte(Joueur joueurConnecte) {
        App.joueurConnecte = joueurConnecte;
    }
}
