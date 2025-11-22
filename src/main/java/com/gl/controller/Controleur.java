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

        Commande cmd = Commande.fromString(input);
        if (cmd != null) {
            handleGlobalCommand(cmd);
            return; 
        }

        // Sinon, c'est une commande LOCALE
        handleLocalInput(input);
    }

    private void handleGlobalCommand(Commande cmd) {
        switch (cmd) {
            case FERMER -> routeur.stop();
            case QUITTER -> routeur.backToRoot();
            case RETOURNER -> routeur.pop();
        }
    }

    public Vue getVue() {
        return vue;
    }
}
