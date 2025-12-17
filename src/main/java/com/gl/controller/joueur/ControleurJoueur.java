package com.gl.controller.joueur;

import java.util.Scanner;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.partie.ControleurAjouterPartie;
import com.gl.controller.partie.ControleurPartie;
import com.gl.controller.personnage.ControleurAjouterPersonnage;
import com.gl.controller.personnage.ControleurPersonnage;
import com.gl.model.Joueur;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.persistence.PartieDAO;
import com.gl.persistence.PersonnageDAO;
import com.gl.view.Vue;
import com.gl.view.partie.VueAjouterPartie;
import com.gl.view.partie.VuePartie;
import com.gl.view.personnage.VueAjouterPersonnage;
import com.gl.view.personnage.VuePersonnage;

public class ControleurJoueur extends Controleur {
    private Joueur joueur;
    
    public ControleurJoueur(Routeur routeur, Vue vue, Joueur joueur) {
        super(routeur, vue);
        this.joueur = joueur;
    }

    @Override
    protected void handleLocalInput(String input) {
        final String errorMessage = "Entrée invalide. Veuillez réessayer.";
        Scanner sc = new Scanner(System.in);
        PartieDAO partieDAO = new PartieDAO();
        PersonnageDAO personnageDAO = new PersonnageDAO();

        switch (input) {
            case "1":
                System.out.print("\n Entrez l'id de la partie \n >");
                int partieId = 0;
                try {
                    partieId = Integer.parseInt(sc.nextLine());     
                } catch (NumberFormatException e) {
                   System.out.println(errorMessage);
                    processInput();
                }
                
                Partie partie = partieDAO.findById(partieId);
                if(partie != null) {
                    routeur.push(new ControleurPartie(routeur, new VuePartie(partie), partie));
                } else {
                    System.out.println(errorMessage);
                    processInput();
                }
                break;
            
            case "2":
                System.out.print("\n Entrez l'id du personnage \n >");
                int personnageId = 0;
                try {
                    personnageId = Integer.parseInt(sc.nextLine());     
                } catch (NumberFormatException e) {
                   System.out.println(errorMessage);
                    processInput();
                }
                
                Personnage personnage = personnageDAO.findById(personnageId);
                if(personnage != null) {
                    routeur.push(new ControleurPersonnage(routeur, new VuePersonnage(personnage), personnage));
                } else {
                    System.out.println(errorMessage);
                    processInput();
                }
                
                break;

            case "3":
                routeur.push(new ControleurAjouterPartie(routeur, new VueAjouterPartie()));
                break;

            case "4":
                routeur.push(new ControleurAjouterPersonnage(routeur, new VueAjouterPersonnage()));
                break;
        
            default:
                System.out.println(errorMessage);
                processInput();
        }
    }
}
