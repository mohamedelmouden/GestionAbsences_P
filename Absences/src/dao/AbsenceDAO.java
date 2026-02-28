package dao;

import entities.Absence;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDAO {

    private Connection getConnection() throws SQLException {
        String url  = "jdbc:mysql://localhost:3306/gestion_absences";
        String user = "root";
        String pass = "";
        return DriverManager.getConnection(url, user, pass);
    }

    // ── Requête SELECT de base (JOIN employe + type_absence) ──────────────
    // CHANGEMENT : on sélectionne e.departement, e.poste  au lieu de e.prenom, e.email
    private static final String SELECT_ALL =
        "SELECT a.id_absence, a.date_debut, a.date_fin, " +
        "       a.id_employe, a.id_type, " +
        "       e.nom AS nom_employe, " +
        "       e.departement AS dep_employe, " +   // ← CHANGÉ
        "       e.poste AS poste_employe, " +         // ← CHANGÉ
        "       t.libelle AS libelle_type, " +
        "       DATEDIFF(a.date_fin, a.date_debut) + 1 AS nb_jours " +
        "FROM absence a " +
        "LEFT JOIN employe      e ON a.id_employe = e.id_employe " +
        "LEFT JOIN type_absence t ON a.id_type    = t.id_type";

    // ── Mapper un ResultSet vers un objet Absence ─────────────────────────
    private Absence map(ResultSet rs) throws SQLException {
        Absence ab = new Absence(
            rs.getInt("id_absence"),
            rs.getDate("date_debut"),
            rs.getDate("date_fin"),
            rs.getInt("id_employe"),
            rs.getInt("id_type")
        );
        ab.setNomEmploye(rs.getString("nom_employe"));
        ab.setDepartementEmploye(rs.getString("dep_employe"));    // ← CHANGÉ
        ab.setPosteEmploye(rs.getString("poste_employe"));         // ← CHANGÉ
        ab.setLibelleType(rs.getString("libelle_type"));
        ab.setNbJours(rs.getInt("nb_jours"));
        return ab;
    }

    // ── Ajouter ───────────────────────────────────────────────────────────
    public boolean ajouter(Absence ab) {
        String sql = "INSERT INTO absence (date_debut, date_fin, id_employe, id_type) VALUES (?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ab.getDateDebut().getTime()));
            ps.setDate(2, new java.sql.Date(ab.getDateFin().getTime()));
            ps.setInt(3, ab.getIdEmploye());
            ps.setInt(4, ab.getIdType());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Modifier ─────────────────────────────────────────────────────────
    public boolean modifier(Absence ab) {
        String sql = "UPDATE absence SET date_debut=?, date_fin=?, id_employe=?, id_type=? WHERE id_absence=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(ab.getDateDebut().getTime()));
            ps.setDate(2, new java.sql.Date(ab.getDateFin().getTime()));
            ps.setInt(3, ab.getIdEmploye());
            ps.setInt(4, ab.getIdType());
            ps.setInt(5, ab.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Supprimer ────────────────────────────────────────────────────────
    public boolean supprimer(int id) {
        String sql = "DELETE FROM absence WHERE id_absence=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── Lister toutes ────────────────────────────────────────────────────
    public List<Absence> listerTous() {
        List<Absence> liste = new ArrayList<>();
        try (Connection con = getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(SELECT_ALL + " ORDER BY a.date_debut DESC")) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // ── Filtrer par période et/ou type et/ou département ─────────────────
    public List<Absence> listerAvecFiltres(String dateDebut, String dateFin,
                                            Integer idType, String departement) {
        List<Absence> liste = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SELECT_ALL + " WHERE 1=1");
        if (dateDebut    != null) sql.append(" AND a.date_debut >= '").append(dateDebut).append("'");
        if (dateFin      != null) sql.append(" AND a.date_fin   <= '").append(dateFin).append("'");
        if (idType       != null) sql.append(" AND a.id_type = ").append(idType);
        if (departement  != null && !departement.isEmpty())
            sql.append(" AND e.departement = '").append(departement.replace("'","''")).append("'");
        sql.append(" ORDER BY a.date_debut DESC");
        try (Connection con = getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(sql.toString())) {
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // ── Absences simultanées à une date donnée ────────────────────────────
    public List<Absence> absencesSimultanees(String date) {
        String sql = SELECT_ALL + " WHERE ? BETWEEN a.date_debut AND a.date_fin ORDER BY e.nom";
        List<Absence> liste = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) liste.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // ── Statistiques : jours par employé ─────────────────────────────────
    public List<Object[]> joursParEmploye() {
        List<Object[]> liste = new ArrayList<>();
        String sql = "SELECT e.nom, SUM(DATEDIFF(a.date_fin, a.date_debut)+1) AS total " +
                     "FROM absence a JOIN employe e ON a.id_employe = e.id_employe " +
                     "GROUP BY e.id_employe, e.nom ORDER BY total DESC";
        try (Connection con = getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {
            while (rs.next()) liste.add(new Object[]{ rs.getString(1), rs.getInt(2) });
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }

    // ── Statistiques : jours par mois ────────────────────────────────────
    public List<Object[]> joursParMois() {
        List<Object[]> liste = new ArrayList<>();
        String sql = "SELECT DATE_FORMAT(a.date_debut,'%Y-%m') AS mois, " +
                     "       SUM(DATEDIFF(a.date_fin, a.date_debut)+1) AS total " +
                     "FROM absence a GROUP BY mois ORDER BY mois";
        try (Connection con = getConnection();
             Statement st   = con.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {
            while (rs.next()) liste.add(new Object[]{ rs.getString(1), rs.getInt(2) });
        } catch (SQLException e) { e.printStackTrace(); }
        return liste;
    }
}