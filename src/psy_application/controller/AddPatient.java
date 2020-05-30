package psy_application.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import psy_application.Main;
import psy_application.Model.User.Patient;
import psy_application.Model.User.Psy;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddPatient implements Initializable {
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
    public Label title;
    @FXML
    private javafx.scene.control.Button closeButton;
    private Patient A;
    public static int patient_id = -1;

    @FXML
    private void addButtonAction() {
        try {
            if (!Patient.patientexist(mail_field.getText())) {
                if (patient_id == -1) {
                    int how = 0, prof = 0;
                    how = Patient.findHowID(how_field.getText());
                    prof = Patient.findProfID(prof_field.getText());
                    try {
                        System.out.println(" How = " + how + ", Prof = " + prof);
                        String strDate = Psy_Frame.convertJDatetoString(dob_field);
                        int user_id = Patient.getMaxID("USERS");
                        A = new Patient(user_id, mail_field.getText(), pass_field.getText(), surname_field.getText(), name_field.getText(), strDate, mailing_field.getText(), String.valueOf(how), String.valueOf(prof));
                        System.out.println(A);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    int how = 0, prof = 0;
                    how = Patient.findHowID(how_field.getText());
                    prof = Patient.findProfID(prof_field.getText());
                    try {
                        A = new Patient(patient_id, mail_field.getText(), A.getUser_password(), surname_field.getText(), name_field.getText(), A.getPatient_DOB(), mailing_field.getText(), how, prof);
                        if (A.addinDB()) {
                            Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                            primaryStage.close();
                            Psy_Frame.showInfo("Ajout du patient avec succès !");
                            patient_id = -1;
                            login.psyStage.show();
                        } else {
                            Psy_Frame.showAlert("Erreur dans la saisie des infos ! \n Veuillez recommencer !");
                        }
                    } catch (NullPointerException e) {
                        Psy_Frame.showAlert("Veuillez remplir les champs obligatoires");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (patient_id == -1) {
                Psy_Frame.showAlert(" User déjà dans la base de donnée veuillez recommencer !");
            } else {
                try {
                    try {
                        if (!prof_field.getText().equals(A.getPatient_profession()))
                            A.setPatient_profession(prof_field.getText());
                        if (!how_field.getText().equals(A.getPatient_how())) A.setPatient_how(how_field.getText());
                        if (!mailing_field.getText().equals(A.getPatient_mailing()))
                            A.setPatient_mailing(mailing_field.getText());
                        String DOB = Psy_Frame.convertJDatetoString(dob_field);
                        if (!DOB.equals(A.getPatient_DOB())) A.setPatient_DOB(DOB);
                    } catch (NullPointerException e) {
                    }
                    if (!mail_field.getText().equals(A.getUser_login())) A.setUser_login(mail_field.getText());
                    if (!name_field.getText().equals(A.getPatient_name())) A.setPatient_name(name_field.getText());
                    if (!surname_field.getText().equals(A.getPatient_surname()))
                        A.setPatient_surname(surname_field.getText());
                    if (!pass_field.getText().equals("*****")) A.setUser_password(pass_field.getText());
                    A.UpdatePatient();
                    Psy_Frame.showInfo("Modification effectué !");
                    patient_id = -1;
                    Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                    primaryStage.close();
                    Psy_Frame.HandlePatient.show();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Psy_Frame.showAlert("Veuillez remplir les champs obligatoires");
                }

            }
        } catch (SQLException E) {
            E.printStackTrace();
        }
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
        if (patient_id != -1) {
            patient_id = -1;
            Psy_Frame.HandlePatient.show();
        } else login.psyStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(patient_id);

        if (patient_id != -1) {
            try {
                A = Patient.getPatientFromDB(patient_id);
            } catch (SQLException e) {
                Psy_Frame.showAlert("Patient pas trouvé");
            }
            A.setPatient_DOB(A.getPatient_DOB().substring(0, 10));
            mail_field.setText(A.getUser_login());
            prof_field.setText(A.getPatient_profession());
            how_field.setText(A.getPatient_how());
            name_field.setText(A.getPatient_name());
            surname_field.setText(A.getPatient_surname());
            pass_field.setText("*****");
            addButton.setText("Modifier");
            title.setText("Modification");
        }
    }
}
