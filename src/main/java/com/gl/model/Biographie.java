package com.gl.model;

import java.util.ArrayList;
import java.util.List;

public class Biographie {
    private String titre;
    private List<Episode> episodes = new ArrayList<>();

    public void ajouterEpisode(Episode e) {
        episodes.add(e);
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }
}
