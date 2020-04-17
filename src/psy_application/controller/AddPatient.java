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

import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddPatient{
    @FXML public TextField mail_field;
    @FXML public DatePicker dob_field;
    @FXML public TextField prof_field;
    @FXML public TextField how_field;
    @FXML public TextField name_field;
    @FXML public TextField surname_field;
    @FXML public TextField pass_field;
    @FXML public TextField mailing_field;
    @FXML public Button addButton;
    @FXML private javafx.scene.control.Button closeButton;
    private Patient A;

    @FXML
    private void addButtonAction() throws IOException {
        int how=0, prof=0;
        try{
            // Trouvons dans la base de donnée la professions et compment il connait la PSY
            String myQuery = "SELECT Prof_ID FROM Professions WHERE profession ='" + prof_field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            System.out.println("Je suis ici " + rset);
            if(rset.next()){
                System.out.println(" J'ai trouvé une proffesion avec un int de  = " + rset.getInt(1));
                prof = rset.getInt(1);
            }else if (prof_field.getText().equals("") ){
                System.out.println("Je suis ici");
               prof = 0;
            }else{
                // CREATION D'UNE PROFESSION ET AJOUT DANS LA DB
                myQuery = "SELECT MAX(prof_ID) FROM PROFESSIONS";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                prof = rset.getInt(1)+1;
                myQuery = "INSERT INTO PROFESSIONS VALUES ( "+ prof +", '" + prof_field.getText() + "')";
                rset = Main.database.stmt.executeQuery(myQuery);
            }
            myQuery = "SELECT How_ID FROM HOW WHERE how_s ='" + how_field.getText() + "'";
            rset = Main.database.stmt.executeQuery(myQuery);

            if(rset.next()){
                System.out.println(" J'ai trouvé un how avec un int de  = " + rset.getInt(1));
                how = rset.getInt(1);
            }else if (how_field.getText().equals("") ){
                System.out.println("Je suis la");
                how = 0;
            }else{
                // CREATION D'UN HOW ET AJOUT DANS LA DB
                myQuery = "SELECT MAX(HOW_ID) FROM HOW";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                how = rset.getInt(1) +1;
                myQuery = "INSERT INTO HOW VALUES ( "+ how +", '" + how_field.getText() + "')";
                rset = Main.database.stmt.executeQuery(myQuery);
            }
        }catch ( Exception e){
            System.out.println();
        };
        try{
            System.out.println(" How = " + how +  ", Prof = "+ prof);
            Date date =  java.util.Date.from(dob_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String strDate = dateFormat.format(date);
            String myQuery = "SELECT MAX(User_ID) FROM USERS";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            rset.next();
            System.out.println("Nombre de User =" + rset.getInt(1));
            int user_id = rset.getInt(1)+1;
            // TO_DATE('17-06-1987', 'dd-mm-yyyy')
            A = new Patient(user_id, mail_field.getText(), pass_field.getText() ,surname_field.getText(), name_field.getText(), strDate , mailing_field.getText(), how ,prof);

            System.out.println(date + "\n" + strDate + "\n");

            //VERIFICATION QUE TOUTES LES SAISIES OBLIGATOIRES SONT REMPLIES

            if( (A.getUser_login().equals("")) || (A.getUser_password().equals("")) || ( A.getPatient_name().equals("")) || (A.getPatient_surname().equals("")) ){
                Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                primaryStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setContentText("L'une des informations obligatoire à été oubliées veuillez recommencer");
                alert.showAndWait();
                Parent root = FXMLLoader.load(getClass().getResource("../fxml/AddPatient.fxml"));
                Stage AddPatient = new Stage();
                AddPatient.setScene(new Scene(root));
                AddPatient.setTitle("Veuillez recommencer !");
                AddPatient.show();
            }else{

                // AJOUT DE L'USER DANS LES DIFFERENTE DATABASE
                myQuery = "SELECT * FROM PATIENTS";
                rset = Main.database.stmt.executeQuery(myQuery);

                myQuery = "INSERT INTO USERS VALUES ( "+ user_id + ",'" +  A.getUser_login() + "', '" + A.getUser_password() + "')";
                rset = Main.database.stmt.executeQuery(myQuery);
                myQuery = "INSERT INTO PATIENTS VALUES ( "+ user_id + ",'"
                        +  A.getPatient_surname() + "', '"
                        + A.getPatient_name()  + "', TO_DATE( '"
                        +  A.getPatient_DOB() + "', 'dd-mm-yyyy'), '"
                        +  A.getPatient_mailing() + "', '"
                        +  A.getPatient_how() + "', '"
                        +  A.getPatient_profession() + "'"
                        + ")";
                rset = Main.database.stmt.executeQuery(myQuery);
                Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                primaryStage.close();
            }
            System.out.println(A);
        }catch (Exception e){
            e.printStackTrace();
            Stage primaryStage = (Stage) closeButton.getScene().getWindow();
            primaryStage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("ERROR");
            alert.setContentText("La date de naissance n'est pas bonne");
            alert.showAndWait();
            System.out.println("Erreur dans la création du patient ");
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/AddPatient.fxml"));
            Stage AddPatient = new Stage();
            AddPatient.setScene(new Scene(root));
            AddPatient.setTitle("Veuillez recommencer !");
            AddPatient.show();
        }
    }

    @FXML
    private void closeButtonAction(){
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
