package com.gl.view;

public abstract class Vue {

    /**
     * Affiche la vue
     */
    private boolean besoinAffichage = true;

    public abstract void afficher();

    public boolean doitEtreAffichee() {
        return besoinAffichage;
    }

    public void setBesoinAffichage(boolean besoin) {
        this.besoinAffichage = besoin;
    }
}
