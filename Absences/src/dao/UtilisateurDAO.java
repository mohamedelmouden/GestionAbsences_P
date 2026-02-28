package dao;

import entities.Utilisateur;
import java.sql.*;

public class UtilisateurDAO {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/gestion_absences", "root", "");
    }

    // Vérifier login + mot de passe
    public Utilisateur connecter(String username, String motDePasse) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM utilisateur WHERE username=? AND mot_de_passe=?")) {
            ps.setString(1, username);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("mot_de_passe"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Vérifier si username existe et récupérer l'email
    public Utilisateur trouverParUsername(String username) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM utilisateur WHERE username=?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("mot_de_passe"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Mettre à jour le mot de passe
    public boolean mettreAJourMotDePasse(String username, String nouveauMdp) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE utilisateur SET mot_de_passe=? WHERE username=?")) {
            ps.setString(1, nouveauMdp);
            ps.setString(2, username);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}