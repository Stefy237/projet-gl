package com.gl.model;

public enum Univers {
    FANTASY(1),
    SCIENCE_FICTION(2),
    HISTORIQUE(3),
    MODERNE(4),
    POST_APOCALYPTIQUE(5);

    private final int id;

    private Univers(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public static Univers getById(int id) {
        for (Univers u : values()) {
            if (u.id == id) {
                return u;
            }
        }
        throw new IllegalArgumentException("ID d'Univers inconnu: " + id);
    }
}
