package com.gl.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gl.model.Biographie;
import com.gl.model.Episode;
import com.gl.model.Paragraphe;

public class ParagrapheDAO implements DAO<Paragraphe> {
    
    @Override
    public void save(Paragraphe p) {
        String sql = "INSERT INTO Paragraphe (titre, contenu, is_private, episode_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, p.getTitre());
            stmt.setString(2, p.getContenu()); 
            stmt.setInt(3, p.isPrivate() ? 1 : 0);
            stmt.setInt(4, p.getEpisodeId());
            
            stmt.executeUpdate();
            
            // Récupérer l'ID généré
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Paragraphe findById(int id) {
        String sql = "SELECT titre, contenu, is_private FROM Paragraphe WHERE id = ?";
        Paragraphe paragraphe = null;
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    String titre = rs.getString("titre");
                    String contenu = rs.getString("contenu");
                    boolean isPrivate = rs.getBoolean("is_private");
                    
                    paragraphe = new Paragraphe(titre, contenu, isPrivate);
                    paragraphe.setId(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paragraphe;
    }

    @Override
    public List<Paragraphe> findAll() {
        String sql = "SELECT id, titre, contenu, is_private FROM Paragraphe";
        List<Paragraphe> paragraphes = new ArrayList<>();
        
        try (Connection conn = SQLiteManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String contenu = rs.getString("contenu");
                boolean isPrivate = rs.getBoolean("is_private");
                
                Paragraphe p = new Paragraphe(titre, contenu, isPrivate);
                // p.setId(id);
                paragraphes.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paragraphes;
    }

    @Override
    public void update(Paragraphe p) {
        String sql = "UPDATE Paragraphe SET titre = ?, contenu = ?, is_private = ? WHERE id = ?";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getTitre());
            stmt.setString(2, p.getContenu());
            stmt.setInt(3, p.isPrivate() ? 1 : 0);
            stmt.setInt(4, p.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Paragraphe WHERE id = ?";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Episode findEpisodeById(int episodeId) {
        String sql1 = "SELECT id, titre, biographie_id, aventure_id FROM Episode WHERE id = ?";
        String sql2 = "SELECT id, titre, contenu, is_private FROM Paragraphe WHERE episode_id = ?";

        Episode episode = null;

        try (Connection conn = SQLiteManager.getConnection();
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
            
            pstmt1.setInt(1, episodeId);
            pstmt2.setInt(1, episodeId);
            
            try (ResultSet rs1 = pstmt1.executeQuery()) {
                if(rs1.next()) {
                    String titre = rs1.getString("titre");
                    int biographieId = rs1.getInt("biographie_id");
                    int aventureId = rs1.getInt("aventure_id"); 
                    
                    episode = new Episode(titre); 
                    episode.setRelatedBiographieId(biographieId);
                    episode.setRelatedAventureId(aventureId);
                } 
            } 

            if (episode != null) {
                try (ResultSet rs2 = pstmt2.executeQuery()) {
                    
                    while (rs2.next()) {
                        int id = rs2.getInt("id");
                        String titre = rs2.getString("titre");
                        String contenu = rs2.getString("contenu");
                        boolean isPrivate = rs2.getInt("is_private") == 1; 
                        
                        Paragraphe paragraphe = new Paragraphe(titre, contenu, isPrivate); 
                        paragraphe.setId(id);
                        
                        episode.ajouterParagraphe(paragraphe);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'extraction de l'épisode: " + e.getMessage());

            episode = null;
        }
        
        return episode;
    }

    public Biographie findBiographieById(int biographieId) {
        
        String sqlBase = "SELECT titre FROM Biographie WHERE id = ?";
        String sqlEpisodes = "SELECT id FROM Episode WHERE biographie_id = ?";
        
        Biographie biographie = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement stmtBase = conn.prepareStatement(sqlBase);
             PreparedStatement stmtEpisodes = conn.prepareStatement(sqlEpisodes)) {
            
            stmtBase.setInt(1, biographieId);
            try (ResultSet rsBase = stmtBase.executeQuery()) {
                if(rsBase.next()) {
                    String titre = rsBase.getString("titre");
                    
                    biographie = new Biographie(titre); 
                    biographie.setId(biographieId);
                } 
            } 
            
            if (biographie != null) {
                stmtEpisodes.setInt(1, biographieId);
                
                try (ResultSet rsEpisodes = stmtEpisodes.executeQuery()) {
                    
                    while (rsEpisodes.next()) {
                        
                        int episodeId = rsEpisodes.getInt("id");
                        Episode episode = findEpisodeById(episodeId);
                        
                        if (episode != null) {
                            biographie.ajouterEpisode(episode);
                        }
                    }
                } 
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'extraction de la biographie: " + e.getMessage());
        }
        
        return biographie;
    }
}