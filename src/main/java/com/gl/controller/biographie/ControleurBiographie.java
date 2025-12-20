package com.gl.controller.biographie;

import java.util.ArrayList;
import java.util.List;

import com.gl.App;
import com.gl.controller.Controleur;
import com.gl.controller.ControleurConfirmation;
import com.gl.controller.Routeur;
import com.gl.model.Biographie;
import com.gl.model.Episode;
import com.gl.model.Paragraphe;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.persistence.EpisodeDAO;
import com.gl.persistence.ParagrapheDAO;
import com.gl.persistence.PartieDAO;
import com.gl.persistence.PersonnageDAO;
import com.gl.view.Vue;
import com.gl.view.VueConfirmation;
import com.gl.view.biographie.VueBiographie;

public class ControleurBiographie extends Controleur {
    private  Biographie biographie;
    private EpisodeDAO episodeDAO = new EpisodeDAO();
    private ParagrapheDAO paragrapheDAO = new ParagrapheDAO();
    private PersonnageDAO personnageDAO = new PersonnageDAO();
    private PartieDAO partieDAO = new PartieDAO();


    public ControleurBiographie(Routeur routeur, Vue vue, Biographie biographie) {
        super(routeur, vue);
        this.biographie = biographie;
    }

    @Override
    protected void handleLocalInput(String input) {
        final String errorMessage = "Entrée invalide. Veuillez réessayer. \n >";
        String[] entries = input.split(",");
        int episodeId;
        int paragrapheId;
        Runnable handleYes = null;
        
        switch (entries[0].trim().toLowerCase()) {
            case "a":
                if (entries.length != 6) {
                    System.out.println(errorMessage);
                    return;
                }

                Episode episode = new Episode(entries[2]);
                if(Integer.parseInt(entries[1]) == 0) {
                    Personnage personnage = personnageDAO.findById(biographie.getPersonnageId());
                    List <Partie> allParties = partieDAO.findAll();
                    List <Partie> aventures = new ArrayList<>();
                    for (Partie partie : allParties) {
                        if(partie.getPersonnages().contains(personnage) && partie.isDejaJouee()) {
                            aventures.add(partie);
                        }
                    }

                    if(!aventures.isEmpty()) {
                        System.out.println("Liste des aventures auxquelles vous pouvez lié cet épisode :");
                        for (Partie partie : aventures) {
                            System.out.println(" - " + partie.getId() + " : " + partie.getTitre());
                        }
                        System.out.println("Entrez l'id de l'aventure à laquelle vous souhaitez lier cet épisode. \n Sinon, appuyez sur Entrée pour ne pas le lier à une aventure.");
                        String aventureInput = scanner.nextLine();
                        try {
                            int aventureId = Integer.parseInt(aventureInput);
                            Partie aventureChoisie = partieDAO.findById(aventureId);
                            if (aventureChoisie != null && aventures.contains(aventureChoisie)) {
                                episode.setRelatedAventureId(aventureChoisie.getId());
                            } else {
                                System.out.println("Aventure non trouvée. L'épisode ne sera pas lié à une aventure.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrée invalide. L'épisode ne sera pas lié à une aventure.");
                        }
                    } 
                    
                    System.out.println("Ce personnage n'a aucune aventure à laquelle vous pouvez lié ce nouvel épisode");
                    episode.setRelatedBiographieId(biographie.getId());
                    episodeId = episodeDAO.save(episode);

                    Paragraphe paragraphe = new Paragraphe(entries[3], entries[4], Integer.parseInt(entries[5]) == 1);
                    paragraphe.setEpisodeId(episodeId);
                    paragrapheDAO.save(paragraphe);

                    System.out.println("Aventure crée avec succès");
                    routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                } else if (episodeDAO.findById(Integer.parseInt(entries[1])) != null) {
                    if(episode.isJoueurValide() && episode.isMjValide()) {
                        System.out.println("Cet épisode est déjà validé et ne peut pas être modifié.");
                        routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                    }
                    Paragraphe paragraphe = new Paragraphe(entries[3], entries[4], Integer.parseInt(entries[5]) == 1);
                    paragraphe.setEpisodeId(Integer.parseInt(entries[1]));
                    paragrapheDAO.save(paragraphe);
                    routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                } else {
                    System.out.println(errorMessage);
                }
                break;

            case "p":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                    return;
                }
                paragrapheId = Integer.parseInt(entries[1]);
                Paragraphe paragraphe = paragrapheDAO.findById(paragrapheId);
                if (paragraphe != null) {
                    handleYes = () -> {
                        paragraphe.rendrePublique();
                        paragrapheDAO.update(paragraphe);
                        routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                    };
                    routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir rendre ce paragrphe public ?"), handleYes));
                } else {
                    System.out.println(errorMessage);
                }
                break;

            case "me":
                if( entries.length != 4) {
                    System.out.println(errorMessage);
                }
                episodeId = Integer.parseInt(entries[2]);
                Episode episodeAmodifier = paragrapheDAO.findEpisodeById(episodeId);
                if (episodeAmodifier != null) {
                    if (entries[1].trim().equalsIgnoreCase("t")) {
                        episodeAmodifier.setTitre(entries[3]);
                        episodeDAO.update(episodeAmodifier);
                        routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                    } else {
                        System.out.println(errorMessage);
                        return;
                    }
                } else {
                    System.out.println(errorMessage);                    
                }
                break;
            
            case "ve":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                    return;
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
                        routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                    };
                    routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir valider cet épisode ?"), handleYes));
                } else {
                    System.out.println(errorMessage);
                }
                break;
            case "xp":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                }
                handleYes = () -> {
                    paragrapheDAO.delete(Integer.parseInt(entries[1]));
                    routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                };
                routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir supprimer ce paragraphe ?"), handleYes));
                break;
            
            case "xe":
                if (entries.length != 2) {
                    System.out.println(errorMessage);
                }
                handleYes = () -> {
                    episodeDAO.delete(Integer.parseInt(entries[1]));
                    routeur.push(new ControleurBiographie(routeur, new VueBiographie(biographie, VueBiographie.Type.PRIVEE), biographie));
                };
                routeur.push(new ControleurConfirmation(routeur, new VueConfirmation("Êtes vous sûr de vouloir supprimer cet épisode ?"), handleYes));
                break;
        
            default:
                System.out.println(errorMessage);
                break;
        }
    }

}
