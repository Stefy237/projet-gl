package com.gl.view.biographie;

import com.gl.model.Biographie;
import com.gl.model.Episode;
import com.gl.model.Observateur;
import com.gl.model.Paragraphe;
import com.gl.view.Vue;

public class VueBiographie implements Observateur, Vue {
    public enum Type { PRIVEE, PUBLIQUE}

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
            System.out.println("\n >>>>>>>>>>>>>>> Episode " + episode.getId() + " -- Status : " + (episode.isValider() ? "Validé" : "En cours d'écriture") + " \n Titre -- " + episode.getTitre() );

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

            - Pour ajouter creer un épisode (avec son premier paragraphe)
                a,0,titre de l'episode, titre du paragraphe,contenu du paragraphe,privée? (1 pour oui 0 sinon)
                ex : a,0,le lycée,la rentree,le premier jour de classe,1

            - Pour rendre un paragraphe pubic
                p, numéro du paragraphe
                ex : p,1,10

            - Pour modifier épisode :
                * Le titre d'un épisode
                    me, t, numéro de l'épisode, nouveau titre de l'épisode
                * Le titre d'un paragraphe
                    mp, t, numéro du paragraphe, nouveau titre du paragraphe
                * Le contenu d'un paragraphe
                    mp, c, numéro du paragraphe, nouveau contenu du paragraphe
                    
            - Pour valider un épisode
                ve, numéro de l'épisode

            - Pour supprimer paragraphe
                xp, numéro du paragraphe

            - Pour supprimer un épisode
                xe,numéro de l'épisode
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
                if(!condition  && episode.isValider()) {
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
