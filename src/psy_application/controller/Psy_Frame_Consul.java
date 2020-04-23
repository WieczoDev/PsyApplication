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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private String CurrentDate;
    ObservableList<Consultation> list0 = FXCollections.observableArrayList();
    ObservableList<Consultation> list1 = FXCollections.observableArrayList();
    ObservableList<Consultation> list2 = FXCollections.observableArrayList();
    ObservableList<Consultation> list3 = FXCollections.observableArrayList();
    ObservableList<Consultation> list4 = FXCollections.observableArrayList();
    ObservableList<Consultation> list5 = FXCollections.observableArrayList();




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

    private boolean isMonday(String strDate) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date Date = dateFormat.parse(strDate);
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        if (c.get(c.DAY_OF_WEEK) == 2) {
            System.out.println(c.get(c.DAY_OF_WEEK));
            return true;
        } else return false;
    }
    public static String addOneDay(String date) {
        return LocalDate
                .parse(date)
                .plusDays(1)
                .toString();
    }
    public static String convertJDatetoString(DatePicker date_field){ //Use a different date format
        java.util.Date Ddate = java.util.Date.from(date_field.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(Ddate);
    }

    @FXML
    private void findButtonAction() throws ParseException {
        //On VERFIE QUE LA DATE CHOISIE EST UN LUNDI
            int count = 0;
            CurrentDate = convertJDatetoString(date_field);
            if(isMonday(CurrentDate)){
                do{
                    switch (count){
                        case 0 :
                            list0 = Psy_Frame.getConsulList(CurrentDate, list0);
                            try{
                                System.out.println(list0.get(0));
                            }catch(Exception e ){ System.out.println("La liste est vide");}
                            mondaytableview.setItems(list0);
                            break;
                        case 1 :
                            list1 = Psy_Frame.getConsulList(CurrentDate, list1);
                            try{
                                System.out.println(list1.get(0));
                            }catch(Exception e ){ System.out.println("La liste est vide");}
                            tuesdaytableview.setItems(list1);
                            break;
                        case 2 :
                            list2 = Psy_Frame.getConsulList(CurrentDate, list2);
                            try{
                                System.out.println(list2.get(0));
                            }catch(Exception e ){ System.out.println("La liste est vide");}
                            wednesdaytableview.setItems(list2);
                            break;
                        case 3 :
                            list3 = Psy_Frame.getConsulList(CurrentDate, list3);
                            try{
                                System.out.println(list3.get(0));
                            }catch(Exception e ){ System.out.println("La liste est vide");}
                            thursdaytableview.setItems(list3);
                            break;
                        case 4 :
                            list4 = Psy_Frame.getConsulList(CurrentDate, list4);
                            try{
                                System.out.println(list4.get(0));
                            }catch(Exception e ){ System.out.println("La liste est vide");}
                            fridaytableview.setItems(list4);
                            break;
                        case 5 :
                            list5 = Psy_Frame.getConsulList(CurrentDate , list5);
                            try{
                                System.out.println(list5.get(0));
                            }catch(Exception e ){ System.out.println("La liste est vide");}
                            saturdaytableview.setItems(list5);
                            break;
                    }
                    CurrentDate = addOneDay(CurrentDate);
                    count+=1;
                }while( !isMonday(CurrentDate));
            }
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
