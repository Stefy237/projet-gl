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
import com.gl.model.Univers;

public class PartieDAO implements DAO<Partie> {

    @Override
    public int save(Partie entity) {
        String sql = "INSERT INTO Partie(titre, situation_initiale, resume, jouee, lieu, date, validee, mj_id, univers_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int id = -1;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, entity.getTitre());
            pstmt.setString(2, entity.getSituationInitiale());
            pstmt.setString(3, entity.getResume());
            pstmt.setInt(4, entity.isDejaJouee() ? 1 : 0);
            pstmt.setString(5, entity.getLieu());
            pstmt.setString(6, entity.getDate());
            pstmt.setInt(7, entity.isValidee() ? 1 : 0);
            pstmt.setInt(8, entity.getMjId());
            pstmt.setInt(9, entity.getUnivers().getId()); 
            
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
        String sqlPartie = "SELECT id, titre, situation_initiale, resume, jouee, lieu, date, validee, mj_id, univers_id FROM Partie WHERE id = ?";
        Partie partie = null;

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlPartie)) {

            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    
                    String titre = rs.getString("titre");
                    String situationInitiale = rs.getString("situation_initiale");
                    String resume = rs.getString("resume");
                    String lieu = rs.getString("lieu");
                    String date = rs.getString("date");

                    
                    boolean dejaJouee = rs.getInt("jouee") == 1;
                    boolean validee = rs.getInt("validee") == 1; 
                    int universId = rs.getInt("univers_id");
                    int mjId = rs.getInt("mj_id");
                    
                    Univers univers = Univers.getById(universId); 
                    
                    partie = new Partie(titre, univers, situationInitiale, mjId);
                    partie.setResume(resume);
                    partie.setLieu(lieu);
                    partie.setDate(date);
                    
                    partie.setId(id);
                    partie.setDejaJouee(dejaJouee);
                    partie.setValidee(validee);
                    
                    partie.setPersonnages(findPersonnagesByPartieId(id)); 
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
        String sql = "SELECT id, titre, situation_initiale, resume, jouee, lieu, date, validee, mj_id, univers_id FROM Partie";

        try (Connection conn = SQLiteManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String situationInitiale = rs.getString("situation_initiale");
                String lieu = rs.getString("lieu");
                String date = rs.getString("date");
                String resume = rs.getString("resume");
                
                boolean dejaJouee = rs.getInt("jouee") == 1;
                boolean validee = rs.getInt("validee") == 1; 
                int universId = rs.getInt("univers_id");
                int mjId = rs.getInt("mj_id");
                
                Univers univers = Univers.getById(universId);
                
                Partie p = new Partie(titre, univers, situationInitiale, mjId);
                p.setId(id);
                p.setDejaJouee(dejaJouee);
                p.setValidee(validee);
                p.setDate(date);
                p.setLieu(lieu);
                p.setResume(resume);

                p.setPersonnages(findPersonnagesByPartieId(id));

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
        String sql = "UPDATE Partie SET titre = ?, resume = ?, validee = ?, jouee = ?, lieu = ?, date = ?, situation_initiale = ?, univers_id = ? WHERE id = ?";

        try (Connection conn = SQLiteManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getTitre());
            pstmt.setString(2,  entity.getResume());
            pstmt.setInt(3, entity.isValidee() ? 1 : 0);
            pstmt.setInt(4, entity.isDejaJouee() ? 1 : 0);
            pstmt.setString(5, entity.getLieu());
            pstmt.setString(6, entity.getDate());
            pstmt.setString(7, entity.getSituationInitiale());
            pstmt.setInt(8, entity.getUnivers().getId());
            pstmt.setInt(9, entity.getId());

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

    /**
     * Récupère tous les Personnages participant à cette Partie.
     * Le PersonnageDAO est utilisé pour charger chaque objet Personnage.
     * Note : Ceci charge les Personnages joués, non le MJ.
     */
    private List<Personnage> findPersonnagesByPartieId(int partieId) {
        // Jointure pour trouver les IDs Personnage joués dans cette Partie.
        String sql = "SELECT DISTINCT personnage_id FROM Participation WHERE partie_id = ? AND personnage_id IS NOT NULL";
        List<Personnage> personnages = new ArrayList<>();
        
        // Instanciation du DAO nécessaire pour éviter le couplage direct aux modèles
        PersonnageDAO personnageDAO = new PersonnageDAO(); 

        try (Connection conn = SQLiteManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, partieId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    int personnageId = rs.getInt("personnage_id");
                    // Charge l'objet Personnage complet
                    Personnage personnage = personnageDAO.findById(personnageId); 

                    if (personnage != null) {
                        personnages.add(personnage);
                    }
                }
            } 
        } catch (SQLException e) {
            System.err.println("Erreur chargement Personnages par Partie ID : " + e.getMessage());
            e.printStackTrace();
        }
        return personnages;
    }


    /**
     * Récupère le ou les Joueurs Maîtres du Jeu (MJ) pour cette Partie.
     * Le JoueurDAO est utilisé pour charger chaque objet Joueur.
     */
    // private List<Joueur> findMjsByPartieId(int partieId) {
    //     // Jointure pour trouver les IDs Joueur (MJ) pour cette Partie.
    //     // Utilise mj_id car un MJ est toujours un Joueur.
    //     String sql = "SELECT DISTINCT mj_id FROM Participation WHERE partie_id = ? AND mj_id IS NOT NULL";
    //     List<Joueur> mjs = new ArrayList<>();
        
    //     // Instanciation du DAO nécessaire
    //     JoueurDAO joueurDAO = new JoueurDAO(); 

    //     try (Connection conn = SQLiteManager.getConnection();
    //         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
    //         pstmt.setInt(1, partieId);

    //         try (ResultSet rs = pstmt.executeQuery()) {
    //             while(rs.next()) {
    //                 int mjId = rs.getInt("mj_id");
    //                 // Charge l'objet Joueur (MJ) complet
    //                 Joueur mj = joueurDAO.findById(mjId); 

    //                 if (mj != null) {
    //                     mjs.add(mj);
    //                 }
    //             }
    //         } 
    //     } catch (SQLException e) {
    //         System.err.println("Erreur chargement MJs par Partie ID : " + e.getMessage());
    //         e.printStackTrace();
    //     }
    //     return mjs;
    // }
}