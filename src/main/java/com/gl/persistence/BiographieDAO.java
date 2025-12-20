package com.gl.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.gl.model.Biographie;
import com.gl.model.Episode;

public class BiographieDAO implements DAO {

    @Override
    public int save(Object entity) {
        if (!(entity instanceof Biographie)) return -1;
        Biographie biographie = (Biographie) entity;
        
        String sql = "INSERT INTO Biographie (titre) VALUES (?)";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, biographie.getTitre());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        biographie.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur Save Biographie : " + e.getMessage());
        }
        return -1;
    }

    @Override
    public Biographie findById(int id) {
        String sql = "SELECT * FROM Biographie WHERE id = ?";
        Biographie biographie = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    biographie = new Biographie(rs.getString("titre"));
                    biographie.setId(rs.getInt("id"));
                    
                    // Chargement des épisodes liés (Composition)
                    biographie.setEpisodes(findEpisodesByBiographieId(id));
                    
                    // Récupération du personnage lié via la table Personnage
                    biographie.setPersonnageId(findPersonnageIdByBiographieId(id));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur findById Biographie : " + e.getMessage());
        }
        return biographie;
    }

    @Override
    public List<Biographie> findAll() {
        List<Biographie> biographies = new ArrayList<>();
        String sql = "SELECT * FROM Biographie";

        try (Connection conn = SQLiteManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Biographie bio = new Biographie(rs.getString("titre"));
                bio.setId(rs.getInt("id"));
                bio.setEpisodes(findEpisodesByBiographieId(bio.getId()));
                bio.setPersonnageId(findPersonnageIdByBiographieId(bio.getId()));
                biographies.add(bio);
            }
        } catch (SQLException e) {
            System.err.println("Erreur findAll Biographie : " + e.getMessage());
        }
        return biographies;
    }

    @Override
    public void update(Object entity) {
        if (!(entity instanceof Biographie)) return;
        Biographie biographie = (Biographie) entity;

        String sql = "UPDATE Biographie SET titre = ? WHERE id = ?";

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, biographie.getTitre());
            pstmt.setInt(2, biographie.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur Update Biographie : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Biographie WHERE id = ?";
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            // Le "ON DELETE CASCADE" dans le schéma SQL gérera automatiquement 
            // la suppression des Personnages et Episodes liés.
        } catch (SQLException e) {
            System.err.println("Erreur Delete Biographie : " + e.getMessage());
        }
    }

    // --- Méthodes privées pour gérer la composition et les liens ---

    private List<Episode> findEpisodesByBiographieId(int biographieId) {
        List<Episode> episodes = new ArrayList<>();
        // On suppose l'existence d'un EpisodeDAO ou on requête directement ici
        String sql = "SELECT id, titre, joueur_valide, mj_valide, date_creation, aventure_id " +
                     "FROM Episode WHERE biographie_id = ?";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, biographieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Episode ep = new Episode(rs.getString("titre"));
                    ep.setId(rs.getInt("id"));
                    ep.setJoueurValide(rs.getInt("joueur_valide") == 1);
                    ep.setMjValide(rs.getInt("mj_valide") == 1);
                    ep.setDateCreation(rs.getString("date_creation"));
                    ep.setRelatedBiographieId(biographieId);
                    ep.setRelatedAventureId(rs.getInt("aventure_id"));
                    episodes.add(ep);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return episodes;
    }

    private int findPersonnageIdByBiographieId(int biographieId) {
        String sql = "SELECT id FROM Personnage WHERE biographie_id = ?";
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, biographieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}