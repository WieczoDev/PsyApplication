package psy_application.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import psy_application.Consultation;
import psy_application.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class Psy_Frame implements Initializable {
    @FXML
    public static BorderPane home;
    @FXML
    public Label homepageLabel;
    @FXML
    public TableView tableview;
    @FXML
    public TableColumn heureCol;
    @FXML
    public TableColumn idCol;
    @FXML
    public TableColumn patient1Col;
    @FXML
    public TableColumn patient2Col;
    @FXML
    public TableColumn patient3Col;
    @FXML
    public TableColumn reasonCol;
    @FXML
    public TableColumn rangeCol;
    @FXML
    public Button gestionpatientButton;

    ObservableList<Consultation> list = FXCollections.observableArrayList();

    public static void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention !");
        alert.setHeaderText("ERROR");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void showInfo(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Félicitation");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static Consultation getConsul(int consul_id) {
        Consultation Consul1;
        ArrayList<Integer> listPatient = new ArrayList<>();
        try {
            String myQuery = "SELECT consul_ID, consul_hour, consul_reason, consul_text, consul_price, consultation_how, consul_date FROM Consultations WHERE consul_id = " + consul_id;
            ResultSet rset1 = Main.database.stmt.executeQuery(myQuery);
            if(rset1.next()){
                try {
                    String myQuery2 = "SELECT patient_ID FROM PATIENT_CONSUL WHERE CONSUL_ID = " + consul_id;
                    ResultSet rset2 = Main.database.stmt2.executeQuery(myQuery2);
                    int count = 0;
                    while (rset2.next()) {
                        listPatient.add(rset2.getInt(1));
                    }
                    while ( listPatient.size() <3){
                        listPatient.add(0);
                    }
                    Consul1 = new Consultation(rset1.getInt(1), listPatient.get(0), listPatient.get(1), //
                            listPatient.get(2), rset1.getString(7), rset1.getInt(2), rset1.getInt(3), 0, rset1.getString(4), rset1.getInt(5), rset1.getInt(6));
                    return Consul1;
                } catch (SQLException e) {
                    System.out.println("Erreur de connexion avec la database");
                    return null;
                }
            }else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion avec la database");
            e.printStackTrace();
            return null;
        }
    }

    /*
           METHODES PERMETTANT DE CREER LES LISTES DE CONSULTATION POUR LES TABLEAU ( AFFICHAGE )
     */

    public static ObservableList<Consultation> getConsulList(String strDate, ObservableList list) {
        list.clear();
        ArrayList<Integer> listPatient = new ArrayList<>();
        String consul_text;
        try { //this.consul_text = consul_text;            this.consul_price = consul_price;            this.consul_how = consul_how;
            String myQuery = "SELECT consul_ID, consul_hour, consul_reason, consul_text, consul_price, consultation_how FROM Consultations WHERE consul_date = TO_DATE('" + strDate + "', 'yyyy-MM-dd')";
            ResultSet rset1 = Main.database.stmt.executeQuery(myQuery);
            while (rset1.next()) {
                    listPatient.clear();
                try {
                    String myQuery2 = "SELECT patient_ID FROM PATIENT_CONSUL WHERE CONSUL_ID = " + rset1.getInt(1);
                    ResultSet rset2 = Main.database.stmt2.executeQuery(myQuery2);
                    int count = 0;
                    while (rset2.next()) {
                        listPatient.add(rset2.getInt(1));
                    }
                    while ( listPatient.size() <3){
                        listPatient.add(0);
                    }
                    Consultation Consul1 = new Consultation(rset1.getInt(1), listPatient.get(0), listPatient.get(1),
                            listPatient.get(2), strDate, rset1.getDouble(2), rset1.getInt(3), 0, rset1.getString(4), rset1.getInt(5), rset1.getInt(6));
                    list.add(Consul1);
                } catch (SQLException e) {
                    System.out.println("Erreur de connexion avec la database 1");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion avec la database 2");
            e.printStackTrace();
        }
        return list;
    }

    public static ObservableList<Consultation> getConsulList(int patient_id, ObservableList list) {
        list.clear();
        ArrayList<Integer> listPatient = new ArrayList();
        try {
            String myQuery2 = "SELECT consul_ID FROM PATIENT_CONSUL WHERE patient_ID = " + patient_id;
            ResultSet rset2 = Main.database.stmt2.executeQuery(myQuery2);
            while (rset2.next()){
                listPatient.clear();
                String myQuery1 = "SELECT patient_ID FROM PATIENT_CONSUL WHERE CONSUL_ID = " + rset2.getInt(1);
                ResultSet rset1 = Main.database.stmt.executeQuery(myQuery1);
                while(rset1.next()){
                    listPatient.add(rset1.getInt(1));
                }
                while ( listPatient.size() <3){
                    listPatient.add(0);
                }
                String myQuery = "SELECT CONSUL_DATE , consul_hour, consul_reason, consul_text, consul_price, consultation_how FROM Consultations WHERE CONSUL_ID =" + rset2.getInt(1);
                rset1 = Main.database.stmt.executeQuery(myQuery);
                rset1.next();
                Consultation Consul1 = new Consultation(rset2.getInt(1), listPatient.get(0), listPatient.get(1),
                        listPatient.get(2),rset1.getString(1),  rset1.getDouble(2), rset1.getInt(3), 0, rset1.getString(4), rset1.getInt(5), rset1.getInt(6));
                list.add(Consul1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion avec la database 2");
            e.printStackTrace();
        }
        return list;
    }

    public static String convertJDatetoString(DatePicker date_field) {
        java.util.Date Ddate = java.util.Date.from(date_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Ddate);
    }

    @FXML
    public static void openHomeScene() throws IOException {
        Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/Psy_Frame_Home.fxml"));
        login.psyStage.setScene(new Scene(root));
    }

    @FXML
    public static void openConsulScene() throws IOException {
        Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/Psy_Frame_Consul.fxml"));
        login.psyStage.setScene(new Scene(root));
    }
    @FXML
    public void gestionpatientButtonAction() throws IOException {
        login.psyStage.close();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/HandlePatient.fxml"));
        Stage HandlePatient = new Stage();
        HandlePatient.setScene(new Scene(root));
        HandlePatient.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        heureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("consul_ID"));
        patient1Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        patient2Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID2"));
        patient3Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID3"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("consul_reason"));
        tableview.setPlaceholder(new Label("Aucune consultation aujourd'hui !"));
        String strDate = java.time.LocalDate.now().toString();
        list = getConsulList(strDate, list);
        tableview.setItems(list);
    }


}
