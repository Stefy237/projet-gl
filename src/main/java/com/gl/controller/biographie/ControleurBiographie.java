package com.gl.controller.biographie;

import java.util.OptionalInt;

import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.model.Biographie;
import com.gl.model.Paragraphe;
import com.gl.persistence.ParagrapheDAO;
import com.gl.view.Vue;

public class ControleurBiographie extends Controleur {
    private  Biographie biographie;
    private ParagrapheDAO paragrapheDAO = new ParagrapheDAO();

    public ControleurBiographie(Routeur routeur, Vue vue, Biographie biographie) {
        super(routeur, vue);
        this.biographie = biographie;
    }

    @Override
    protected void handleLocalInput(String input) {
        String[] entries = input.split(",");

        switch (entries[0].trim().toLowerCase()) {
            case "a":
                if (entries.length != 4) {
                    System.out.println("Veuillez entrer une entrée valide");
                    processInput();
                }

                if(Integer.parseInt(entries[1]) == 0) {
                    Paragraphe paragraphe = new Paragraphe(entries[3], entries[4], Integer.parseInt(entries[5]) == 1);
                    OptionalInt maxId = paragrapheDAO.findAll().stream()
                        .mapToInt(Paragraphe::getEpisodeId) 
                        .max();
                    int maxEpisodeId = maxId.orElse(1);
                    paragraphe.setEpisodeId(Integer.parseInt(entries[maxEpisodeId + 1]));
                    paragrapheDAO.save(paragraphe);
                } else if (paragrapheDAO.findEpisodeById(Integer.parseInt(entries[1])) != null) {
                    Paragraphe paragraphe = new Paragraphe(entries[3], entries[4], Integer.parseInt(entries[5]) == 1);
                    paragraphe.setEpisodeId(Integer.parseInt(entries[1]));
                    
                } else {
                    System.out.println("Veuillez entrer une entrée valide");
                    processInput();
                }
                break;

            case "p":
                
                break;

            case "m":
                
                break;

            case "x":
                
                break;
        
            default:
                break;
        }
    }

}
