package com.gl.persistence;

import com.gl.model.Personnage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO implements DAO<Personnage> {
    @Override
    public void save(Personnage p) {
        try {
            Connection conn = SQLiteManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO personnage(nom, profession, naissanceDate, univers) VALUES (?, ?, ?, ?)");
            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getProfession());
            stmt.setString(3, p.getNaissanceDate());
            stmt.setString(4, p.getUnivers().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    @Override
    public Personnage findById(int id) { 
        return null; 
    }

    @Override
    public List<Personnage> findAll() { 
        return new ArrayList<>(); 
    }

    @Override
    public void update(Personnage p) {

    }

    @Override
    public void delete(int id) {

    }
}
