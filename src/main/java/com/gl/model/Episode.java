package com.gl.model;

import java.util.ArrayList;
import java.util.List;

import com.gl.persistence.ParagrapheDAO;

public class Episode extends Sujet {
    private String titre;
    private List<Paragraphe> paragraphes = new ArrayList<>();

    
    public void ajouterParagraphe(Paragraphe p) {
        paragraphes.add(p);
        informe();
    }

    public void valider() {
        // validation logique
    }
}
