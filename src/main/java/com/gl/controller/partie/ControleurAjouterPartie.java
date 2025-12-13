package com.gl.controller.partie;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.model.Partie;
import com.gl.model.Univers;
import com.gl.persistence.PartieDAO;
import com.gl.view.Vue;

public class ControleurAjouterPartie extends Controleur {

    public ControleurAjouterPartie(Routeur routeur, Vue vue) {
        super(routeur, vue);
        //TODO Auto-generated constructor stub
    }

    @Override
    protected void handleLocalInput(String input) {
        System.out.println("Entrez le titre de votre aventure");
        String titre = scanner.nextLine();
        System.out.println("Entrez le resume de la situation initiale: ");
        String initSituation = scanner.nextLine();
        System.out.println("Entrez l'id de l'Univers");
        int id = scanner.nextInt();

        Partie partie = new Partie(titre,Univers.getById(id),initSituation);

        PartieDAO partieDAO = new PartieDAO();
        partieDAO.save(partie);

        System.out.println("partie creee");
    }
    
}
