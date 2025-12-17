package com.gl.controller;

public enum Commande {
    FERMER("x"), 
    QUITTER("q"), 
    HOME("h"), 
    RETOURNER("r"), 
    ACCEPTER("o"), 
    REFUSER("n");

    private String cmd;

    Commande(String cmd) {
        this.cmd = cmd;
    } 

    public static Commande fromString(String text) {
        for (Commande c : values()) {
            if (c.cmd.equalsIgnoreCase(text.trim())) return c;
        }
        return null;
    }

    public String getCmd() {
        return cmd;
    }
}
