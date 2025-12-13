package com.gl.controller.personnage;

import com.gl.model.Joueur;
import com.gl.model.Personnage;
import com.gl.model.Univers;
import com.gl.view.Vue;
import com.gl.Routeur;
import com.gl.controller.Controleur;
import com.gl.model.Biographie;

public class ControleurPersonnage extends Controleur {

    public ControleurPersonnage(Routeur routeur, Vue vue, Personnage personnage) {
        super(routeur, vue);
        this.personnage=personnage;
        //TODO Auto-generated constructor stub
    }


    private Personnage personnage;

    

    @Override
    protected void handleLocalInput(String input) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleLocalInput'");
    }


}
