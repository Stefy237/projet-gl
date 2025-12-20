package com.gl.controller.partie;

import java.util.Scanner;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.personnage.ControleurPersonnage;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.persistence.PartieDAO;
import com.gl.persistence.PersonnageDAO;
import com.gl.view.Vue;
import com.gl.view.personnage.VuePersonnage;

public class ControleurPartie extends Controleur{
    private Partie partie;
    private PartieDAO partieDAO = new PartieDAO();

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
            case "go":
                int id = Integer.parseInt(entries[1]);
                Personnage personnage = personnageDAO.findById(id);
                routeur.push(new ControleurPersonnage(routeur, new VuePersonnage(personnage), personnage));
                break;

            case "t":
                String resume = entries[1];
                partie.setResume(resume);
                partie.setDejaJouee(true);
                partieDAO.update(partie);
                break;

            case "ac":
                id = Integer.parseInt(entries[1]);
                personnage = personnageDAO.findById(id);
                partie.ajouterPersonnage(personnage);
                break;

            case "xp":
                 id = Integer.parseInt(entries[1]);
                 personnage = personnageDAO.findById(id);
                 partie.supprimerPersonnage(personnage);
                break;

            default:
                System.out.println("Entrée invalide. Veuillez réessayer. \n >");
                break;
        }
    }
}