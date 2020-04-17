package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import psy_application.Main;

import java.net.URL;
import java.rmi.server.ExportException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class NewConsul implements Initializable {
    @FXML public Button Step1Button;
    @FXML public Button Step2Button1;
    @FXML public Label Step1Label;
    @FXML public Label Step2Label;
    @FXML public Label patient1label;
    @FXML public Label patient2label;
    @FXML public Label patient3label;
    @FXML public Button CheckDateButton;
    @FXML public Label datelabel;

    @FXML private javafx.scene.control.Button closeButton;

    @FXML public ChoiceBox heure_Box;
    @FXML public TextField reason_field;
    @FXML public DatePicker date_field;
    @FXML public TextField patient1field;
    @FXML public TextField patient2field;
    @FXML public TextField patient3field;

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
    String[] List = new String[]{"8h","8h30","9h","9h30","10h","10h30", "11h","11h30", "12h","12h30","13h","13h30",
            "14h","14h30", "15h","15h30","16h","16h30","17h","17h30","18h","18h30", "19h", "19h30"};
    ArrayList<String> ListofHour = new ArrayList<String>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collections.addAll(ListofHour,List);
        heure_Box.setItems(FXCollections.observableArrayList(ListofHour));
    }

    private boolean isSunday(Date Date){
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        if(c.get(c.DAY_OF_WEEK) == 1){
            return true;
        }else return false;
    }


    @FXML
    private void CheckDateButtonAction() throws SQLException {
        verifyDate = false;
        Date Ddate =  Date.from(date_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(Ddate);
        String myQuery = "SELECT consul_hour FROM Consultations WHERE consul_date = TO_DATE('" + date +"', 'dd-mm-yyyy')";
        ResultSet rset = Main.database.stmt.executeQuery(myQuery);
        while(rset.next()){
            String str = rset.getString(1);
            if(str.substring(str.length() - 1, str.length()).equals("5")){  // ON CONVERTIT TOUTES LES VALEURS DE FLOAT EN .5 EN H30
                if(str.length()==3){
                    str= str.substring(0, 1) +"h30";
                }else{
                    str= str.substring(0, 2) +"h30";
                }
            }
            ListofHour.remove(str);
        }
        if((ListofHour.size()< 5)|| (isSunday(Ddate)) ){
            datelabel.setText("Cette journée est indisponible");
            verifyDate = false;
        }else{
            heure_Box.setItems(FXCollections.observableArrayList(ListofHour));
            datelabel.setText("Date de la consultation : √");
            verifyDate = true;
        }
    }


    @FXML
    private void Step1ButtonAction(){
        date = null;
        heure = null;
        reason = 0;
        verifyStep1 = false;

        heure = (String) heure_Box.getSelectionModel().getSelectedItem();
        if(heure.length() > 2){
            heure = (String ) heure.substring(0,1) + ".5";
        }

        // Trouvons dans la base de donnée la raison de la consultation et Testons la date

        try{
            Date Ddate =  Date.from(date_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            date = dateFormat.format(Ddate);

            String myQuery = "SELECT Reason_ID FROM Reasons WHERE reason ='" + reason_field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            if(rset.next()){
                System.out.println(" J'ai trouvé une reason avec un int de  = " + rset.getInt(1));
                reason = rset.getInt(1);
            }else if (reason_field.getText().equals("") ){
                System.out.println("Je suis ici");
                reason = 0;
            }else{
                // CREATION D'UNE RAISON ET AJOUT DANS LA DB
                myQuery = "SELECT MAX(reason_ID) FROM reasons";
                rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                reason = rset.getInt(1)+1;
                myQuery = "INSERT INTO REASONS VALUES ( "+ reason +", '" + reason_field.getText() + "')";
                rset = Main.database.stmt.executeQuery(myQuery);
            }
        }catch (Exception e){
            System.out.println("Erreur de saisie dans la date ! ");
            e.fillInStackTrace();
        }
        if(!verifyDate){
            datelabel.setText("Date de la consultation : NON VERIFIEE");
        }
        if ( (!date.equals("")) && (!heure.equals(""))&&(verifyDate)){
            verifyStep1 = true;
            Step1Label.setText("ETAPE 1 : √");
        }
    }

    @FXML
    private void Step2ButtonAction() throws SQLException {
        Patient1 = Patient2 = Patient3 = 0 ;
        verifyStep2 = false;

        // On vérifie l'existence des Patients
        try {
            Integer.parseInt(patient1field.getText());
            System.out.println(patient1field.getText());
            String myQuery = "SELECT Patient_ID FROM Patients WHERE patient_id ='" + patient1field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            System.out.println("Je suis ICI");
            if(rset.next()){
                Patient1 = rset.getInt(1);
                patient1label.setText("Patient n°1 *: Trouvé");
            }else{
                patient1label.setText("Patient n°1 *: Pas trouvé");
            }
        }catch (NumberFormatException e){ //Si l'input n'est pas un int alors on va chercher via email
            System.out.println("Je vais chercher via l'émail le patient 1");
            String myQuery = "SELECT User_ID FROM USERS WHERE user_login ='" + patient1field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            if(rset.next()){

                System.out.println(" J'ai trouvé un patient via email avec un int de  = " + rset.getInt(1));
                Patient1 = rset.getInt(1);
                patient1label.setText("Patient n°1 *: Trouvé");
            }else{
                patient1label.setText("Patient n°1 *: Pas trouvé");
            }
        }
        if(!(patient2field.getText().equals(""))){
            try {
                Integer.parseInt(patient2field.getText());
                System.out.println(patient2field.getText());
                String myQuery = "SELECT Patient_ID FROM Patients WHERE patient_id ='" + patient2field.getText() + "'";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if(rset.next()){
                    Patient2 = rset.getInt(1);
                    patient2label.setText("Patient n°2 : Trouvé");
                }else{
                    patient2label.setText("Patient n°2 : Pas trouvé");
                }
            }catch (NumberFormatException e){
                String myQuery = "SELECT User_ID FROM USERS WHERE user_login ='" + patient2field.getText() + "'";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if(rset.next()){
                    Patient2 = rset.getInt(1);
                    patient2label.setText("Patient n°2 : Trouvé");
                }else{
                    patient2label.setText("Patient n°2 : Pas trouvé");
                }
            }
            if(Patient2 == 0){
                Patient2 = -1;
                System.out.println("Aucun patient 2 trouvé");
            }
        }
        if(!(patient3field.getText().equals(""))){
            try {
                Integer.parseInt(patient3field.getText());
                System.out.println(patient3field.getText());
                String myQuery = "SELECT Patient_ID FROM Patients WHERE patient_id ='" + patient3field.getText() + "'";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if(rset.next()){
                    Patient3 = rset.getInt(1);
                    patient3label.setText("Patient n°3 : Trouvé");
                }else{
                    patient2label.setText("Patient n°3 : Pas trouvé");
                }
            }catch (NumberFormatException e){
                String myQuery = "SELECT User_ID FROM USERS WHERE user_login ='" + patient3field.getText() + "'";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                if(rset.next()){
                    Patient3 = rset.getInt(1);
                    patient3label.setText("Patient n°3 : Trouvé");
                }else {
                    patient2label.setText("Patient n°3 : Pas trouvé");
                }
            }
            if(Patient3 == 0){
                Patient3 = -1;
                System.out.println("Aucun patient 3 trouvé");
            }
        }
        if((Patient1!=0)&&(Patient2!=-1)&&(Patient3!=-1)){
            verifyStep2 = true;
            Step2Label.setText("ETAPE 2 : √");
        }

    }

    @FXML
    private void AddConsulButtonAction(){
        if(verifyStep1&&verifyStep2){
            try{
                String myQuery = "SELECT MAX(Consul_ID) FROM Consultations";
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                rset.next();
                int Consul_id = rset.getInt(1)+1; //On créer l'ID de la consultation
                myQuery = "INSERT INTO CONSULTATIONS VALUES ( "+ Consul_id + ","+" TO_DATE( '"+ date + "','dd-mm-yyyy')," +  heure + ", "+  reason + ", " +  null + ")";
                rset = Main.database.stmt.executeQuery(myQuery); // On ajoute cette consultation dans la base de donnée
                myQuery = " INSERT INTO Patient_Consul VALUES ("+ Patient1 + "," + Consul_id + ")";
                rset = Main.database.stmt.executeQuery(myQuery);
                if(Patient2 !=0){
                    myQuery = " INSERT INTO Patient_Consul VALUES ("+ Patient2 + "," + Consul_id + ")";
                    rset = Main.database.stmt.executeQuery(myQuery);
                }else if(Patient3 != 0){
                    myQuery = " INSERT INTO Patient_Consul VALUES ("+ Patient3 + "," + Consul_id + ")";
                    rset = Main.database.stmt.executeQuery(myQuery);
                }
                Stage primaryStage = (Stage) closeButton.getScene().getWindow();
                primaryStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Félicitation");
                alert.setHeaderText("Ajout de la consultation avec succès");
                alert.showAndWait();
            }catch (Exception e){
                System.out.println("Erreur lors de l'ajout dans la base de donnée");
                e.printStackTrace();
            }
        }else if(!verifyStep1||!verifyStep2){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention !");
            alert.setHeaderText("ERROR");
            alert.setContentText("Vous n'avez pas validée une ( ou les deux ) étapes !");
            alert.showAndWait();
        }
    }


    @FXML
    private void closeButtonAction(){
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
