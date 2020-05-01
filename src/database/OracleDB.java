package database;

import sun.plugin2.jvm.RemoteJVMLauncher;

import java.sql.*;

public class OracleDB {

    public Connection conn;
    public Statement stmt;

    public Connection conn2; // Pour permettre deux requetes en même temps
     // Pour permettre deux requetes en même temps
     public Statement stmt2;
     /*
        CallableStatement for all our procedure !
      */

     public CallableStatement RemoveConsulstmt;
     public CallableStatement RemovePatientstmt;


     /*
    *   Pour tous les constructeur de classe la connection suivante à été utilisé
     */
    public Connection conn3;
    public Statement stmt3;





    public OracleDB(String login, String password) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = "jdbc:oracle:thin:@192.168.1.58:1521:XE";
            conn = conn2 = conn3 = DriverManager.getConnection(url, login, password);

            /*  CREATE MULTIPLE STATEMENT TO ENJOY MULTIPLE QUERY ON THE SAME TIME */

            stmt  =  conn.createStatement();
            stmt2 = conn2.createStatement();
            stmt3 = conn3.createStatement();
            stmt2 = conn2.createStatement();

            // INITIALISE PROCEDURE

            linkProcedure();


        } catch (Exception e) {
            System.out.println("Aucune database trouvée");
        }
    }


    private void linkProcedure() throws SQLException {
        RemoveConsulstmt = conn.prepareCall("call REMOVE_CONSUL(?)");
        RemovePatientstmt = conn.prepareCall("call REMOVE_PATIENT(?)");
    }
}
