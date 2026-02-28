package dao;

import entities.TypeAbsence;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeAbsenceDAO {

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/gestion_absences", "root", "");
    }

    public boolean ajouter(TypeAbsence t) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO type_absence (libelle) VALUES (?)")) {
            ps.setString(1, t.getLibelle());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean modifier(TypeAbsence t) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE type_absence SET libelle=? WHERE id_type=?")) {
            ps.setString(1, t.getLibelle());
            ps.setInt(2, t.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean supprimer(int id) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM type_absence WHERE id_type=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<TypeAbsence> listerTous() {
        List<TypeAbsence> liste = new ArrayList<>();
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_type, libelle FROM type_absence")) {
            while (rs.next())
                liste.add(new TypeAbsence(rs.getInt("id_type"), rs.getString("libelle")));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }
}