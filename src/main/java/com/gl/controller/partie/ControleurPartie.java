package com.gl.controller.partie;

import java.util.Scanner;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.personnage.ControleurAjouterPersonnage;
import com.gl.controller.personnage.ControleurPersonnage;
import com.gl.model.Partie;
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

    public ControleurPartie(Routeur routeur, Vue vue) {
        super(routeur, vue);
        //TODO Auto-generated constructor stub
    }

    // public ControleurPartie(Partie partie) {
    //     this.partie = partie;
    // }

    public Partie creerPartie(String titre, Univers univers, String resume) {
        return new Partie(titre, univers, resume);
    }

    public void modifierPartie(Partie partie) {
        
    }

    @Override
    protected void handleLocalInput(String input) {

        PersonnageDAO personnageDAO = new PersonnageDAO();
        switch (input) {
            case "1":
                routeur.push(new ControleurModifierPartie(routeur,new VueModifierPartie(),partie));
                break;
            
            case "2":
                System.out.println("entrez l'id du personnage");
                int id = scanner.nextInt();
                routeur.push(new ControleurPersonnage(routeur, new VuePersonnage(),personnageDAO.findById(id)));
                
                break;

        
            default:
                System.out.println("Veullez saisir une entrée valide");
                processInput();
        }
    }
}
