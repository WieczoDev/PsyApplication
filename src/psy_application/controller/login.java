package psy_application.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import psy_application.Main;
import psy_application.User.Psy;
import psy_application.User.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class login {

    @FXML
    private TextField login_field;
    @FXML private TextField pass_field;
    @FXML private javafx.scene.control.Button closeButton;
    public static User Current_User;
    public static Stage psyStage;
    public static Stage patientStage;

    @FXML
    private void connection(ActionEvent event) throws SQLException, IOException {
        String myQuery = "SELECT User_Login, User_pass FROM Users WHERE User_login='" + login_field.getText() +"' AND User_pass = \'" + pass_field.getText() +"'";
        ResultSet rset = Main.database.stmt.executeQuery(myQuery);
        if(rset.next()){
            String i = rset.getString(1);
            System.out.println("Connection réussie en tant que " + i);
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/Psy_Frame_Home.fxml"));
            Current_User = new Psy( login_field.getText() , pass_field.getText());
            Stage primaryStage = (Stage) closeButton.getScene().getWindow(); //close login page
            primaryStage.close();
            if(login_field.getText().equals("admin")){
                psyStage = new Stage();
                psyStage.setScene(new Scene(root));primaryStage.setResizable(false);
                psyStage.initStyle(StageStyle.UNDECORATED);
                psyStage.show();
            }else{
                patientStage = new Stage();
                patientStage.setScene(new Scene(root));primaryStage.setResizable(false);
                patientStage.initStyle(StageStyle.UNDECORATED);
                patientStage.show();
            }

        }else {
            System.out.println("Erreur identifiants ou mot de passe éronnée");
        }
    }

    @FXML
    private void closeButtonAction(){
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
