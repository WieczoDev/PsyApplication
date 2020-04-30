package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import psy_application.Consultation;
import psy_application.Main;
import psy_application.User.Psy;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CancelConsul implements Initializable {
    @FXML
    public Button findButton;
    @FXML
    public DatePicker date_field;
    @FXML
    public Pane container;
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
    public Button deleteButton;
    @FXML
    public TableColumn payement;
    @FXML
    public TableColumn prix;
    @FXML
    public TableColumn commentaire;
    @FXML
    public Button finaliseButton;
    @FXML
    private javafx.scene.control.Button closeButton;

    private String strDate;
    private static int consul_id;
    ObservableList<Consultation> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        heureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("consul_ID"));
        patient1Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        patient2Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID2"));
        patient3Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID3"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("consul_reason"));
        payement.setCellValueFactory(new PropertyValueFactory<>("consul_how"));
        prix.setCellValueFactory(new PropertyValueFactory<>("consul_price"));
        commentaire.setCellValueFactory(new PropertyValueFactory<>("consul_text"));
    }

    //GETTERS
    public static int getConsul_id(){
        return consul_id;
    }

    @FXML
    private void handleDeletePerson() {
        // this gives the value in the selected cell:
        consul_id = (int ) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
        System.out.println(consul_id);
        try{
            Main.database.RemoveConsulstmt.setString(1, String.valueOf(consul_id));
            Main.database.RemoveConsulstmt.execute();                                                    // On appelle la procédure pour supprimer un rdv
            tableview.getItems().remove(tableview.getSelectionModel().getSelectedIndex());
        }catch (SQLException e){
            System.out.println("Erreur dans la suppression !");
            e.printStackTrace();
    }
    }

    @FXML
    private void findButtonAction() throws SQLException {
        strDate = Psy_Frame.convertJDatetoString(date_field);
        list = Psy_Frame.getConsulList(strDate , list);
        tableview.setItems(list);
    }

    @FXML
    private void finaliseButtonAction(){
        try{
            consul_id =  (int ) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
            Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/FinaliseConsul.fxml"));
            Stage FinaliseConsul = new Stage();
            FinaliseConsul.setScene(new Scene(root));
            FinaliseConsul.show();
        }catch (Exception e ){
            Psy_Frame.showAlert("Aucune consultation séléctionnée !");
        }

    }


    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
