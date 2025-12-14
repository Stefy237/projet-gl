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
    public int save(Joueur entity) {
        String sql = "INSERT INTO Joueur(nom) VALUES(?)";
        int id = -1;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, entity.getPseudo());
            
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
        
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (id != -1) {
            entity.setId(id);
        } 
        
        return id;
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
                    String pseudo = rs.getString("nom");
                    
                    joueur = new Joueur(pseudo); 
                    joueur.setId(id);

                    List<Personnage> personnages = findPersonnagesByJoueurId(id);
                    List<Partie> partiesMJ = findPartieMjByJoueurId(id);
                    joueur.setPersonnages(personnages);
                    joueur.setPartieMJ(partiesMJ);
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
            pstmt.setInt(2, entity.getId());

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
                    
                    List<Personnage> personnages = findPersonnagesByJoueurId(id);
                    List<Partie> partiesMJ = findPartieMjByJoueurId(id);
                    joueur.setPersonnages(personnages);
                    joueur.setPartieMJ(partiesMJ);
                }
            }
        
        } catch (SQLException e) { 
            System.err.println("ERREUR DB lors de la recherche du joueur '" + name + "'.");
            e.printStackTrace(); 
            
            joueur = null; 
        }
        
        return joueur;
    }

    private List<Personnage> findPersonnagesByJoueurId(int id) {
        PersonnageDAO personnageDAO = new PersonnageDAO();
        List<Personnage> personnages = new ArrayList<>();
        String sql = "SELECT  partie_id, personnage_id FROM Participation WHERE joueur_id = ? ";
        
        try (Connection conn = SQLiteManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    int partieId = rs.getInt("partie_id");
                    Personnage personnage = personnageDAO.findById(rs.getInt("personnage_id"));

                    if (personnage != null) {
                        personnage.setPartieId(partieId);
                        personnages.add(personnage);
                    }
                }
            } 
        } catch (SQLException e) {
            System.out.println("Erreur FindByName : " + e.getMessage());
        }

        return personnages;
    }

    private List<Partie> findPartieMjByJoueurId(int id) {
        PartieDAO partieDAO = new PartieDAO();
        List<Partie> parties = new ArrayList<>();
        String sql = "SELECT  partie_id  FROM Participation WHERE mj_id = ? ";

        try (Connection conn = SQLiteManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    int partieId = rs.getInt("partie_id");
                    Partie partie = partieDAO.findById(partieId);

                    parties.add(partie);
                }
            } 
        } catch (SQLException e) {
            System.out.println("Erreur FindByName : " + e.getMessage());
        }

        return parties;
    }
}
