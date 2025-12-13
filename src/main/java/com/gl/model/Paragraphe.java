package com.gl.model;

public class Paragraphe extends Sujet {
    private int id;
    private int episodeId;

    private String titre;
    private String contenu;
    private boolean isPrivate;
    
    public Paragraphe(String titre, String contenu, boolean isPrivate) {
        this.titre = titre;
        this.contenu = contenu;
        this.isPrivate = isPrivate;
    }
    
    public void rendrePublique() {
        this.isPrivate = false;
        informe();
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    public String getTitre() {
        return titre;
    }


    public void setTitre(String titre) {
        this.titre = titre;
    }


    public String getContenu() {
        return contenu;
    }


    public void setContenu(String contenu) {
        this.contenu = contenu;
    }


    public boolean isPrivate() {
        return isPrivate;
    }


    // public void setPrivate(boolean isPrivate) {
    //     this.isPrivate = isPrivate;
    // }
}
