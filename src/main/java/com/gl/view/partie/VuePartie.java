package com.gl.view.partie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.gl.App;
import com.gl.controller.partie.ControleurPartie;
import com.gl.model.Observateur;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.view.Vue;

public class VuePartie extends Vue implements Observateur {
    private ControleurPartie controleur;
    private Partie partie;
    
    public VuePartie(Partie partie) {
        this.partie = partie;
        this.partie.attache(this);
    }
    @Override
    public void miseAJour() {
        // Logique de mise à jour 
    }

    @Override
    public void afficher() {
        String texte;

        System.out.println("======================= BIENVENUE sur la partie: " + partie.getTitre() + " ========================");

        System.out.println("Situation initiale de la partie: \n " +  partie.getSituationInitiale());

        System.out.println(">>>>>>>>> Participants de la partie");
        System.out.println(" Personnage | Personnage id");

        for(Personnage personnage : partie.getPersonnages()) {
            System.out.println(  personnage.getNom() + " | " + personnage.getId());
        }

        System.out.println(">>>>>>>>> Personnages en attente de validation");
        System.out.println(" Personnage | Personnage id");

        Path path = Path.of("Buffer/Partie/partie"+partie.getId()+".txt");

        

        if (!Files.exists(path)) {
            System.out.println("Aucun personnage en attente de validation.");
            //return;
        }else{
            try (Stream<String> lines = Files.lines(path)) {
                lines.forEach(System.out::println);
            } catch (IOException e) {
                System.err.println("Erreur de lecture : " + e.getMessage());
            }
        }
        
        if(partie.getMjId() == App.getjoueurConnecte().getId()) {
            if(partie.isDejaJouee()) {
                texte = """
                        Entrez : 
                        - Pour accéder à un personnage
                            go,id du personnage
                        """;
            }
            texte = """
                    Entrez : 
                    - Pour accéder à un personnage
                        go,id du personnage
                    - Pour marquer la partie comme terminée
                        t,resumé de la partie
                    - Pour accepter un personnage
                        ac,id du personnage
                    - Pour supprimer un personnage
                        xp,id du personnage
                    """;
        } else {
            texte = "Entrer r pour revenir au menu précédent";
        }

        System.out.println(texte);
    }
    
}
