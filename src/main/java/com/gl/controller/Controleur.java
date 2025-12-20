package com.gl.controller;

import java.util.Scanner;

import com.gl.Routeur;
import com.gl.view.Vue;

public abstract class Controleur {
    protected Routeur routeur;
    protected Scanner scanner;
    private Vue vue;

    public Controleur(Routeur routeur, Vue vue) {
        this.routeur = routeur;
        this.scanner = new Scanner(System.in);
        this.vue = vue;
    }

    protected abstract void handleLocalInput(String input);

    // Affichage chevron
    public void processInput() {
        System.out.print("> ");
        String input = scanner.nextLine();

        getVue().setBesoinAffichage(false);

        Commande cmd = Commande.fromString(input);
        if (cmd != null) {
            handleGlobalCommand(cmd);
            return; 
        }

        handleLocalInput(input);
    }

    private void handleGlobalCommand(Commande cmd) {
        switch (cmd) {
            case FERMER -> routeur.stop();
            case QUITTER -> routeur.backToRoot();
            case RETOURNER -> routeur.pop();
            case HOME -> routeur.backToHome();
        }
    }

    public Vue getVue() {
        return vue;
    }
}
