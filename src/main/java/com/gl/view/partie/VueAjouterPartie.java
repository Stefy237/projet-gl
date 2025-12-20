package com.gl.view.partie;

import com.gl.view.Vue;

public class VueAjouterPartie extends Vue {
    @Override
    public void afficher() {
        afficherChamp(null);
    }

    public void afficherChamp(Object etape) {
        if (etape == null) {
            System.out.println("Entrez le titre de votre aventure: \n >");
            return;
        }
        
        String etapeStr = etape.toString();
        
        switch (etapeStr) {
            case "SITUATION":
                System.out.println("Entrez le resume de la situation initiale: \n >");
                break;
            case "UNIVERS":
                System.out.println("Entrez l'id de l'Univers :\n >");
                break;
            default : 
                System.out.println("Entrer une valeur valide. \n >");
        }
    }
    
}
