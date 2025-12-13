package com.gl.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gl.model.Joueur;
import com.gl.model.Partie;
import com.gl.model.Personnage;

public class JoueurDAO implements DAO<Joueur> {

    @Override
    public void save(Joueur entity) {
        String sql = "INSERT INTO Joueur(nom) VALUES(?)";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entity.getPseudo());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Joueur findById(int id) {
        String sql = "SELECT id, nom FROM Joueur WHERE id = ?";
        Joueur joueur = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Reconstruction de l'objet Joueur depuis la BDD
                    String pseudo = rs.getString("nom");
                    
                    joueur = new Joueur(pseudo); 
                    joueur.setId(id);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur FindById : " + e.getMessage());
        }
        return joueur;
    }

    @Override
    public List<Joueur> findAll() {
        List<Joueur> joueurs = new ArrayList<>();
        String sql = "SELECT id, nom FROM Joueur";

        try (Connection conn = SQLiteManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String pseudo = rs.getString("nom");
                
                Joueur j = new Joueur(pseudo);
                j.setId(id);
                
                joueurs.add(j);
            }

        } catch (SQLException e) {
            System.out.println("Erreur FindAll : " + e.getMessage());
        }
        return joueurs;
    }

    @Override
    public void update(Joueur entity) {
        String sql = "UPDATE Joueur SET nom = ? WHERE id = ?";

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getPseudo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur Update : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Joueur WHERE id = ?";

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erreur Delete : " + e.getMessage());
        }
    }
    
    // Méthode spécifique non présente dans l'interface générique DAO
    public Joueur findByName(String name) {
        String sql = "SELECT id, nom FROM Joueur WHERE nom = ? COLLATE NOCASE";
        Joueur joueur = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String pseudo = rs.getString("nom");
                    
                    joueur = new Joueur(pseudo);
                    joueur.setId(id);
                    findPersonnagesByJoueur(joueur);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur FindByName : " + e.getMessage());
        }
        return joueur;
    }

    public List<Personnage> findPersonnagesByJoueur(Joueur joueur) {
        PersonnageDAO personnageDAO = new PersonnageDAO();
        PartieDAO partieDAO = new PartieDAO();
        List<Personnage> personnages = new ArrayList<>();
        String sql = "SELECT  partie_id, personnage_id FROM Participation WHERE joueur_id = ? ";
        
        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, joueur.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    int partieId = rs.getInt("partie_id");
                    Personnage personnage = personnageDAO.findById(rs.getInt("personnage_id"));
                    personnage.setPartie(partieDAO.findById(partieId));
                    personnages.add(personnage);
                }
            } 
        } catch (SQLException e) {
            System.out.println("Erreur FindByName : " + e.getMessage());
        }

        return personnages;
    }
}
