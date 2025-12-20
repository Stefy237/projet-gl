package com.gl.view.personnage;

import com.gl.view.Vue;

public class VueAjouterPersonnage extends Vue {

    @Override
    public void afficher() {
        // System.out.println("======================== AJOUTER UN PERSONNAGE ======================");
        afficherChamp(null);
    }

    public void afficherChamp(Object etape) {
        if (etape == null) {
            System.out.println("Veuillez entrer le nom du personnage: \n >");
            return;
        }
        
        String etapeStr = etape.toString();
        
        switch (etapeStr) {
            case "PROFESSION":
                System.out.println("Veuillez entrer la profession du personnage: \n >");
                break;
            case "DATE_NAISSANCE":
                System.out.println("Veuillez entrer la date de naissance du personnage: \n >");
                break;
            case "UNIVERS_ID":
                System.out.println("Veuillez entrer l'ID de l'univers du personnage: \n >");
                break;
            case "IMAGE":
                System.out.println("Veuillez entrer le chemin de l'image du personnage (optionnel): \n >");
                break;
            case "CONTENU_BIO":
                System.out.println("Veuillez entrer le contenu de la biographie initiale: \n >");
                break;
            case "PARTIE_ID":
                System.out.println("Veuillez entrer l'ID de la partie associée: \n >");
                break;
            default : 
                System.out.println("Entrer une valeur valide. \n >");
        }
    }
    
}
