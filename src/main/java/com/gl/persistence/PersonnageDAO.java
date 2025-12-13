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
        // NOTE: Nous utilisons LEFT JOIN pour récupérer les IDs du Joueur et du MJ 
        // depuis la table Participation.
        String sql = "SELECT p.nom, p.profession, p.date_naissance, p.univers_id, p.biogragie_id, p.image_path, " +
                     "pa.joueur_id AS relatedJoueurId, pa.mj_id AS relatedMjId " +
                     "FROM Personnage p " +
                     "LEFT JOIN Participation pa ON p.id = pa.personnage_id " +
                     "WHERE p.id = ?";
                     
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
                    String imagePath = rs.getString("image_path");
                    
                    int relatedJoueurId = rs.getInt("relatedJoueurId"); 
                    int relatedMjId = rs.getInt("relatedMjId"); 

                    personnage = new Personnage(nom, profession, naissanceDate, Univers.getById(universId), biographieId, relatedJoueurId);
                    personnage.setId(id);
                    personnage.setImage(imagePath);
                    personnage.setRelatedMjId(relatedMjId);
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personnage;
    }

    public Personnage findById(int id, Connection conn) { 
        String sql = "SELECT p.nom, p.profession, p.date_naissance, p.univers_id, p.biogragie_id, p.image_path, " +
                     "pa.joueur_id AS relatedJoueurId, pa.mj_id AS relatedMjId " +
                     "FROM Personnage p " +
                     "LEFT JOIN Participation pa ON p.id = pa.personnage_id " +
                     "WHERE p.id = ?";
                     
        Personnage personnage = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    String nom = rs.getString("nom");
                    String profession = rs.getString("profession");
                    String naissanceDate = rs.getString("date_naissance");
                    int universId = rs.getInt("univers_id");
                    int biographieId = rs.getInt("biogragie_id"); 
                    String imagePath = rs.getString("image_path");
                    
                    int relatedJoueurId = rs.getInt("relatedJoueurId"); 
                    int relatedMjId = rs.getInt("relatedMjId"); 

                    personnage = new Personnage(nom, profession, naissanceDate, Univers.getById(universId), biographieId, relatedJoueurId);
                    personnage.setId(id);
                    personnage.setImage(imagePath);
                    personnage.setRelatedMjId(relatedMjId);
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();

            personnage = null;
        }

        return personnage;
    }

    @Override
    public List<Personnage> findAll() { 
        String sql = "SELECT id, nom, profession, date_naissance, univers_id, biogragie_id, image_path FROM Personnage";
        List<Personnage> personnages = new ArrayList<>();

        // try (Connection conn = SQLiteManager.getConnection();
        //      Statement stmt = conn.createStatement();
        //      ResultSet rs = stmt.executeQuery(sql)) {
            
        //     while (rs.next()) {
        //         int id = rs.getInt("id");
        //         String nom = rs.getString("nom");
        //         String profession = rs.getString("profession");
        //         String naissanceDate = rs.getString("date_naissance");
        //         int universId = rs.getInt("univers_id");
        //         String imageId = rs.getString("image_id");
        //         String biographieId = rs.getString("biographie_id");

        //         Personnage p = new Personnage(nom, profession, naissanceDate, Univers.getById(universId), biographieId);
        //         p.setId(id);
        //         p.setImage(imageId);
        //         personnages.add(p);
        //     }
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        return personnages;
    }

    @Override
    public void update(Personnage p) {
        String sql = "UPDATE Personnage SET nom = ?, profession = ?, date_naissance = ?, univers_id = ?, biographie_id =?, image_path = ? WHERE id = ?";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getProfession());
            stmt.setString(3, p.getNaissanceDate());
            stmt.setInt(4, p.getUnivers().getId());
            stmt.setInt(5, p.getBiographieId());
            stmt.setString(6, p.getImage());
            stmt.setInt(7, p.getId()); 
            
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