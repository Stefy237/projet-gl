package com.gl;

import java.util.ArrayDeque;
import java.util.Deque;

import com.gl.controller.Controleur;

public class Routeur {
    private static Routeur instance = new Routeur();

    private final Deque<Controleur> historique = new ArrayDeque<>();
    private boolean isRunning = true;

    private Routeur() {}

    public static Routeur getInstance() {
        return instance;
    }

    public void start(Controleur controleurInitial) {
        historique.push(controleurInitial);

        while (isRunning && !historique.isEmpty()) {
            Controleur courant = historique.peek();
            
            courant.getVue().afficher();
            
            // Gestion de l'input utilisateur
            courant.processInput();
        }
        System.out.println("Fermeture de l'application. \n Au revoir");
    }

    // --- Transitions ---

    // Passer à la vue suivante -> ajouter le controleur dédié à la pile
    public void push(Controleur controller) {
        this.historique.push(controller);
    }

    // Aller à la vue précédente -> l'utilisateur appuie sur "r"
    public void pop() {
        if (historique.size() > 1) {
            this.historique.pop();
        }
    }
    
    // Arreter l'application -> l'utilisateur appuie sur "x"
    public void stop() {
        this.isRunning = false;
    }
    
    // Retourner à l'accueil -> l'utilisateur appuie sur "q"
    public void backToRoot() {
        while (historique.size() > 1) {
            historique.pop();
        }
    }
}
