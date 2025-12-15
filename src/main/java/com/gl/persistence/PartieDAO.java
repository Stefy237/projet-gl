package com.gl.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gl.model.Partie;
import com.gl.model.Univers;

public class PartieDAO implements DAO<Partie> {

    @Override
    public int save(Partie entity) {
        String sql = "INSERT INTO Partie(titre, resume, validee, jouee, univers_id) VALUES(?, ?, ?, ?, ?)";
        int id = -1;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, entity.getTitre());
            pstmt.setString(2, entity.getResume());
            pstmt.setInt(3, entity.isValidee() ? 1 : 0);
            pstmt.setInt(4, entity.isDejaJouee() ? 1 : 0);
            pstmt.setInt(5, entity.getUnivers().getId()); 
            
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                    entity.setId(id);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur Save Partie : " + e.getMessage());
            e.printStackTrace();
        }
        
        return id;
    }

    @Override
    public Partie findById(int id) {
        String sqlPartie = "SELECT id, titre, resume, jouee, validee, univers_id FROM Partie WHERE id = ?";
        Partie partie = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlPartie)) {

            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    
                    String titre = rs.getString("titre");
                    String resume = rs.getString("resume");
                    
                    boolean dejaJouee = rs.getInt("jouee") == 1;
                    boolean validee = rs.getInt("validee") == 1; 
                    int universId = rs.getInt("univers_id");
                    
                    Univers univers = Univers.getById(universId); 
                    
                    partie = new Partie(titre, univers, resume);
                    
                    partie.setId(id);
                    partie.setDejaJouee(dejaJouee);
                    partie.setValidee(validee);
                    
                    // 🚨 CHARGEMENT DES RELATIONS (si nécessaire, nécessiterait ParticipationDAO)
                    // partie.setPersonnages(findPersonnagesByPartieId(id));
                    // partie.setMjs(findMjsByPartieId(id)); 
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur FindById Partie : " + e.getMessage());
            e.printStackTrace();
            partie = null;
        }
        return partie;
    }

    @Override
    public List<Partie> findAll() {
        List<Partie> parties = new ArrayList<>();
        String sql = "SELECT id, titre, resume, jouee, validee, univers_id FROM Partie";

        try (Connection conn = SQLiteManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String resume = rs.getString("resume");
                
                boolean dejaJouee = rs.getInt("jouee") == 1;
                boolean validee = rs.getInt("validee") == 1; 
                int universId = rs.getInt("univers_id");
                
                Univers univers = Univers.getById(universId);
                
                Partie p = new Partie(titre, univers, resume);
                p.setId(id);
                p.setDejaJouee(dejaJouee);
                p.setValidee(validee);
                
                parties.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur FindAll Partie : " + e.getMessage());
            e.printStackTrace();
        }
        return parties;
    }

    @Override
    public void update(Partie entity) {
        String sql = "UPDATE Partie SET titre = ?, resume = ?, validee = ?, jouee = ?, univers_id = ? WHERE id = ?";

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getTitre());
            pstmt.setString(2, entity.getResume());
            pstmt.setInt(3, entity.isValidee() ? 1 : 0);
            pstmt.setInt(4, entity.isDejaJouee() ? 1 : 0);
            pstmt.setInt(5, entity.getUnivers().getId());
            pstmt.setInt(6, entity.getId());

            pstmt.executeUpdate();

            // 🚨 Cohérence : Si la relation Univers a changé, c'est géré ici.
            // Si les personnages ou MJ changent, cette logique doit être gérée par le PersonnageDAO/JoueurDAO 
            // ou une méthode dédiée pour Participation, en cascade depuis le Service Métier.

        } catch (SQLException e) {
            System.err.println("Erreur Update Partie : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        // La suppression en cascade est gérée par la base de données (DDL)
        // pour les tables Episode et Participation qui pointent vers Partie.
        
        String sql = "DELETE FROM Partie WHERE id = ?";

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur Delete Partie : " + e.getMessage());
            e.printStackTrace();
        }
    }
}