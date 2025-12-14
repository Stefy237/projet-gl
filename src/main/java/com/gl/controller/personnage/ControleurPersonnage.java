package com.gl.controller.personnage;

import com.gl.model.Biographie;
import com.gl.model.Joueur;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.model.Univers;
import com.gl.persistence.JoueurDAO;
import com.gl.persistence.ParagrapheDAO;
import com.gl.persistence.PartieDAO;
import com.gl.persistence.PersonnageDAO;
import com.gl.view.Vue;
import com.gl.view.biographie.VueBiographie;
import com.gl.view.partie.VuePartie;

import com.gl.App;
import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.biographie.ControleurBiographie;
import com.gl.controller.partie.ControleurPartie;

public class ControleurPersonnage extends Controleur {
    private Personnage personnage;

    private PersonnageDAO personnageDAO = new PersonnageDAO();
    private JoueurDAO joueurDAO = new JoueurDAO();
    private PartieDAO partieDAO = new PartieDAO();
    private ParagrapheDAO paragrapheDAO = new ParagrapheDAO();


    public ControleurPersonnage(Routeur routeur, Vue vue, Personnage personnage) {
        super(routeur, vue);
        this.personnage = personnage;
    }

    @Override
    protected void handleLocalInput(String input) {
        Biographie biographie;
        String[] entries = input.split(",");

        switch (entries[0].trim().toLowerCase()) {
            case "t":
                if(entries.length != 2) {
                    System.out.println("Veuillez une entrée valide");
                    processInput();
                }

                App.getjoueurConnecte().getPersonnages().remove(personnage);
                personnage.setRelatedJoueurId(Integer.parseInt(entries[1]));
                joueurDAO.update(App.getjoueurConnecte());
                personnageDAO.update(personnage);
                routeur.backToHome();
                break;

            case "c":
                if(entries.length != 2) {
                    System.out.println("Veuillez une entrée valide");
                    processInput();
                }
                
                int newMjId = Integer.parseInt(entries[1]);
                Joueur newMJ = joueurDAO.findById(newMjId);

                if(newMJ.getPartieMJ().isEmpty()) {
                    System.out.println("Le joueur mentioné n'est meneur de jeu d'aucune partie -- Action impossible \n Veuillez ressayer");
                    processInput();
                }
                
                boolean condition = newMJ.getPersonnages().stream()
                    .anyMatch(personnage -> 
                        personnage.getPartieId() <= 0 || 
                        partieDAO.findById(personnage.getPartieId()).isDejaJouee() 
                    );
                if(!newMJ.getPersonnages().isEmpty() && !condition) {
                    System.out.println("Ce meneur de jeu est engagé en tant que joueur dans une autre partie -- Action impossible \n Veuillez ressayer");
                    processInput();
                }
                
                personnage.setRelatedMjId(Integer.parseInt(entries[1]));
                personnageDAO.update(personnage);
                routeur.pop();
                break;

            case "bio":
                biographie = paragrapheDAO.findBiographieById(personnage.getBiographieId());
                routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                break;
            
            case "bio_pub":
                biographie = paragrapheDAO.findBiographieById(personnage.getBiographieId());
                routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PUBLIQUE), biographie));
                break;

            case "go_partie":
                Partie partie = partieDAO.findById(personnage.getPartieId());
                routeur.push(new ControleurPartie(routeur, new VuePartie(partie), partie));
                break;
        
            default:
                break;
        }
    }
}
