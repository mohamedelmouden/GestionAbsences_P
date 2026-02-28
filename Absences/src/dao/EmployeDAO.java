package dao;

import entities.Employe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {

    private Connection getConnection() throws SQLException {
        String url  = "jdbc:mysql://localhost:3306/gestion_absences";
        String user = "root";
        String pass = "";
        return DriverManager.getConnection(url, user, pass);
    }

    // ── Ajouter ──────────────────────────────────────────────────────────
    public boolean ajouter(Employe emp) {
        String sql = "INSERT INTO employe (nom, departement, poste) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getNom());
            ps.setString(2, emp.getDepartement());
            ps.setString(3, emp.getPoste());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Modifier ─────────────────────────────────────────────────────────
    public boolean modifier(Employe emp) {
        String sql = "UPDATE employe SET nom=?, departement=?, poste=? WHERE id_employe=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, emp.getNom());
            ps.setString(2, emp.getDepartement());
            ps.setString(3, emp.getPoste());
            ps.setInt(4,    emp.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Supprimer ────────────────────────────────────────────────────────
    public boolean supprimer(int id) {
        String sql = "DELETE FROM employe WHERE id_employe=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Lister tous ──────────────────────────────────────────────────────
    public List<Employe> listerTous() {
        List<Employe> liste = new ArrayList<>();
        String sql = "SELECT id_employe, nom, departement, poste FROM employe";
        try (Connection con = getConnection();
             Statement st  = con.createStatement();
             ResultSet rs  = st.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Employe(
                    rs.getInt("id_employe"),
                    rs.getString("nom"),
                    rs.getString("departement"),
                    rs.getString("poste")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }
}