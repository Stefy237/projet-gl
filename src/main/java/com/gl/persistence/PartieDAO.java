package com.gl.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.gl.model.Partie;
import com.gl.model.Univers;

public class PartieDAO implements DAO<Partie> {

    @Override
    public int save(Partie entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public Partie findById(int id) {
        // Inclusion des colonnes du modèle et de la DDL
        String sql = "SELECT id, titre, resume, jouee, validee, univers_id FROM Partie WHERE id = ?";
        Partie partie = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    
                    String titre = rs.getString("titre");
                    String resume = rs.getString("resume");
                    
                    boolean dejaJouee = rs.getInt("jouee") == 1;
                    boolean validee = rs.getInt("validee") == 1; 
                    int universId = rs.getInt("univers_id");
                    
                    partie = new Partie(titre, Univers.getById(universId), resume);
                    
                    partie.setId(id);
                    partie.setDejaJouee(dejaJouee);
                    partie.setValidee(validee);
                    
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
