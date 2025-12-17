package com.gl.controller.partie;

import java.util.Scanner;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.personnage.ControleurAjouterPersonnage;
import com.gl.controller.personnage.ControleurPersonnage;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.model.Univers;
import com.gl.persistence.PartieDAO;
import com.gl.persistence.PersonnageDAO;
import com.gl.view.Vue;
import com.gl.view.partie.VueAjouterPartie;
import com.gl.view.partie.VueModifierPartie;
import com.gl.view.personnage.VueAjouterPersonnage;
import com.gl.view.personnage.VuePersonnage;

public class ControleurPartie extends Controleur{
    private Partie partie;

    Scanner scanner = new Scanner(System.in);

    public ControleurPartie(Routeur routeur, Vue vue, Partie partie) {
        super(routeur, vue);
        this.partie = partie;
    }

    @Override
    protected void handleLocalInput(String input) {
        PersonnageDAO personnageDAO = new PersonnageDAO();
        String[] entries = input.split(",");

        switch (entries[0].trim().toLowerCase()) {
            case "1":
                routeur.push(new ControleurModifierPartie(routeur,new VueModifierPartie(),partie));
                break;
            
            case "2":
                System.out.println("entrez l'id du personnage \n > ");
                int id = scanner.nextInt();
                Personnage personnage = personnageDAO.findById(id);
                routeur.push(new ControleurPersonnage(routeur, new VuePersonnage(personnage), personnage));
                
                break;

        
            default:
                System.out.println("Entrée invalide. Veuillez réessayer.");
                processInput();
        }
    }
}
