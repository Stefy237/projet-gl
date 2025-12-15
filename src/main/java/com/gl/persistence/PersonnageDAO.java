package com.gl.persistence;

import com.gl.model.Personnage;
import com.gl.model.Univers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO implements DAO<Personnage> {
    
    @Override
    public int save(Personnage p) {
        String sqlPersonnage = "INSERT INTO Personnage (nom, profession, date_naissance, image_path, univers_id, biographie_id) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlParticipation = "INSERT INTO Participation (joueur_id, mj_id, partie_id, personnage_id) VALUES (?, ?, ?, ?)";
        int id = -1;
        
        try (Connection conn = SQLiteManager.getConnection();
            PreparedStatement pstmtP = conn.prepareStatement(sqlPersonnage, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pstmtA = conn.prepareStatement(sqlParticipation)) {

            pstmtP.setString(1, p.getNom());
            pstmtP.setString(2, p.getProfession());
            pstmtP.setString(3, p.getNaissanceDate());
            pstmtP.setString(4, p.getImage());
            pstmtP.setInt(5, p.getUnivers().getId());
            pstmtP.setInt(6, p.getBiographieId());
            pstmtP.executeUpdate();

            try (ResultSet rs = pstmtP.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                    p.setId(id);
                }
            }
            
            if (id != -1 && p.getRelatedJoueurId() > 0) {
                // 2. 🚨 Sauvegarde de la Participation (Joueur)
                pstmtA.setInt(1, p.getRelatedJoueurId());
                pstmtA.setInt(2, p.getRelatedMjId()); 
                pstmtA.setInt(3, p.getPartieId()); 
                pstmtA.setInt(4, id); 
                pstmtA.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public Personnage findById(int id) { 
        // NOTE: Nous utilisons LEFT JOIN pour récupérer les IDs du Joueur et du MJ 
        // depuis la table Participation.
        String sql = "SELECT p.nom, p.profession, p.date_naissance, p.image_path, p.univers_id, p.biographie_id,  " +
                     "pa.joueur_id , pa.mj_id, partie_id  " +
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
                    int biographieId = rs.getInt("biographie_id"); 
                    String imagePath = rs.getString("image_path");
                    
                    int relatedJoueurId = rs.getInt("joueur_id"); 
                    int relatedMjId = rs.getInt("mj_id"); 
                    int partieId= rs.getInt("partie_id"); 

                    personnage = new Personnage(nom, profession, naissanceDate, Univers.getById(universId), biographieId, relatedJoueurId);
                    personnage.setId(id);
                    personnage.setImage(imagePath);
                    personnage.setRelatedMjId(relatedMjId);
                    personnage.setPartieId(partieId);
                }
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return personnage;
    }

    @Override
    public List<Personnage> findAll() { 
        // 🚨 CORRECTION : Retirer la clause WHERE, inclure tous les champs pertinents.
        // NOTE: Utiliser GROUP BY pour éviter les doublons si un Personnage est lié à plusieurs Partages.
        String sql = """
                        SELECT p.id, p.nom, p.profession, p.date_naissance, p.univers_id, p.biographie_id, p.image_path,
                            pa.joueur_id, pa.mj_id, pa.partie_id
                        FROM Personnage p
                        LEFT JOIN Participation pa ON p.id = pa.personnage_id
                    """; 
        List<Personnage> personnages = new ArrayList<>();
        
        // 🚨 ATTENTION : La jointure LEFT JOIN dans findAll peut renvoyer plusieurs lignes par Personnage 
        // si Participation est mal gérée. La DAO devrait charger l'entité Personnage complète.

        try (Connection conn = SQLiteManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String profession = rs.getString("profession");
                String naissanceDate = rs.getString("date_naissance");
                int universId = rs.getInt("univers_id");
                String imagePath = rs.getString("image_path");
                int biographieId = rs.getInt("biographie_id");

                int relatedJoueurId = rs.getInt("joueur_id"); 
                int relatedMjId = rs.getInt("mj_id"); 
                int partieId= rs.getInt("partie_id"); 

                Personnage p = new Personnage(nom, profession, naissanceDate, Univers.getById(universId), biographieId, relatedJoueurId);
                p.setId(id);
                p.setImage(imagePath);
                p.setRelatedMjId(relatedMjId);
                p.setPartieId(partieId);
                personnages.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personnages;
    }

    @Override
    public void update(Personnage p) {
        String sqlP = "UPDATE Personnage SET nom = ?, profession = ?, date_naissance = ?, univers_id = ?, image_path = ? WHERE id = ?";
    
        // 🚨 La Participation est gérée séparément : soit UPDATE, soit DELETE/INSERT.
        // L'UPDATE est plus propre, mais doit gérer la PK composée.
        String sqlUpdateParticipation = "UPDATE Participation SET joueur_id = ?, mj_id = ?, partie_id = ? WHERE personnage_id = ?";
        String sqlDeleteParticipation = "DELETE FROM Participation WHERE personnage_id = ?";
        String sqlInsertParticipation = "INSERT INTO Participation (joueur_id, mj_id, partie_id, personnage_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = SQLiteManager.getConnection()) {
            
            // 1. Mise à jour de l'entité Personnage
            try (PreparedStatement stmtP = conn.prepareStatement(sqlP)) {
                stmtP.setString(1, p.getNom());
                stmtP.setString(2, p.getProfession());
                stmtP.setString(3, p.getNaissanceDate());
                stmtP.setInt(4, p.getUnivers().getId());
                stmtP.setString(5, p.getImage());
                stmtP.setInt(6, p.getId()); 
                stmtP.executeUpdate();
            }

            // 2. 🚨 Gestion de la Cohérence dans Participation
            try (PreparedStatement stmtU = conn.prepareStatement(sqlUpdateParticipation)) {
                stmtU.setInt(1, p.getRelatedJoueurId());
                stmtU.setInt(2, p.getRelatedMjId()); 
                stmtU.setInt(3, p.getPartieId()); 
                stmtU.setInt(4, p.getId());
                
                int rowsAffected = stmtU.executeUpdate();
                
                // Si aucune ligne n'a été mise à jour (l'entrée n'existait pas), on l'insère
                if (rowsAffected == 0 && p.getRelatedJoueurId() > 0) {
                    try (PreparedStatement stmtI = conn.prepareStatement(sqlInsertParticipation)) {
                        stmtI.setInt(1, p.getRelatedJoueurId());
                        stmtI.setInt(2, p.getRelatedMjId()); 
                        stmtI.setInt(3, p.getPartieId()); 
                        stmtI.setInt(4, p.getId());
                        stmtI.executeUpdate();
                    }
                }
            } 
            
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