package com.gl.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:jeu_de_role.db");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() {
        try (InputStream inputStream = SQLiteManager.class.getResourceAsStream("/schema.sql")) {
            
            if (inputStream == null) {
                throw new RuntimeException("Fichier schema.sql introuvable !");
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
