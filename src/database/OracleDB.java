package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class OracleDB {

    public Connection conn;
    public Statement stmt;
    public Connection conn2; // Pour permettre deux requetes en même temps
    public Statement stmt2; // Pour permettre deux requetes en même temps

    public OracleDB(String login, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@192.168.1.58:1521:XE";
            conn = conn2 = DriverManager.getConnection(url, login, password);
            stmt  =  conn.createStatement();
            stmt2 = conn2.createStatement();
        } catch (Exception e) {
            System.out.println("Aucune database trouvée");
        }
    }
}
