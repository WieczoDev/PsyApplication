package psy_application.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Psy_Frame {
    @FXML
    public static BorderPane home;
    @FXML
    public Label homepageLabel;
    @FXML
    public Button createButton;


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
    public void createConsul(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/NewConsul.fxml"));
        Stage NewConsul = new Stage();
        NewConsul.setScene(new Scene(root));
        NewConsul.show();
    }
}
