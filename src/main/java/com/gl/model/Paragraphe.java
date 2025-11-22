package com.gl.model;

import com.gl.persistence.ParagrapheDAO;

public class Paragraphe extends Sujet {
    private String titre;
    private boolean isPrivate;

    
    public void rendrePublique() {
        this.isPrivate = false;
        informe();
    }
}
