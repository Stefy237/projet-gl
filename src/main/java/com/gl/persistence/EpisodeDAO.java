package com.gl.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.gl.model.Episode;
import com.gl.model.Paragraphe;

public class EpisodeDAO implements DAO {

    @Override
    public int save(Object entity) {
        if (!(entity instanceof Episode)) return -1;
        Episode episode = (Episode) entity;
        
        String sql = "INSERT INTO Episode (titre, joueur_valide, mj_valide, date_creation, biographie_id, aventure_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = SQLiteManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, episode.getTitre());
            pstmt.setInt(2, episode.isJoueurValide() ? 1 : 0);
            pstmt.setInt(3, episode.isMjValide() ? 1 : 0);
            pstmt.setString(4, episode.getDateCreation());
            pstmt.setInt(5, episode.getRelatedBiographieId());
            pstmt.setInt(6, episode.getRelatedAventureId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        episode.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Episode findById(int id) {
        String sql = "SELECT * FROM Episode WHERE id = ?";
        Episode episode = null;

        try (Connection connection = SQLiteManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    episode = mapResultSetToEpisode(rs);
                    // Chargement des paragraphes associés (Composition)
                    episode.setParagraphes(findParagraphesByEpisodeId(id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return episode;
    }

    @Override
    public List<Episode> findAll() {
        List<Episode> episodes = new ArrayList<>();
        String sql = "SELECT * FROM Episode";

        try (Connection connection = SQLiteManager.getConnection();
            Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Episode ep = mapResultSetToEpisode(rs);
                ep.setParagraphes(findParagraphesByEpisodeId(ep.getId()));
                episodes.add(ep);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return episodes;
    }

    @Override
    public void update(Object entity) {
        if (!(entity instanceof Episode)) return;
        Episode episode = (Episode) entity;

        String sql = "UPDATE Episode SET titre = ?, joueur_valide = ?, mj_valide = ?, date_creation = ?, biographie_id = ?, aventure_id = ? WHERE id = ?";

        try (Connection connection = SQLiteManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, episode.getTitre());
            pstmt.setInt(2, episode.isJoueurValide() ? 1 : 0);
            pstmt.setInt(3, episode.isMjValide() ? 1 : 0);
            pstmt.setString(4, episode.getDateCreation());
            pstmt.setInt(5, episode.getRelatedBiographieId());
            pstmt.setInt(6, episode.getRelatedAventureId());
            pstmt.setInt(7, episode.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Episode WHERE id = ?";
        try (Connection connection = SQLiteManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            // Le "ON DELETE CASCADE" en SQL gérera la suppression des paragraphes automatiquement
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Méthodes utilitaires privées ---

    private Episode mapResultSetToEpisode(ResultSet rs) throws SQLException {
        Episode episode = new Episode(rs.getString("titre"));
        episode.setId(rs.getInt("id"));
        episode.setJoueurValide(rs.getInt("joueur_valide") == 1);
        episode.setMjValide(rs.getInt("mj_valide") == 1);
        episode.setDateCreation(rs.getString("date_creation"));
        episode.setRelatedBiographieId(rs.getInt("biographie_id"));
        episode.setRelatedAventureId(rs.getInt("aventure_id"));
        return episode;
    }

    private List<Paragraphe> findParagraphesByEpisodeId(int episodeId) {
        List<Paragraphe> paragraphes = new ArrayList<>();
        String sql = "SELECT * FROM Paragraphe WHERE episode_id = ?";
        
        try (Connection connection = SQLiteManager.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, episodeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Paragraphe p = new Paragraphe(rs.getString("titre"), rs.getString("contenu"), rs.getInt("is_private") == 1);
                    p.setId(rs.getInt("id"));
                    p.setEpisodeId(episodeId);
                    paragraphes.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paragraphes;
    }
}