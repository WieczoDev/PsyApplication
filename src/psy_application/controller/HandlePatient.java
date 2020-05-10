package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import psy_application.Main;
import psy_application.Model.User.Patient;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HandlePatient implements Initializable {
    @FXML
    public Button findButton;
    @FXML
    public DatePicker date_field;
    @FXML
    public Pane container;
    @FXML
    public TableView tableview;
    @FXML
    public TableColumn idCol;
    @FXML
    public TableColumn surnameCol;
    @FXML
    public TableColumn nameCol;
    @FXML
    public TableColumn dobCol;
    @FXML
    public TableColumn maillingCol;
    @FXML
    public TableColumn howCol;
    @FXML
    public TableColumn profCol;
    @FXML
    public Button deleteButton;
    @FXML
    public Button printConsulButton;
    @FXML
    private javafx.scene.control.Button closeButton;

    private ObservableList<Patient> list = FXCollections.observableArrayList();
    public static int patient_id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("user_ID"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("patient_surname"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("patient_name"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("patient_DOB"));
        maillingCol.setCellValueFactory(new PropertyValueFactory<>("patient_mailing"));
        howCol.setCellValueFactory(new PropertyValueFactory<>("patient_how"));
        profCol.setCellValueFactory(new PropertyValueFactory<>("patient_profession"));
        list = getPatientList();
        tableview.setItems(list);
    }

    public ObservableList<Patient> getPatientList(){
        ObservableList<Patient> listPatient = FXCollections.observableArrayList();
            String consul_text;
            try {
                String myQuery = "SELECT patient_ID, patient_surname , patient_name , patient_dob, patient_mailing, patient_how , patient_profession FROM PATIENTS";
                ResultSet rset1 = Main.database.stmt.executeQuery(myQuery);
                while (rset1.next()){
                    Patient patient = new Patient(rset1.getInt(1) , rset1.getString(2), rset1.getString(3),  rset1.getString(4),  rset1.getString(5) , rset1.getInt(6), rset1.getInt(7));
                    listPatient.add(patient);
                }
            } catch (SQLException e) {
                System.out.println("Erreur de connexion avec la database");
                e.printStackTrace();
            }
            return listPatient;
    }

    @FXML
    private void handleDeletePerson() {
        // this gives the value in the selected cell:
        patient_id = (int ) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
        System.out.println(patient_id);
        try{
            // On appelle la procédure pour supprimer le PATIENT

            Main.database.RemovePatientstmt.setString(1, String.valueOf(patient_id));
            Main.database.RemovePatientstmt.execute();
            tableview.getItems().remove(tableview.getSelectionModel().getSelectedIndex()); // On le supprime aussi visuellement
        }catch (SQLException e){
            System.out.println("Erreur dans la suppression !");
            e.printStackTrace();
        }
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
        login.psyStage.show();
    }

    @FXML
    private void printConsulButtonAction() throws IOException {
        // this gives the value in the selected cell:
        patient_id = (int ) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/PatientConsul.fxml"));
        Stage PatientConsul = new Stage();
        PatientConsul.setScene(new Scene(root));
        PatientConsul.initStyle(StageStyle.UNDECORATED);
        PatientConsul.show();
    }
}
