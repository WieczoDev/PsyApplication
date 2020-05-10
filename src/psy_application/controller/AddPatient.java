package psy_application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import psy_application.Main;
import psy_application.Model.User.Patient;
import psy_application.Model.User.Psy;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @FXML
    private void addButtonAction() {
        try {
            if (!Patient.patientexist(mail_field.getText())) {
                int how = 0, prof = 0;
                how = Patient.findHowID(how_field.getText());
                prof = Patient.findProfID(prof_field.getText());
                try {
                    System.out.println(" How = " + how + ", Prof = " + prof);
                    String strDate = Psy_Frame.convertJDatetoString(dob_field);
                    int user_id = Patient.getMaxID("USERS");
                    A = new Patient(user_id, mail_field.getText(), pass_field.getText(), surname_field.getText(), name_field.getText(), strDate, mailing_field.getText(), how, prof);
                    if (A.addinDB()) {
                        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                        primaryStage.close();
                        Psy_Frame.showInfo("Ajout du patient avec succès !");
                        login.psyStage.show();
                    } else {
                        Psy_Frame.showAlert("Erreur dans la saisie des infos ! \n Veuillez recommencer !");
                    }
                } catch (NullPointerException e) {
                    Psy_Frame.showAlert("Veuillez remplir les champs obligatoires");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException E) {
        }
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
        login.psyStage.show();
    }
}
