package psy_application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import psy_application.Model.User.Patient;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

    /**
     * PAGE D'AJOUT DE PATIENT MAIS AUSSI DE MOFICATION DES PATIENTS DEJA EXISTANTS SI OUVERTE VIA LE BUTTON MODIFIER LE
     * PATIENT
     * <p>
     * SI L'ON OUVRE VIA ADD -> PATIENT_ID = -1
     * SINON = L'ID DU PATIENT QU'ON VA MODIFIER
     */

    @FXML
    private void addButtonAction() {
        try {
            if (!Patient.patientexist(mail_field.getText()) && patient_id == -1) { // On verifie que le patient n'existe pas deja
                int how = 0, prof = 0;
                /** ON CHERCHE LES CLé PRIMAIRE ASSOCIé AU INPUTS **/
                how = Patient.findHowID(how_field.getText());
                prof = Patient.findProfID(prof_field.getText());
                try {
                    String strDate = Psy_Frame.convertJDatetoString(dob_field);

                    // Comme l'implémentation d'une auto incrémentation sur SQL PLUS n'était pas intuitive
                    // nous avons fait une fonction qui s'assure de cela

                    int user_id = Patient.getMaxID("USERS");
                    A = new Patient(user_id, mail_field.getText(), pass_field.getText(), surname_field.getText(), name_field.getText(), strDate, mailing_field.getText(), String.valueOf(how), String.valueOf(prof));
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
                    /*
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
                }*/
            } else if (Patient.patientexist(mail_field.getText()) && patient_id == -1) {
                Psy_Frame.showAlert(" User déjà dans la base de donnée veuillez recommencer !");
            } else if (patient_id != -1) {

                /** OUVERTURE DE LA FENETRE POUR LA MODIFICATION **/

                try {
                    try {
                        if (!prof_field.getText().equals(A.getPatient_profession()))
                            A.setPatient_profession(prof_field.getText());
                    } catch (NullPointerException e) {
                        // Ce sont des champs qui peuvent être vide donc
                    }
                    try {
                        if (!how_field.getText().equals(A.getPatient_how()))
                            A.setPatient_how(how_field.getText());
                    } catch (NullPointerException e) {
                        // Ce sont des champs qui peuvent être vide donc
                    }
                    try {
                        if (!mailing_field.getText().equals(A.getPatient_mailing()))
                            A.setPatient_mailing(mailing_field.getText());
                    } catch (NullPointerException e) {
                        // Ce sont des champs qui peuvent être vide donc
                    }
                    // VERIFICATION DE LA DATE DE NAISSANCE
                    try {
                        String DOB = Psy_Frame.convertJDatetoString(dob_field);
                        if (!DOB.equals(A.getPatient_DOB().substring(0, 10)))
                            A.setPatient_DOB(DOB);
                    } catch (NullPointerException e) {
                        // Ce sont des champs qui peuvent être vide donc
                    }
                    if (!mail_field.getText().equals(A.getUser_login())) A.setUser_login(mail_field.getText());
                    if (!name_field.getText().equals(A.getPatient_name())) A.setPatient_name(name_field.getText());
                    if (!surname_field.getText().equals(A.getPatient_surname()))
                        A.setPatient_surname(surname_field.getText());
                    if (!pass_field.getText().equals("*****") && !pass_field.getText().equals(""))
                        A.setUser_password(pass_field.getText());
                    A.UpdatePatient();
                    try {
                        closeButtonAction();
                    } catch (IOException e) {
                    }
                } catch (NullPointerException e) {
                    // Les champs sont obligatoires donc s'il sont vide on stop l'opération
                    Psy_Frame.showAlert("Veuillez remplir les champs obligatoires");
                }
            }
        } catch (SQLException E) {
            System.out.println("Erreur dans l'ajout dans la base de donnée ( ADD PATIENT ) ");
        }
    }

    @FXML
    private void closeButtonAction() throws IOException {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
        /** SOIT ON OUVRE HANDLEPATIENT (ON ETAIT EN MODIFICATION )
         *  SOIT ON OUVRE LA PAGE D'ACCEUIL ( ON ETAIT EN AJOUT )
         */

        if (patient_id != -1) {
            patient_id = -1;
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/HandlePatient.fxml"));
            Psy_Frame.HandlePatient = new Stage();
            Psy_Frame.HandlePatient.setScene(new Scene(root));
            Psy_Frame.HandlePatient.initStyle(StageStyle.UNDECORATED);
            Psy_Frame.HandlePatient.show();
        } else {
            login.psyStage.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /** ON PRE REMPLIS LES CHAMPS SI ON EST EN MODIFICATION */

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
