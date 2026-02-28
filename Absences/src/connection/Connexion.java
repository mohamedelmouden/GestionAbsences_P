package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    private static final String URL = "jdbc:mysql://localhost:3306/gestion_absences";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection cn = null;

    public static Connection getConnection() {
        try {
            if (cn == null || cn.isClosed()) {
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                cn = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver non trouvé : " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
        return cn;
    }
}