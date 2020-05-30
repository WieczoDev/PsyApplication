package psy_application;

import database.OracleDB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.sql.*;

public class Main extends Application {
    public static OracleDB database;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Je suis le main");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}
