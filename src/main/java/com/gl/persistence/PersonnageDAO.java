package com.gl.persistence;

import com.gl.model.Personnage;
import com.gl.model.Univers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO implements DAO<Personnage> {
    
    @Override
    public void save(Personnage p) {
        // ... (Méthode save existante)
    }

    @Override
    public Personnage findById(int id) { 
        String sql = "SELECT nom, profession, date_naissance, univers_id, biogragie_id, relatedJoueurId, relatedMjId FROM Personnage WHERE id = ?";
        Personnage personnage = null;

        try (Connection conn = SQLiteManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    String nom = rs.getString("nom");
                    String profession = rs.getString("profession");
                    String naissanceDate = rs.getString("date_naissance");
                    int universId = rs.getInt("univers_id");
                    int biographieId = rs.getInt("biogragie_id"); 
                    int relatedJoueurId = rs.getInt("relatedJoueurId");
                    int relatedMjId = rs.getInt("relatedMjId");

                    personnage = new Personnage(nom, profession, naissanceDate, Univers.getById(universId), biographieId, relatedJoueurId);
                    personnage.setId(id);
                    personnage.setRelatedMjId(relatedMjId);
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personnage;
    }

    @Override
    public List<Personnage> findAll() { 
        String sql = "SELECT id, nom, profession, date_naissance, univers_id, biogragie_id, relatedJoueurId, relatedMjId FROM Personnage";
        List<Personnage> personnages = new ArrayList<>();

        try (Connection conn = SQLiteManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String profession = rs.getString("profession");
                String naissanceDate = rs.getString("date_naissance");
                int universId = rs.getInt("univers_id");
                int biographieId = rs.getInt("biogragie_id");
                int relatedJoueurId = rs.getInt("relatedJoueurId");
                int relatedMjId = rs.getInt("relatedMjId");

                Personnage p = new Personnage(nom, profession, naissanceDate, Univers.getById(universId), biographieId, relatedJoueurId);
                p.setId(id);
                p.setRelatedMjId(relatedMjId);
                personnages.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personnages;
    }

    @Override
    public void update(Personnage p) {
        String sql = "UPDATE Personnage SET nom = ?, profession = ?, date_naissance = ?, univers_id = ?, biogragie_id = ?, relatedJoueurId = ?, relatedMjId = ? WHERE id = ?";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getProfession());
            stmt.setString(3, p.getNaissanceDate());
            stmt.setInt(4, p.getUnivers().getId());
            stmt.setInt(5, p.getBiographieId());
            stmt.setInt(6, p.getRelatedJoueurId());
            stmt.setInt(7, p.getRelatedMjId());
            stmt.setInt(8, p.getId()); // Condition WHERE
            
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Personnage WHERE id = ?";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}