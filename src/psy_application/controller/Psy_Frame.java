package psy_application.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class Psy_Frame implements Initializable{
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


    public static ObservableList<Consultation> getConsulList(String strDate , ObservableList list) {
        list.clear();
        int consul_ID, consul_hour, consul_reason, patient1 = 0, patient2 = 0, patient3 = 0;
        try {
            String myQuery = "SELECT consul_ID, consul_hour, consul_reason FROM Consultations WHERE consul_date = TO_DATE('" + strDate + "', 'yyyy-MM-dd')";
            ResultSet rset1 = Main.database.stmt.executeQuery(myQuery);
            while (rset1.next()) {
                consul_ID = rset1.getInt(1);
                consul_hour = rset1.getInt(2);
                consul_reason = rset1.getInt(3);
                try {
                    System.out.println(" Consul id = " + consul_ID + " , consul_hour = " + consul_hour + " , consul_reason = " + consul_reason);
                    String myQuery2 = "SELECT patient_ID FROM PATIENT_CONSUL WHERE CONSUL_ID = " + consul_ID;
                    System.out.println(" Je suis ici ");
                    ResultSet rset2 = Main.database.stmt2.executeQuery(myQuery2);
                    int count = 0;
                    while (rset2.next()) {
                        System.out.println(" Patient trouvé : " + rset2.getInt(1));
                        switch (count) {
                            case 0:
                                patient1 = rset2.getInt(1);
                                count += 1;
                                break;
                            case 1:
                                patient2 = rset2.getInt(1);
                                count += 1;
                                break;
                            case 2:
                                patient3 = rset2.getInt(1);
                                count += 1;
                                break;
                        }
                    }
                    Consultation Consul1 = new Consultation(consul_ID, patient1, patient2, //
                            patient3, strDate, consul_hour, consul_reason, consul_hour);
                    list.add(Consul1);
                } catch (SQLException e) {System.out.println("Erreur de connexion avec la database");}
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion avec la database");
            e.printStackTrace();
        }
        return list;
    }


    public static String convertJDatetoString(DatePicker date_field){
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        heureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("consul_ID"));
        patient1Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        patient2Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID2"));
        patient3Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID3"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("consul_reason"));
        System.out.println("Bonjour Admin !");
        tableview.setPlaceholder(new Label("Aucune consultation aujourd'hui !"));
        String strDate = java.time.LocalDate.now().toString();
        System.out.println(strDate);
        list = getConsulList(strDate, list);
        tableview.setItems(list);
    }
}
