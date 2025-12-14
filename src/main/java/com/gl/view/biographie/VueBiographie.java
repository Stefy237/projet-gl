package com.gl.view.biographie;

import com.gl.controller.biographie.ControleurBiographie;
import com.gl.model.Biographie;
import com.gl.model.Episode;
import com.gl.model.Observateur;
import com.gl.model.Paragraphe;
import com.gl.persistence.ParagrapheDAO;
import com.gl.view.Vue;

public class VueBiographie implements Observateur, Vue {
    public enum Type { PRIVEE, PUBLIQUE}

    private ParagrapheDAO paragrapheDAO = new ParagrapheDAO();
    private Biographie biographie;
    private Type type;

    public VueBiographie(Biographie biographie, Type type) {
        this.biographie = biographie;
        biographie.attache(this);
        this.type = type;
    }

    @Override
    public void miseAJour() {
        // Logique de mise à jour
    }

    @Override
    public void afficher() {
        if (this.type == Type.PRIVEE) {
            System.out.println("""
            ----------------------------------------------------------------------------------------------------------------
                                                            BIOGRAPHIE INTEGRALE
            ----------------------------------------------------------------------------------------------------------------
        """);  

        for(Episode episode : biographie.getEpisodes()) {
            System.out.println("\n >>>>>>>>>>>>>>> Episode " + episode.getId() + " \n Titre -- " + episode.getTitre() );

            for (Paragraphe paragraphe : episode.getParagraphes()) {
                paragraphe.attache(this);
                System.out.println("\n **** PARAGRAPHE " + paragraphe.getId() + " -- Type : " + (paragraphe.isPrivate() ? "Privée" : "Publique") + " \n Titre -- " + paragraphe.getTitre() + "\n" + paragraphe.getContenu());
            }
        }

        System.out.println("\n");
        System.out.println(""" 
           Entrez (en respectant le format): 

            - Pour ajouter un paragraphe à un épisode existant
                a,numéro de l'épisode,titre du paragraphe,contenu du paragraphe,privée? (1 pour oui 0 sinon)
                ex : a,2,la rentree,le premier jour de classe,0 

            - Pour ajouter creer un épisode et y ajouter un paragrphe (soit ajouter un paragraphe à un paragraphe inexistant, création récursive)
                a,0,titre de l'episode, titre du paragraphe,contenu du paragraphe,privée? (1 pour oui 0 sinon)
                ex : a,0,le lycée,la rentree,le premier jour de classe,0 

            - Pour rendre un paragraphe pubic
                p,numéro de l'épisode, numéro du paragraphe
                ex : p,1,1

            - Pour modifier paragraphe
                m,numéro de l'épisode, numéro du paragraphe

            - Pour supprimer paragraphe
                x,numéro de l'épisode, numéro du paragraphe

            - Pour supprimer un épisode
                x,numéro de l'épisode
        """);
        } else if(this.type == Type.PUBLIQUE) {
                System.out.println("""
                ----------------------------------------------------------------------------------------------------------------
                                                                    BIOGRAPHIE 
                ----------------------------------------------------------------------------------------------------------------
            """);  

            for(Episode episode : biographie.getEpisodes()) {
                boolean condition = episode.getParagraphes().stream()
                                            .allMatch(Paragraphe::isPrivate);
                if(!condition) {
                    System.out.println("\n >>>>>>>>>>>>>>> Episode " + episode.getId() + " \n Titre -- " + episode.getTitre() );

                    for (Paragraphe paragraphe : episode.getParagraphes()) {
                        if(!paragraphe.isPrivate()) {
                            paragraphe.attache(this);
                            System.out.println("\n **** PARAGRAPHE " + paragraphe.getId() + " \n Titre -- " + paragraphe.getTitre() + "\n" + paragraphe.getContenu());
                        }
                    }
                }
            }

            System.out.println("\n");
            System.out.println("Entrer r pour retourner à la page précédente");
        }
    }
    
}
