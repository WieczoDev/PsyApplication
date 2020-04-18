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
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class Psy_Frame{
    @FXML
    public static BorderPane home;
    @FXML
    public Label homepageLabel;

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

    public static String convertJDatetoString(DatePicker date_field){
        java.util.Date Ddate = java.util.Date.from(date_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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
}
