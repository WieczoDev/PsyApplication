package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import psy_application.Model.Consultation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PatientFrame implements Initializable {
    @FXML
    public static BorderPane home;
    @FXML
    public Label homepageLabel;
    @FXML
    public TableView tableview;
    @FXML
    public TableColumn heureCol;
    @FXML
    public TableColumn dateCol;
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
    public javafx.scene.control.Button exitButton;
    @FXML
    public Button modifyButton;
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
        buildtableview(login.Current_User.getUser_ID());
    }

    private void buildtableview(int patient_id){
        list.clear();
        list = Psy_Frame.getConsulList(patient_id, list);
        System.out.println(list);
        tableview.setItems(list);
    }

    @FXML
    private void exitButtonAction() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/verifyexit.fxml"));
        Stage VerifyExit = new Stage();
        VerifyExit.setScene(new Scene(root));
        VerifyExit.show();
    }

    @FXML
    public void modifyButtonAction() throws IOException {
        login.psyStage.close();
        AddPatient.patient_id = login.Current_User.getUser_ID();
        System.out.println(login.Current_User.getUser_ID());
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/AddPatient.fxml"));
        Stage ModifyPatient = new Stage();
        ModifyPatient.setScene(new Scene(root));
        ModifyPatient.initStyle(StageStyle.UNDECORATED);
        ModifyPatient.show();
    }
}
