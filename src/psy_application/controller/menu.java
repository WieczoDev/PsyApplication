package psy_application.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class menu {
    @FXML
    Hyperlink homeHP;
    @FXML
    Hyperlink consultationHP;
    @FXML
    private Button addButton;
    @FXML
    private ImageView exit;
    @FXML
    private javafx.scene.control.Button closeButton;

    @FXML
    private void openHomeScene() throws IOException {
        Psy_Frame.openHomeScene();
    }

    @FXML
    private void openConsulScene() throws IOException {
        Psy_Frame.openConsulScene();
    }

    @FXML
    private void closeButtonAction() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/verifyexit.fxml"));
        Stage VerifyExit = new Stage();
        VerifyExit.setScene(new Scene(root));
        VerifyExit.show();
    }

    @FXML
    private void addPatients() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/AddPatient.fxml"));
        Stage AddPatient = new Stage();
        AddPatient.setScene(new Scene(root));
        AddPatient.show();
    }

}
