package com.gl.view.joueur;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.gl.model.Joueur;
import com.gl.model.Observateur;
import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.persistence.PartieDAO;
import com.gl.view.Vue;

public class VueJoueur extends Vue implements Observateur {
    private Joueur joueur;
    private PartieDAO partieDAO = new PartieDAO();

    public VueJoueur() {
    }

    public VueJoueur(Joueur joueur) {
        this.joueur = joueur;
        this.joueur.attache(this);  
    }

    @Override
    public void miseAJour() {
        
    }

    @Override
    public void afficher() {
        System.out.println("======================= BIENVENUE " + joueur.getPseudo() + " ========================");

        System.out.println(">>>>>>>>>>> Liste des parties dans lequel vous avez un personnage");
        System.out.println("Partie | Partie id | Personnage | Personnage id");

        for(Personnage personnage : joueur.getPersonnages()) {
            Partie partie = partieDAO.findById(personnage.getPartieId());
            partie.attache(this);
            System.out.println(partie.getTitre() + " | " + partie.getId() + " | " + personnage.getNom() + " | " + personnage.getId());
        }

        if(!joueur.getPartieMJ().isEmpty()) {
            System.out.println(">>>>>>>>>>> Liste des parties dans lequel vous êtes meneur de jeu");
            System.out.println("Partie | Partie id");

            for(Partie partie : joueur.getPartieMJ()) {
                System.out.println(partie.getTitre() + " | " + partie.getId());
            }
        }

        System.out.println(">>>>>>>>>>> Liste des parties des propositions de partie");
        Path path = Path.of("Buffer/Partie/partie.txt");

        

        if (!Files.exists(path)) {
            System.out.println("Aucune proposition de partie.");
            //return;
        }else{
            try (Stream<String> lines = Files.lines(path)) {
                lines.forEach(System.out::println);
            } catch (IOException e) {
                System.err.println("Erreur de lecture : " + e.getMessage());
            }
        }
        

        System.out.println("""
                Entrez : 
                - Pour afficher une partie en particulier
                    1
                - Pour afficher un personnage en particulier
                    2
                - Pour creer une partie
                    3
                - Pour creer un personnage
                    4
                - Pour rejoindre une partie
                    5,id de la partie,id du personnage
                """);
    }
    
}
