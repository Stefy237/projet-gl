   
package com.gl.controller.partie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.gl.App;
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

        Partie partie = new Partie(titre,Univers.getById(id),initSituation, App.getjoueurConnecte().getId());

        PartieDAO partieDAO = new PartieDAO();
        int partieId = partieDAO.save(partie);
        try {
            //Path file = Path.of("../../Buffer/Partie/partie0.txt");
            Path file = Path.of("Buffer","Partie","partie"+partieId+".txt");
            Path path = Path.of("Buffer","Partie","partie.txt");

            //  Crée les dossiers parents si nécessaires
            Files.createDirectories(file.getParent());
            Files.createDirectories(path.getParent());

            //  Crée le fichier s’il n’existe pas
            Files.createFile(file);
            //Files.createFile(path);

            Files.writeString(
                path,
                ""+partie.getTitre()+" | "+partie.getId()+" | "+partie.getUnivers().getId()+ System.lineSeparator(), StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("partie creee");
        routeur.pop();
    }
    
}