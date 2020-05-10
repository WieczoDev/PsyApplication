package psy_application.Model.User;

import psy_application.Main;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Patient extends User {
    String patient_surname;
    String patient_name;
    String patient_DOB;
    String patient_mailing;
    String patient_how;
    String patient_profession;

    public String getPatient_surname() {
        return patient_surname;
    }

    public void setPatient_surname(String patient_surname) {
        this.patient_surname = patient_surname;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_DOB() {
        return patient_DOB;
    }

    public void setPatient_DOB(String patient_DOB) {
        this.patient_DOB = patient_DOB;
    }

    public String getPatient_mailing() {
        return patient_mailing;
    }

    public void setPatient_mailing(String patient_mailing) {
        this.patient_mailing = patient_mailing;
    }

    public String getPatient_how() {
        return patient_how;
    }

    public void setPatient_how(int patient_how) throws SQLException {
        String MyQuery = "SELECT HOW_S FROM HOW WHERE HOW_ID = " + patient_how;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery);
        rset.next();
        this.patient_how = rset.getString(1);
    }

    public String getPatient_profession() {
        return patient_profession;
    }

    public void setPatient_profession(int patient_profession) throws SQLException {
        String MyQuery = "SELECT PROFESSION FROM PROFESSIONS WHERE PROF_ID = " + patient_profession;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery);
        rset.next();
        this.patient_profession = rset.getString(1);
    }

    public Patient(String user_login, String user_password) {
        this.user_login = user_login;
        this.user_password = user_password;
        isFilled = false;
        this.user_ID = count.incrementAndGet();
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
