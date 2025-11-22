package com.gl;

import com.gl.controller.joueur.ControleurAccueil;
import com.gl.view.joueur.VueAccueil;

public class App {
    public static void main(String[] args) {
        Routeur router = Routeur.getInstance();
        router.start(new ControleurAccueil(router, new VueAccueil()));
    }
}
