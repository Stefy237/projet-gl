package com.gl.view.partie;

import java.util.Scanner;

import com.gl.model.Partie;
import com.gl.model.Personnage;
import com.gl.model.Univers;
import com.gl.persistence.PartieDAO;
import com.gl.view.Vue;

public class VueAjouterPartie implements Vue {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void afficher() {

        System.out.println(" Univers | Univers id /n");

        for (Univers u : Univers.values()) {
            System.out.println(u + " | " + u.getId() );
        }
        
        System.out.println("Appuyez sur entre pour commencer");

    }
    
}
