   
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
import com.gl.view.partie.VueAjouterPartie;

public class ControleurAjouterPartie extends Controleur {
    private PartieDAO partieDAO = new PartieDAO();

     private enum EtapeFormulaire {
        TITRE, SITUATION, UNIVERS
    }

    private EtapeFormulaire etapeCourante = EtapeFormulaire.TITRE;

    // Données collectées
    private String titre;
    private String initSituation;
    private String universId;

    public ControleurAjouterPartie(Routeur routeur, Vue vue) {
        super(routeur, vue);
    }

    @Override
    protected void handleLocalInput(String input) {
        switch (etapeCourante) {
            case TITRE:
                titre = input.trim();
                etapeCourante = EtapeFormulaire.SITUATION;
                afficherProchainChamp();
                break;

            case SITUATION:
                initSituation = input.trim();
                etapeCourante = EtapeFormulaire.UNIVERS;
                afficherProchainChamp();
                break;

            case UNIVERS:
                universId = input.trim();
                afficherProchainChamp();
                sauvegarderPartie();
                routeur.pop();
                break;
            default:
                System.out.println("Entrer une valeur valide : \n >");
                break;
        }

    }

    private void afficherProchainChamp() {
        ((VueAjouterPartie) getVue()).afficherChamp(etapeCourante);
    }
    
    private void sauvegarderPartie() {
        Partie partie = new Partie(titre,Univers.getById(Integer.parseInt(universId)), initSituation, App.getjoueurConnecte().getId());

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

         System.out.println("partie creee");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}