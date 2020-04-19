package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import psy_application.Consultation;
import psy_application.Main;

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
    private javafx.scene.control.Button closeButton;

    private String strDate;
    ObservableList<Consultation> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        heureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("consul_ID"));
        patient1Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        patient2Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID2"));
        patient3Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID3"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("consul_reason"));
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = tableview.getSelectionModel().getSelectedIndex();
        Object item = tableview.getItems().get(selectedIndex);
        // this gives the value in the selected cell:
        int consul_id =  (int ) idCol.getCellObservableValue(item).getValue();
        System.out.println(consul_id);
        try{
            String myQuery = "DELETE FROM PATIENT_CONSUL WHERE CONSUL_ID = " + consul_id ;
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            System.out.println("J'ai pu l'effacer dans la première table");
            myQuery = "DELETE FROM CONSULTATIONS WHERE CONSUL_ID = " + consul_id ;
            rset = Main.database.stmt.executeQuery(myQuery);
            System.out.println("J'ai pu l'effacer dans la seconde table");
            tableview.getItems().remove(selectedIndex);
        }catch (SQLException e){
            System.out.println("Erreur dans la suppression !");
        }
    }

    @FXML
    private void findButtonAction() throws SQLException {
        strDate = Psy_Frame.convertJDatetoString(date_field);
        list = Psy_Frame.getConsulList(strDate , list);
        tableview.setItems(list);
    }


    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
