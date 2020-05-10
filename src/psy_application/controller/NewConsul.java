package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import psy_application.Main;
import psy_application.Model.Consultation;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class NewConsul implements Initializable {
    @FXML
    public Button Step1Button;
    @FXML
    public Button Step2Button1;
    @FXML
    public Label Step1Label;
    @FXML
    public Label Step2Label;
    @FXML
    public Label patient1label;
    @FXML
    public Label patient2label;
    @FXML
    public Label patient3label;
    @FXML
    public Button CheckDateButton;
    @FXML
    public Label datelabel;

    @FXML
    private javafx.scene.control.Button closeButton;

    @FXML
    public ChoiceBox heure_Box;
    @FXML
    public TextField reason_field;
    @FXML
    public DatePicker date_field;
    @FXML
    public TextField patient1field;
    @FXML
    public TextField patient2field;
    @FXML
    public TextField patient3field;

    private boolean verifyStep1 = false;
    private boolean verifyStep2 = false;
    private boolean verifyDate = false;

    //On creer des variables locales qui permettront de creer le rdv
    private String heure;
    private String date;
    private int reason;
    private int Patient1 = 0;
    private int Patient2 = 0;
    private int Patient3 = 0;
    String[] List = new String[]{"8h", "8h30", "9h", "9h30", "10h", "10h30", "11h", "11h30", "12h", "12h30", "13h", "13h30",
            "14h", "14h30", "15h", "15h30", "16h", "16h30", "17h", "17h30", "18h", "18h30", "19h", "19h30"};
    ArrayList<String> ListofHour = new ArrayList<String>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private boolean isSunday(Date Date) {
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        if (c.get(c.DAY_OF_WEEK) == 1) {
            return true;
        } else return false;
    }

    private int getMaxID(String table) throws SQLException {
        switch (table) {
            case "CONSULTATIONS":
                String myQuery = "SELECT MAX(Consul_ID) FROM Consultations";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                return rset.getInt(1) + 1;
            case "PATIENTS":
                myQuery = "SELECT MAX(Patient_ID) FROM Patients";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                return rset.getInt(1) + 1;
            default:
                return 0;
        }
    }

    // LA SAISIE D'UNE NOUVELLE CONSULTATION CE FAIT EN 2 ETAPES
    // LA PREMIERE ETAPE CONTIENT UNE VERIFICATION DE LA DATE ET UNE MISE A JOUR DES CRENEAUX HORRAIRES DISPONIBLES
        // PUIS LA SELECTION DE L'HORRAIRE ET (FALCULTATIF) LA SAISIE DE LA RAISON
    // LA SECONDE ETAPE CORRESPOND A LA SAISIE DES PATIENTS VIA ID OU PAR EMAIL

    @FXML
    private void CheckDateButtonAction() throws SQLException {
        try{
            ListofHour.clear();
            Collections.addAll(ListofHour, List);
            date = Psy_Frame.convertJDatetoString(date_field);
            verifyDate = false;
            String myQuery = "SELECT consul_hour FROM Consultations WHERE consul_date = TO_DATE('" + date + "', 'yyyy-MM-dd')";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            while (rset.next()) {
                String str = rset.getString(1);
                switch (str.length()){   //8 , 10 , 8.5 , 10.5
                    case 1:
                    case 2:
                        str += "h";
                        break;
                    case 3:
                        str = str.substring(0, 1) + "h30";
                        break;
                    case 4 :
                        str = str.substring(0, 2) + "h30";
                        break;
                }
                ListofHour.remove(str);
            }
            if ((ListofHour.size() < 5) || (isSunday(Date.from(date_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())))) {
                datelabel.setText("Cette journée est indisponible");
                verifyDate = false;
            } else {
                heure_Box.setItems(FXCollections.observableArrayList(ListofHour));
                datelabel.setText("Date de la consultation : √");
                verifyDate = true;
            }
        }catch(NullPointerException e){
            Psy_Frame.showAlert("Vous devez selectionner une date avant !");
        }
    }

    @FXML
    private void Step1ButtonAction() {
        date = null;
        heure = null;
        reason = 0;
        verifyStep1 = false;
        Step1Label.setText("ETAPE 1 :");

        heure = (String) heure_Box.getSelectionModel().getSelectedItem();
        switch (heure.length()){
            case 2:
                heure = (String) heure.substring(0, 1);
                break;
            case 3:
                heure = (String) heure.substring(0, 2);
                break;
            case 4:
                heure = (String) heure.substring(0, 1) + ".5";
                break;
            case 5 :
                heure = (String) heure.substring(0, 2) + ".5";
                break;
        }
        // Trouvons dans la base de donnée la raison de la consultation et Testons la date

        try {
            date = Psy_Frame.convertJDatetoString(date_field);
            reason = Consultation.findaddReason(reason_field.getText());
        } catch (Exception e) {
            System.out.println("Erreur de saisie dans la date ! ");
            e.fillInStackTrace();
        }
        if (!verifyDate) {
            datelabel.setText("Date de la consultation : NON VERIFIEE");
        }
        if ((!date.equals("")) && (!heure.equals("")) && (verifyDate)) {
            verifyStep1 = true;
            Step1Label.setText("ETAPE 1 : √");
        }
    }

    @FXML
    private void Step2ButtonAction() throws SQLException {
        Patient1 = Patient2 = Patient3 = 0;
        verifyStep2 = false;
        Step2Label.setText("ETAPE 2 :");

        // On vérifie l'existence des Patients
        // VERIFICATION DU PATIENT N1
        Patient1 = Consultation.findaddPatient(patient1field.getText());
        if ( Patient1 == 0 ){
            patient1label.setText("Patient n°1 *: Pas trouvé");
        }else{
            patient1label.setText("Patient n°1 *: Trouvé");
        }
        // VERIFICATION DU PATIENT N2
        if (!(patient2field.getText().equals(""))) {
            Patient2 = Consultation.findaddPatient(patient2field.getText());
            if (Patient2 == 0) {
                patient2label.setText("Patient n°2 *: Pas trouvé");
                Patient2 = -1;
            } else {
                patient2label.setText("Patient n°2 *: Trouvé");
            }
        }
        // VERIFICATION DU PATIENT N3
        if (!(patient3field.getText().equals(""))) {
            Patient3 = Consultation.findaddPatient(patient3field.getText());
            if (Patient3 == 0) {
                patient3label.setText("Patient n°3 *: Pas trouvé");
                Patient3 = -1;
            } else {
                patient3label.setText("Patient n°3 *: Trouvé");
            }
        }
        /*
        try {
            Integer.parseInt(patient1field.getText()); //On regarde si c'est l'ID du patient qui est donné ou pas
            System.out.println(patient1field.getText());
            String myQuery = "SELECT Patient_ID FROM Patients WHERE patient_id ='" + patient1field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            System.out.println("Je suis ICI");
            if (rset.next()) {
                Patient1 = rset.getInt(1);
                patient1label.setText("Patient n°1 *: Trouvé");
            } else {
                patient1label.setText("Patient n°1 *: Pas trouvé");
            }
        } catch (NumberFormatException e) { //Si l'input n'est pas un int alors on va chercher via email
            String myQuery = "SELECT User_ID FROM USERS WHERE user_login ='" + patient1field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            if (rset.next()) {
                System.out.println(" J'ai trouvé un patient via email avec un int de  = " + rset.getInt(1));
                Patient1 = rset.getInt(1);
                patient1label.setText("Patient n°1 *: Trouvé");
            } else {
                patient1label.setText("Patient n°1 *: Pas trouvé");
            }
        }

        // VERIFICATION DU PATIENT N2

        if (!(patient2field.getText().equals(""))) {
            try {
                Integer.parseInt(patient2field.getText());
                String myQuery = "SELECT Patient_ID FROM Patients WHERE patient_id ='" + patient2field.getText() + "'";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if (rset.next()) {
                    Patient2 = rset.getInt(1);
                    patient2label.setText("Patient n°2 : Trouvé");
                } else {
                    patient2label.setText("Patient n°2 : Pas trouvé");
                }
            } catch (NumberFormatException e) {
                String myQuery = "SELECT User_ID FROM USERS WHERE user_login ='" + patient2field.getText() + "'";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if (rset.next()) {
                    Patient2 = rset.getInt(1);
                    patient2label.setText("Patient n°2 : Trouvé");
                } else {
                    patient2label.setText("Patient n°2 : Pas trouvé");
                }
            }
            if (Patient2 == 0) {
                Patient2 = -1;
                System.out.println("Aucun patient 2 trouvé");
            }
        }
        // VERIFICATION DU PATIENT N3

        if (!(patient3field.getText().equals(""))) {
            try {
                Integer.parseInt(patient3field.getText());
                String myQuery = "SELECT Patient_ID FROM Patients WHERE patient_id =" + patient3field.getText();
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if (rset.next()) {
                    Patient3 = rset.getInt(1);
                    System.out.println("Patient 3 est : " + Patient3);
                    patient3label.setText("Patient n°3 : Trouvé");
                } else {
                    patient2label.setText("Patient n°3 : Pas trouvé");
                }
            } catch (NumberFormatException e) {
                String myQuery = "SELECT User_ID FROM USERS WHERE user_login =" + patient3field.getText();
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if (rset.next()) {
                    Patient3 = rset.getInt(1);
                    patient3label.setText("Patient n°3 : Trouvé");
                } else {
                    patient3label.setText("Patient n°3 : Pas trouvé");
                }
            }
            if (Patient3 == 0) {
                Patient3 = -1;
                System.out.println("Aucun patient 3 trouvé");
            }
        }*/
        if ((Patient1 != 0) && (Patient2 != -1) && (Patient3 != -1)) {
            verifyStep2 = true;
            Step2Label.setText("ETAPE 2 : √");
        }

    }

    @FXML
    private void AddConsulButtonAction() {
        if (verifyStep1 && verifyStep2) {
            try{
                int Consul_id = getMaxID("CONSULTATIONS"); //On créer l'ID de la consultation
                /*public Consultation(int consul_ID, String patient_1, String patient_2, String patient_3, String consul_date, double consul_hour, String consul_reason, int consul_range, String consul_text, int consul_price, String consul_how) {*/
                Consultation temp_consul = new Consultation( Consul_id, String.valueOf(Patient1),String.valueOf(Patient2), String.valueOf(Patient3), date, Double.valueOf(heure), String.valueOf(reason) , 0, null, 0, null);
                temp_consul.addConsulDB();
                System.out.println(String.valueOf(Patient2) + " , " + String.valueOf(Patient3));
                /*String myQuery = "INSERT INTO CONSULTATIONS VALUES ( " + Consul_id + "," + " TO_DATE( '" + date + "','yyyy-MM-dd')," + heure + ", " + reason + ", " + null + ", " + null + ", " + null+ ")";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery); // On ajoute cette consultation dans la base de donnée
                myQuery = " INSERT INTO Patient_Consul VALUES (" + Patient1 + "," + Consul_id + ")";
                rset = Main.database.stmt.executeQuery(myQuery);
                if (Patient2 != 0) {
                    myQuery = " INSERT INTO Patient_Consul VALUES (" + Patient2 + "," + Consul_id + ")";
                    rset = Main.database.stmt.executeQuery(myQuery);
                }
                if (Patient3 != 0) {
                    myQuery = " INSERT INTO Patient_Consul VALUES (" + Patient3 + "," + Consul_id + ")";
                    rset = Main.database.stmt.executeQuery(myQuery);
                }*/
                Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                primaryStage.close();
                login.psyStage.show();
                Psy_Frame.showInfo("Ajout de la consultation avec succès");
            } catch (Exception e) {
                System.out.println("Erreur lors de l'ajout dans la base de donnée");
            }
        } else{
            Psy_Frame.showAlert("Vous n'avez pas validée une ( ou les deux ) étapes !");
        }
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
        login.psyStage.show();
    }
}
