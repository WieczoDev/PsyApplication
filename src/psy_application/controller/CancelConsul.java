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

import java.lang.invoke.SwitchPoint;
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

    private ObservableList<Consultation> getConsulList(String strDate) {
        list.clear();
        int consul_ID, consul_hour, consul_reason, patient1 = 0, patient2 = 0, patient3 = 0;

        try {
            String myQuery = "SELECT consul_ID, consul_hour, consul_reason FROM Consultations WHERE consul_date = TO_DATE('" + strDate + "', 'dd-mm-yyyy')";
            ResultSet rset1 = Main.database.stmt.executeQuery(myQuery);
            while (rset1.next()) {
                consul_ID = rset1.getInt(1);
                consul_hour = rset1.getInt(2);
                consul_reason = rset1.getInt(3);
                try {
                    System.out.println(" Consul id = " + consul_ID + " , consul_hour = " + consul_hour + " , consul_reason = " + consul_reason);
                    String myQuery2 = "SELECT patient_ID FROM PATIENT_CONSUL WHERE CONSUL_ID = " + consul_ID;
                    System.out.println(" Je suis ici ");
                    ResultSet rset2 = Main.database.stmt2.executeQuery(myQuery2);
                    int count = 0;
                    while (rset2.next()) {
                        System.out.println(" Patient trouvé : " + rset2.getInt(1));
                        switch (count) {
                            case 0:
                                patient1 = rset2.getInt(1);
                                count += 1;
                                break;
                            case 1:
                                patient2 = rset2.getInt(1);
                                count += 1;
                                break;
                            case 2:
                                patient3 = rset2.getInt(1);
                                count += 1;
                                break;
                        }
                    }
                    Consultation Consul1 = new Consultation(consul_ID, patient1, patient2, //
                            patient3, strDate, consul_hour, consul_reason, consul_hour);
                    list.add(Consul1);
                } catch (SQLException e) {System.out.println("Erreur de connexion avec la database");}
            }
        } catch (SQLException e) {System.out.println("Erreur de connexion avec la database");}
        return list;
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = tableview.getSelectionModel().getSelectedIndex();
        Object item = tableview.getItems().get(selectedIndex);
        // this gives the value in the selected cell:
        int data =  (int ) idCol.getCellObservableValue(item).getValue();
        System.out.println(data);
    }

    @FXML
    private void findButtonAction() throws SQLException {
        strDate = Psy_Frame.convertJDatetoString(date_field);
        list = getConsulList(strDate);
        tableview.setItems(list);
    }


    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
