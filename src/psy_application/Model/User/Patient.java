package psy_application.Model.User;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import psy_application.Main;
import psy_application.controller.Psy_Frame;
import psy_application.controller.login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient extends User {
    String patient_surname;
    String patient_name;
    String patient_DOB;
    String patient_mailing;
    String patient_how;
    String patient_profession;

    /** GETTER **/

    public String getPatient_surname() {
        return patient_surname;
    }
    public String getPatient_name() {
        return patient_name;
    }
    public String getPatient_DOB() {
        return patient_DOB;
    }
    public String getPatient_mailing() {
        return patient_mailing;
    }
    public String getPatient_how() {
        return patient_how;
    }
    public String getPatient_profession() {
        return patient_profession;
    }

    /** SETTER **/

    public void setPatient_surname(String patient_surname) {
        this.patient_surname = patient_surname;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public void setPatient_DOB(String patient_DOB) {
        this.patient_DOB = patient_DOB;
    }

    public void setPatient_mailing(String patient_mailing) {
        this.patient_mailing = patient_mailing;
    }

    public void setPatient_how(int patient_how) throws SQLException {
        String MyQuery = "SELECT HOW_S FROM HOW WHERE HOW_ID = " + patient_how;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery);
        rset.next();
        this.patient_how = rset.getString(1);
    }

    public void setPatient_profession(int patient_profession) throws SQLException {
        String MyQuery = "SELECT PROFESSION FROM PROFESSIONS WHERE PROF_ID = " + patient_profession;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery);
        rset.next();
        this.patient_profession = rset.getString(1);
    }

    /** CONSTRUCTORS **/

    public Patient(String user_login, String user_password) {
        this.user_login = user_login;
        this.user_password = user_password;
    }

    public Patient(int user_ID, String user_login, String user_password, String patient_surname, String patient_name, String patient_DOB, String patient_mailing, int patient_how, int patient_profession) throws SQLException {
        this.user_login = user_login;
        this.user_password = user_password;
        this.user_ID = user_ID;
        this.patient_surname = patient_surname;
        this.patient_name = patient_name;
        this.patient_DOB = patient_DOB;
        this.patient_mailing = patient_mailing;
        this.patient_how = String.valueOf(patient_how);
        this.patient_profession = String.valueOf(patient_profession);
    }

    public Patient(int user_ID, String patient_surname, String patient_name, String patient_DOB, String patient_mailing, int patient_how, int patient_profession) throws SQLException {
        this.user_ID = user_ID;
        this.patient_surname = patient_surname;
        this.patient_name = patient_name;
        this.patient_DOB = patient_DOB;
        this.patient_mailing = patient_mailing;
        setPatient_how( patient_how);
        setPatient_profession(patient_profession);
    }

    /** METHODS **/

    public static int getMaxID(String table) throws SQLException {
        switch (table) {
            case "USERS":
                String myQuery = "SELECT MAX(User_ID) FROM USERS";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                return rset.getInt(1) + 1;
            case "CONSULTATIONS":
                myQuery = "SELECT MAX(Consul_ID) FROM Consultations";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                return rset.getInt(1) + 1;
            case "PATIENTS":
                myQuery = "SELECT MAX(Patient_ID) FROM Patients";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                return rset.getInt(1) + 1;
            case "PROFESSIONS":
                myQuery = "SELECT MAX(prof_ID) FROM PROFESSIONS";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                return rset.getInt(1) + 1;
            case "HOW":
                myQuery = "SELECT MAX(HOW_ID) FROM HOW";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                return rset.getInt(1) + 1;
            default:
                return 0;
        }
    }

    public static int findProfID(String prof) throws SQLException {
        // Trouvons dans la base de donnée la professions et commment il connait la PSY
        String myQuery3 = "SELECT Prof_ID FROM Professions WHERE profession ='" + prof + "'";
        ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
        if (rset3.next()) {
            System.out.println(" J'ai trouvé une proffesion avec un int de  = " + rset3.getInt(1));
            return rset3.getInt(1);
        } else if (prof.equals("")) {
            return 0;
        } else {
            // CREATION D'UNE PROFESSION ET AJOUT DANS LA DB
            int prof_id = getMaxID("PROFESSIONS");
            myQuery3 = "INSERT INTO PROFESSIONS VALUES ( " + prof_id + ", '" + prof + "')";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
            return prof_id;
        }
    }

    public static int findHowID(String how) throws SQLException {
        String myQuery3 = "SELECT How_ID FROM HOW WHERE  how_s ='" + how + "'";
        ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
        if (rset3.next()) {
            System.out.println(" J'ai trouvé un how avec un int de  = " + rset3.getInt(1));
            return rset3.getInt(1);
        } else if (how.equals("")) {
            return 0;
        } else {
            // CREATION D'UN MOYEN DE CONNAISSANCE ET AJOUT DANS LA DB
            int how_id = getMaxID("HOW");
            myQuery3 = "INSERT INTO HOW VALUES ( " + how_id  + ", '" + how + "')";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
            return how_id;
        }
    }

    private boolean isaValidDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(java.time.LocalDate.now().toString());        //Date d'aujourd'hui
        Date date2 = sdf.parse(this.patient_DOB);   // Date de Naissance
        if (date1.compareTo(date2) > 0) {
            System.out.println("Date1 is after Date2");
            return true;
        } else {
            return false;
        }

    }

    public static boolean patientexist(String mail ) throws SQLException {
        String myQuery = "SELECT user_login FROM USERS WHERE USER_LOGIN ='" + mail + "'";
        ResultSet rset = Main.database.stmt.executeQuery(myQuery);
        if (rset.next()) {
            Psy_Frame.showAlert(" User déjà dans la base de donnée veuillez recommencer !");
            return true;
        }else return false;
    }

    public boolean addinDB() throws ParseException {
       try{
           if ((this.user_login.equals("")) ||
                   (this.user_password.equals("")) ||
                   (this.patient_name.equals("")) ||
                   (this.patient_surname.equals("")) ||
                   !isaValidDate()) {
               return false;
           } else {
               // AJOUT DE L'USER DANS LES DIFFERENTE DATABASE
               String myQuery3 = "INSERT INTO USERS VALUES ( " + this.user_ID + ",'" + this.user_login + "', '" + this.user_password + "')";
               ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
               myQuery3 = "INSERT INTO PATIENTS VALUES ( " + this.user_ID + ",'"
                       + this.patient_surname + "', '"
                       + this.patient_name + "', TO_DATE( '"
                       + this.patient_DOB + "', 'yyyy-MM-dd'), '"
                       + this.patient_mailing + "', '"
                       + this.patient_how + "', '"
                       + this.patient_profession + "'"
                       + ")";
               rset3 = Main.database.stmt.executeQuery(myQuery3);
               return true;
           }
       }catch ( SQLException e){
           Psy_Frame.showAlert("Ajout dans la base de donnée echoué");
           return false;
       }

    }



    @Override
    public String toString() {
        return "Patient{" +
                " user_ID=" + user_ID +
                ", user_login='" + user_login + '\'' +
                ", user_password='" + user_password + '\'' +
                ", patient_surname='" + patient_surname + '\'' +
                ", patient_name='" + patient_name + '\'' +
                ", patient_DOB=" + patient_DOB +
                ", patient_mailing='" + patient_mailing + '\'' +
                ", patient_how=" + patient_how +
                ", patient_profession=" + patient_profession +
                '}';
    }
}
