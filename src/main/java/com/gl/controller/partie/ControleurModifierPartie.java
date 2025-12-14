package com.gl.controller.partie;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.personnage.ControleurAjouterPersonnage;
import com.gl.model.Partie;
import com.gl.persistence.PartieDAO;
import com.gl.view.Vue;
import com.gl.view.partie.VueAjouterPartie;
import com.gl.view.personnage.VueAjouterPersonnage;

public class ControleurModifierPartie extends Controleur {

     private Partie partie;

    public ControleurModifierPartie(Routeur routeur, Vue vue, Partie partie) {
        super(routeur, vue);
        this.partie=partie;
        //TODO Auto-generated constructor stub
    }

    @Override
    protected void handleLocalInput(String input) {
        switch (input) {
            case "1":
                PartieDAO partieDAO = new PartieDAO();
                partieDAO.delete(partie.getId());
                break;
            
            case "2":
                System.out.println("Entrez l'id du personnage");
                
                break;

            case "3":
                
                break;

            case "4":
                System.out.println("Entrez l'id du personnage");
                
                routeur.push(new ControleurAjouterPartie(routeur, new VueAjouterPartie()));
                break;

            case "5":
                routeur.push(new ControleurAjouterPersonnage(routeur, new VueAjouterPersonnage()));
                break;
        
            default:
                System.out.println("Veullez entrer une entrée valide");
                processInput();
        }
    }
    
}
