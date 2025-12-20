package com.gl.controller.personnage;

import com.gl.App;
import com.gl.controller.Controleur;
import com.gl.controller.Routeur;
import com.gl.model.Biographie;
import com.gl.model.Episode;
import com.gl.model.Paragraphe;
import com.gl.model.Personnage;
import com.gl.model.Univers;
import com.gl.persistence.BiographieDAO;
import com.gl.persistence.EpisodeDAO;
import com.gl.persistence.ParagrapheDAO;
import com.gl.persistence.PersonnageDAO;
import com.gl.view.Vue;
import com.gl.view.personnage.VueAjouterPersonnage;

public class ControleurAjouterPersonnage extends Controleur {
    private PersonnageDAO personnageDAO = new PersonnageDAO();
    private BiographieDAO biographieDAO = new BiographieDAO();
    private EpisodeDAO episodeDAO = new EpisodeDAO();
    private ParagrapheDAO paragrapheDAO = new ParagrapheDAO();


     private enum EtapeFormulaire {
        NOM, PROFESSION, DATE_NAISSANCE, UNIVERS_ID, IMAGE, CONTENU_BIO, PARTIE_ID, CONFIRMATION
    }
    
    private EtapeFormulaire etapeCourante = EtapeFormulaire.NOM;
    
    // Données collectées
    private String nom;
    private String profession;
    private String naissanceDate;
    private String universId;
    private String image;
    private String contenuBio;
    private String partieId;
    
    public ControleurAjouterPersonnage(Routeur routeur, Vue vue) {
        super(routeur, vue);
    }

    @Override
    protected void handleLocalInput(String input) {
        switch (etapeCourante) {
            case NOM:
                nom = input.trim();
                etapeCourante = EtapeFormulaire.PROFESSION;
                afficherProchainChamp();
                break;

            case PROFESSION:
                profession = input.trim();
                etapeCourante = EtapeFormulaire.DATE_NAISSANCE;
                afficherProchainChamp();
                break;

            case DATE_NAISSANCE:
                naissanceDate = input.trim();
                etapeCourante = EtapeFormulaire.UNIVERS_ID;
                afficherProchainChamp();
                break;

            case UNIVERS_ID:
                universId = input.trim();
                etapeCourante = EtapeFormulaire.IMAGE;
                afficherProchainChamp();

                break;

            case IMAGE:
                image = input.trim();
                etapeCourante = EtapeFormulaire.CONTENU_BIO;
                afficherProchainChamp();
                break;

            case CONTENU_BIO:
                contenuBio = input.trim();
                etapeCourante = EtapeFormulaire.PARTIE_ID;
                afficherProchainChamp();
                break;

            case PARTIE_ID  :
                partieId = input.trim();
                sauvegarderPersonnage();
                routeur.pop();
                break;
        
            default:
                System.out.println("Entrer une valeur valide : \n >");
                break;
        }
    }



    private void afficherProchainChamp() {
        ((VueAjouterPersonnage) getVue()).afficherChamp(etapeCourante);
    }

    private void sauvegarderPersonnage() {
        Biographie biographie = new Biographie("Biographie");
        Episode episode = new Episode("Episode initial");
        Paragraphe paragraphe = new Paragraphe(contenuBio);
        episode.ajouterParagraphe(paragraphe);
        biographie.ajouterEpisode(episode);

        int biographieId = biographieDAO.save(biographie);
        episode.setRelatedBiographieId(biographieId);
        int episodeId = episodeDAO.save(episode);
        paragraphe.setEpisodeId(episodeId);
        paragrapheDAO.save(paragraphe);
        
        Personnage personnage = new Personnage(
            nom, 
            profession, 
            naissanceDate, 
            Univers.getById(Integer.parseInt(universId)),
            biographieId, 
            App.getjoueurConnecte().getId()
        );
        
        personnage.setImage(image);
        personnage.setPartieId(Integer.parseInt(partieId));
        personnage.setRelatedMjId(App.getjoueurConnecte().getId());
        personnageDAO.save(personnage);
        System.out.println("Personnage créé avec succès !");
    }
    
}
