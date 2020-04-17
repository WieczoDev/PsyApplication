package psy_application.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class verifyexit {
    @FXML public javafx.scene.control.Button yesButton;
    @FXML  public javafx.scene.control.Button noButton;

    @FXML
    private void noButtonAction(){
        Stage primaryStage = (Stage) yesButton.getScene().getWindow();
        primaryStage.close();
    }
    @FXML
    private void yesButtonAction(){
        Stage primaryStage = (Stage) yesButton.getScene().getWindow();
        primaryStage.close();
        login.psyStage.close();
    }
}
