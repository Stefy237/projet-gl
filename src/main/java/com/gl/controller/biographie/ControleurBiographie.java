package com.gl.controller.biographie;

import com.gl.App;
import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.controller.ControleurConfirmation;
import com.gl.model.Biographie;
import com.gl.model.Episode;
import com.gl.model.Paragraphe;
import com.gl.persistence.EpisodeDAO;
import com.gl.persistence.ParagrapheDAO;
import com.gl.view.Vue;
import com.gl.view.VueConfirmation;

public class ControleurBiographie extends Controleur {
    private  Biographie biographie;
    private EpisodeDAO episodeDAO = new EpisodeDAO();
    private ParagrapheDAO paragrapheDAO = new ParagrapheDAO();

    public ControleurBiographie(Routeur routeur, Vue vue, Biographie biographie) {
        super(routeur, vue);
        this.biographie = biographie;
    }

    @Override
    protected void handleLocalInput(String input) {
        final String errorMessage = "Entrée invalide. Veuillez réessayer.";
        String[] entries = input.split(",");
        int episodeId;
        int paragrapheId;
        Runnable handleYes = null;
        
        switch (entries[0].trim().toLowerCase()) {
            case "a":
                if (entries.length != 4) {
                    System.out.println(errorMessage);
                    processInput();
                }

                Episode episode = new Episode(entries[2]);
                if(Integer.parseInt(entries[1]) == 0) {
                    episode.setRelatedBiographieId(biographie.getId());
                    episodeId = episodeDAO.save(episode);

                    Paragraphe paragraphe = new Paragraphe(entries[3], entries[4], Integer.parseInt(entries[5]) == 1);
                    paragraphe.setEpisodeId(episodeId);
                    paragrapheDAO.save(paragraphe);
                } else if (episodeDAO.findById(Integer.parseInt(entries[1])) != null) {
                    if(episode.isJoueurValide() && episode.isMjValide()) {
                        System.out.println("Cet épisode est déjà validé et ne peut pas être modifié.");
                        routeur.pop();
                    }
                    Paragraphe paragraphe = new Paragraphe(entries[3], entries[4], Integer.parseInt(entries[5]) == 1);
                    paragraphe.setEpisodeId(Integer.parseInt(entries[1]));
                    paragrapheDAO.save(paragraphe);
                    routeur.pop();
                } else {
                    System.out.println(errorMessage);
                    processInput();
                }
                break;

            case "p":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                    processInput();
                }
                paragrapheId = Integer.parseInt(entries[1]);
                Paragraphe paragraphe = paragrapheDAO.findById(paragrapheId);
                if (paragraphe != null) {
                    handleYes = () -> {
                        paragraphe.rendrePublique();
                        paragrapheDAO.update(paragraphe);
                    };
                    routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir rendre ce paragrphe public ?"), handleYes));
                } else {
                    System.out.println(errorMessage);
                    processInput();
                }
                break;

            case "me":
                if( entries.length != 4) {
                    System.out.println(errorMessage);
                    processInput();
                }
                episodeId = Integer.parseInt(entries[2]);
                Episode episodeAmodifier = paragrapheDAO.findEpisodeById(episodeId);
                if (episodeAmodifier != null) {
                    if (entries[1].trim().equalsIgnoreCase("t")) {
                        episodeAmodifier.setTitre(entries[3]);
                        episodeDAO.update(episodeAmodifier);
                        routeur.pop();
                    } else {
                        System.out.println(errorMessage);
                        processInput();
                    }
                } else {
                    System.out.println(errorMessage);
                    processInput();
                    
                }
                break;
            
            case "ve":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                    processInput();
                }
                episodeId = Integer.parseInt(entries[1]);
                Episode episodeAvalider = episodeDAO.findById(episodeId);
                if (episodeAvalider != null) {
                    handleYes = () -> {
                        if(App.getjoueurConnecte().getPersonnages().stream().anyMatch(personnage -> personnage.getBiographieId() == biographie.getId())) {
                            episodeAvalider.setJoueurValide(true);
                        } else {
                            episodeAvalider.setMjValide(true);
                        }
                        episodeDAO.update(episodeAvalider);
                    };
                    routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir valider cet épisode ?"), handleYes));
                } else {
                    System.out.println(errorMessage);
                    processInput();
                }
                break;
            case "xp":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                    processInput();
                }
                handleYes = () -> {
                    paragrapheDAO.delete(Integer.parseInt(entries[1]));
                };
                routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir supprimer ce paragraphe ?"), handleYes));
                break;
            
            case "xe":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                    processInput();
                }
                handleYes = () -> {
                    episodeDAO.delete(Integer.parseInt(entries[1]));
                };
                routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir supprimer cet épisode ?"), handleYes));
                break;
        
            default:
                break;
        }
    }

}
