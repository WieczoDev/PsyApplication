package psy_application.controller;

import javafx.application.Application.Parameters.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import psy_application.Consultation;
import psy_application.Main;
import psy_application.User.Patient;
import psy_application.User.Psy;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddPatient {
    @FXML
    public TextField mail_field;
    @FXML
    public DatePicker dob_field;
    @FXML
    public TextField prof_field;
    @FXML
    public TextField how_field;
    @FXML
    public TextField name_field;
    @FXML
    public TextField surname_field;
    @FXML
    public TextField pass_field;
    @FXML
    public TextField mailing_field;
    @FXML
    public Button addButton;
    @FXML
    private javafx.scene.control.Button closeButton;
    private Patient A;

    private int getMaxID(String table) throws SQLException {
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

    @FXML
    private void addButtonAction() throws IOException{
        try {
            String myQuery = "SELECT user_login FROM USERS WHERE USER_LOGIN ='" + mail_field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            if(rset.next()){
                System.out.println("Erreur user déjà dans la base de donnée");
                Psy_Frame.showAlert(" User déjà dans la base de donnée veuillez recommencer !");
            }else{
                int how = 0, prof = 0;
                try {
                    // Trouvons dans la base de donnée la professions et compment il connait la PSY
                    myQuery = "SELECT Prof_ID FROM Professions WHERE profession ='" + prof_field.getText() + "'";
                    rset = Main.database.stmt.executeQuery(myQuery);
                    if (rset.next()) {
                        System.out.println(" J'ai trouvé une proffesion avec un int de  = " + rset.getInt(1));
                        prof = rset.getInt(1);
                    } else if (prof_field.getText().equals("")) {
                        prof = 0;
                    } else {
                        // CREATION D'UNE PROFESSION ET AJOUT DANS LA DB
                        prof = getMaxID("PROFESSIONS");
                        myQuery = "INSERT INTO PROFESSIONS VALUES ( " + prof + ", '" + prof_field.getText() + "')";
                        rset = Main.database.stmt.executeQuery(myQuery);
                    }
                    myQuery = "SELECT How_ID FROM HOW WHERE how_s ='" + how_field.getText() + "'";
                    rset = Main.database.stmt.executeQuery(myQuery);

                    if (rset.next()) {
                        System.out.println(" J'ai trouvé un how avec un int de  = " + rset.getInt(1));
                        how = rset.getInt(1);
                    } else if (how_field.getText().equals("")) {
                        how = 0;
                    } else {
                        // CREATION D'UN HOW ET AJOUT DANS LA DB
                        how = getMaxID("HOW");
                        myQuery = "INSERT INTO HOW VALUES ( " + how + ", '" + how_field.getText() + "')";
                        rset = Main.database.stmt.executeQuery(myQuery);
                    }
                } catch (Exception e) {
                    System.out.println();
                }
                try {
                    System.out.println(" How = " + how + ", Prof = " + prof);
                    String strDate = Psy_Frame.convertJDatetoString(dob_field);
                    int user_id = getMaxID("USERS");
                    A = new Patient(user_id, mail_field.getText(), pass_field.getText(), surname_field.getText(), name_field.getText(), strDate, mailing_field.getText(), how, prof);

                    //VERIFICATION QUE TOUTES LES SAISIES OBLIGATOIRES SONT REMPLIES

                    if ((A.getUser_login().equals("")) || (A.getUser_password().equals("")) || (A.getPatient_name().equals("")) || (A.getPatient_surname().equals(""))) {
                        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                        primaryStage.close();
                        Psy_Frame.showAlert("L'une des informations obligatoire à été oubliée veuillez recommencer");
                        Parent root = FXMLLoader.load(getClass().getResource("../fxml/AddPatient.fxml"));
                        Stage AddPatient = new Stage();
                        AddPatient.setScene(new Scene(root));
                        AddPatient.setTitle("Veuillez recommencer !");
                        AddPatient.show();
                    } else {

                        // AJOUT DE L'USER DANS LES DIFFERENTE DATABASE
                        myQuery = "SELECT * FROM PATIENTS";
                        rset = Main.database.stmt.executeQuery(myQuery);

                        myQuery = "INSERT INTO USERS VALUES ( " + user_id + ",'" + A.getUser_login() + "', '" + A.getUser_password() + "')";
                        rset = Main.database.stmt.executeQuery(myQuery);
                        myQuery = "INSERT INTO PATIENTS VALUES ( " + user_id + ",'"
                                + A.getPatient_surname() + "', '"
                                + A.getPatient_name() + "', TO_DATE( '"
                                + A.getPatient_DOB() + "', 'yyyy-MM-dd'), '"
                                + A.getPatient_mailing() + "', '"
                                + A.getPatient_how() + "', '"
                                + A.getPatient_profession() + "'"
                                + ")";
                        rset = Main.database.stmt.executeQuery(myQuery);
                        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                        primaryStage.close();
                        Psy_Frame.showInfo("Ajout du patient avec succès !");
                    }
                    System.out.println(A);
                } catch (Exception e) {
                    e.printStackTrace();
                    Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                    primaryStage.close();
                    Psy_Frame.showAlert("La date de naissance n'est pas bonne");
                    System.out.println("Erreur dans la création du patient ");
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/AddPatient.fxml"));
                    Stage AddPatient = new Stage();
                    AddPatient.setScene(new Scene(root));
                    AddPatient.setTitle("Veuillez recommencer !");
                    AddPatient.show();
                }
            }
        }catch (SQLException | IOException E){}
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
