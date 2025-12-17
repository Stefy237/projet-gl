package com.gl.persistence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
    // private static final String sep = File.separator;
    private static final String DB_FOLDER = "db";
    private static final String DB_NAME = "jeu_de_role.db";
    private static final String DB_FULL_PATH = DB_FOLDER + File.separator + DB_NAME; 
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_FULL_PATH;

    // private static Connection connection;

    private SQLiteManager() {}

    // public static Connection getConnection() {
    //     if (connection == null) {
    //         try {
    //             connection = DriverManager.getConnection("jdbc:sqlite:jeu_de_role.db");
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }
    //     return connection;
    // }

    // public static void closeConnection() {
    //     try {
    //         if (connection != null) connection.close();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    private static void ensureDbFolderExists() {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            // Utilise mkdirs() pour créer le dossier
            if (folder.mkdirs()) {
                 System.out.println("Dossier de base de données créé : " + DB_FOLDER);
            }
        }
    }

    public static Connection getConnection() {
        ensureDbFolderExists(); 
        
        try {
            return DriverManager.getConnection(JDBC_URL); 
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données : " + JDBC_URL);
            e.printStackTrace();
            return null;
        }
    }

    public static void initialize() {
        try (InputStream inputStream = SQLiteManager.class.getResourceAsStream("/install_bd.sql")) {
            
            if (inputStream == null) {
                throw new RuntimeException("Fichier install_bd.sql introuvable !");
            }

            String sqlSchema = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {
                
                stmt.executeUpdate(sqlSchema);
                System.out.println("Base de données initialisée avec succès.");
            } 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadTestData() {
        System.out.println("Chargement des données de test...");
        try (InputStream inputStream = SQLiteManager.class.getResourceAsStream("/populate.sql")) {
            
            if (inputStream == null) {
                System.err.println("Fichier data.sql introuvable !");
                return;
            }

            String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            
            try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
                
                // SQLite exécute tout le script d'un coup
                stmt.executeUpdate(sql);
                System.out.println("Données insérées avec succès !");
                
            } catch (SQLException e) {
                // Astuce : Ignorer l'erreur si les données existent déjà (Constraint Violation)
                if(e.getMessage().contains("UNIQUE constraint failed")) {
                    System.out.println("Les données semblent déjà présentes.");
                } else {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
