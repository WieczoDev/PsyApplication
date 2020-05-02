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
import javafx.stage.StageStyle;
import psy_application.Consultation;
import psy_application.Main;
import psy_application.User.Psy;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PatientConsul implements Initializable {
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
    public TableColumn dateCol;
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
    public Label titleLabel;
    @FXML
    private javafx.scene.control.Button closeButton;
    ObservableList<Consultation> list = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        heureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("consul_ID"));
        patient1Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        patient2Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID2"));
        patient3Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID3"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("consul_reason"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("consul_date"));
        payement.setCellValueFactory(new PropertyValueFactory<>("consul_how"));
        prix.setCellValueFactory(new PropertyValueFactory<>("consul_price"));
        commentaire.setCellValueFactory(new PropertyValueFactory<>("consul_text"));
        buildtableview(HandlePatient.patient_id);
    }
    @FXML
    private void handleDeletePerson() {
        // this gives the value in the selected cell:
        int consul_id = (int)idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
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
    private void modifyButtonAction() throws IOException {
        System.out.println((int ) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue());
        ModifyConsul.consultation_id =  (int ) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
        System.out.println(ModifyConsul.consultation_id);
        Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/ModifyConsul.fxml"));
        Stage ModifyConsul = new Stage();
        ModifyConsul.setScene(new Scene(root));
        ModifyConsul.initStyle(StageStyle.UNDECORATED);
        ModifyConsul.show();
    }

    private  void buildtableview(int patient_id){
        list.clear();
        list = Psy_Frame.getConsulList(patient_id, list);
        tableview.setItems(list);
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
