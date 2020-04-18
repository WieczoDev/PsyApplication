package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import psy_application.Consultation;
import psy_application.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class Psy_Frame_Consul  implements Initializable {
    @FXML
    public Button createButton;
    @FXML
    public Button cancelConsulButton;
    @FXML
    public TableView mondaytableview;
    @FXML
    public TableColumn mheureCol;
    @FXML
    public TableColumn mpatientCol;
    @FXML
    public TableView tuesdaytableview;
    @FXML
    public TableColumn tuheureCol;
    @FXML
    public TableColumn tupatientCol;
    @FXML
    public TableView wednesdaytableview;
    @FXML
    public TableColumn wheureCol;
    @FXML
    public TableColumn wpatientCol;
    @FXML
    public TableView thursdaytableview;
    @FXML
    public TableColumn thheureCol;
    @FXML
    public TableColumn thpatientCol;
    @FXML
    public TableView fridaytableview;
    @FXML
    public TableColumn fheureCol;
    @FXML
    public TableColumn fpatientCol;
    @FXML
    public TableView saturdaytableview;
    @FXML
    public TableColumn sheureCol;
    @FXML
    public TableColumn spatientCol;
    @FXML
    public Button findButton;
    @FXML
    public DatePicker date_field;

    private String strDate;
    ObservableList<Consultation> list = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mheureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        mpatientCol.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        tuheureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        tupatientCol.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        wheureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        wpatientCol.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        thheureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        thpatientCol.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        fheureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        fpatientCol.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        sheureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        spatientCol.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
    }

    private boolean isMonday(Date Date) {
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        if (c.get(c.DAY_OF_WEEK) == 7) {
            return true;
        } else return false;
    }
    @FXML
    private void findButtonAction(){
        //On VERFIE QUE LA DATE CHOISIE EST UN LUNDI
        if(isMonday(java.util.Date.from(date_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()))){
            strDate = Psy_Frame.convertJDatetoString(date_field);
            list = getConsulList(strDate);
            mondaytableview.setItems(list);
        }
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
    public void cancelConsulButtonAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/CancelConsul.fxml"));
        Stage CancelConsul = new Stage();
        CancelConsul.setScene(new Scene(root));
        CancelConsul.show();
    }

    @FXML
    public void createConsul(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/NewConsul.fxml"));
        Stage NewConsul = new Stage();
        NewConsul.setScene(new Scene(root));
        NewConsul.show();
    }
}
