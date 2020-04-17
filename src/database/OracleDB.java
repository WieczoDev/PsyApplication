package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class OracleDB {

    public Connection conn;
    public Statement stmt;

    public OracleDB(String login, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@192.168.1.58:1521:XE";
            conn = DriverManager.getConnection(url, login, password);
            stmt = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Aucune database trouvée");
        }
    }
}
