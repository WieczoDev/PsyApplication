package psy_application.controller;

import database.OracleDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import psy_application.Main;
import psy_application.Model.User.Patient;
import psy_application.Model.User.Psy;
import psy_application.Model.User.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login {

    @FXML
    private TextField login_field;
    @FXML
    private TextField pass_field;
    @FXML
    private javafx.scene.control.Button closeButton;
    public static User Current_User;
    public static Stage psyStage;
    public static Stage patientStage;

    /**
     * LOGIN PAGE -> PAGE DE CONNECTION DE LA PSY MAIS AUSSI DES FUTURS PATIENTS ( pas implémenté pour le moment )
     */

    @FXML
    private void connection(ActionEvent event) throws SQLException, IOException {
        if (!pass_field.getText().equals("")) {
            Main.database = new OracleDB("admin", "oracle");
            String myQuery = "SELECT User_Login, User_pass, User_ID FROM Users WHERE User_login='" + login_field.getText() + "' AND User_pass = \'" + pass_field.getText() + "'";
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            if (rset.next()) {
                int ID = rset.getInt(3);
                System.out.println("Connection réussie, Bienvenue " + rset.getString(1) + " !");
                Stage primaryStage = (Stage) closeButton.getScene().getWindow(); //close login page
                primaryStage.close();
                if (login_field.getText().equals("admin")) {
                    Current_User = new Psy(login_field.getText(), pass_field.getText());
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/Psy_Frame_Home.fxml"));
                    psyStage = new Stage();
                    psyStage.setScene(new Scene(root));
                    primaryStage.setResizable(false);
                    psyStage.initStyle(StageStyle.UNDECORATED);
                    psyStage.show();
                } else {
                    Current_User = new Patient(login_field.getText(), pass_field.getText(), ID);
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/PatientFrame.fxml"));
                    psyStage = new Stage();
                    psyStage.setScene(new Scene(root));
                    primaryStage.setResizable(false);
                    psyStage.initStyle(StageStyle.UNDECORATED);
                    psyStage.show();
                }
            } else {
                System.out.println("Erreur identifiants ou mot de passe éronnée");
            }
        }
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
