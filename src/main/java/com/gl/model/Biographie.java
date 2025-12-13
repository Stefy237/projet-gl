package com.gl.model;

import java.util.ArrayList;
import java.util.List;

public class Biographie {
    private int id;
    private String titre;
    private int personnageId;
    private List<Episode> episodes = new ArrayList<>();

    public Biographie(String titre) {
        this.titre = titre;
    }
    public void ajouterEpisode(Episode e) {
        episodes.add(e);
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
    public int getPersonnageId() {
        return personnageId;
    }
    public void setPersonnageId(int personnageId) {
        this.personnageId = personnageId;
    }
}
