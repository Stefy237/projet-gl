package com.gl.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.gl.model.Partie;
import com.gl.model.Univers;

public class PartieDAO implements DAO<Partie> {

    @Override
    public void save(Partie entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Partie findById(int id) {
        // Inclusion des colonnes du modèle et de la DDL
        String sql = "SELECT id, titre, resume, jouee, validee, univers_id FROM Partie WHERE id = ?";
        Partie partie = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    
                    // 1. Extraction des données
                    String titre = rs.getString("titre");
                    String resume = rs.getString("resume");
                    
                    // Conversion des types spécifiques
                    boolean dejaJouee = rs.getInt("jouee") == 1;
                    boolean validee = rs.getInt("validee") == 1; 
                    int universId = rs.getInt("univers_id");
                    
                    // 2. Construction de l'objet Modèle
                    Partie nouvellePartie = new Partie(titre, Univers.getById(universId), resume);
                    
                    // 3. Setter les propriétés
                    nouvellePartie.setId(id);
                    nouvellePartie.setDejaJouee(dejaJouee);
                    nouvellePartie.setValidee(validee);
                    
                    partie = nouvellePartie;
                    
                    // NOTE: Le chargement des List<Personnage> (Chargement Eager)
                    // devrait être géré par un autre DAO (PersonnageDAO) et injecté ici si nécessaire.
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur FindById Partie : " + e.getMessage());
            e.printStackTrace();
        }
        return partie;
    }

    public Partie findById(int id, Connection conn) {
        // Inclusion des colonnes du modèle et de la DDL
        String sql = "SELECT id, titre, resume, jouee, validee, univers_id FROM Partie WHERE id = ?";
        Partie partie = null;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    
                    // 1. Extraction des données
                    String titre = rs.getString("titre");
                    String resume = rs.getString("resume");
                    
                    // Conversion des types spécifiques
                    boolean dejaJouee = rs.getInt("jouee") == 1;
                    boolean validee = rs.getInt("validee") == 1; 
                    int universId = rs.getInt("univers_id");
                    
                    // 2. Construction de l'objet Modèle
                    Partie nouvellePartie = new Partie(titre, Univers.getById(universId), resume);
                    
                    // 3. Setter les propriétés
                    nouvellePartie.setId(id);
                    nouvellePartie.setDejaJouee(dejaJouee);
                    nouvellePartie.setValidee(validee);
                    
                    partie = nouvellePartie;
                    
                    // NOTE: Le chargement des List<Personnage> (Chargement Eager)
                    // devrait être géré par un autre DAO (PersonnageDAO) et injecté ici si nécessaire.
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur FindById Partie : " + e.getMessage());
            e.printStackTrace();
        }
        return partie;
    }

    @Override
    public List<Partie> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public void update(Partie entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
