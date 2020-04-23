package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import psy_application.Consultation;
import psy_application.Main;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FinaliseConsul implements Initializable {
    @FXML
    public Label dateLabel;
    @FXML
    public Label heureLabel;
    @FXML
    public Label patientsLabel;
    @FXML
    public TextField pricefield;
    @FXML
    public Slider anxieteScroll;
    @FXML
    public ComboBox payBox;
    @FXML
    public TextField textLabel;
    @FXML
    private javafx.scene.control.Button closeButton;
    @FXML
    public Button finaliseButton;

    private int consul_id;
    private Consultation consultation;
    String[] List = new String[]{"Non payé","Chèque", "Carte Bancaire", "Espèces" , "PayPal", "Lydia"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        payBox.setItems(FXCollections.observableArrayList(List));
        consul_id = CancelConsul.getConsul_id();
        consultation = Psy_Frame.getConsul(consul_id);
        dateLabel.setText( dateLabel.getText() +" " + consultation.getConsul_date());
        heureLabel.setText( heureLabel.getText() +" " + consultation.getConsul_hour());
        if(consultation.getConsul_text() != null) textLabel.setText(consultation.getConsul_text());
        patientsLabel.setText( patientsLabel.getText() +" " + consultation.getPatient_ID1() + ", " + consultation.getPatient_ID2() + ", " + consultation.getPatient_ID3());
        if(consultation.getConsul_reason() != 6){
            anxieteScroll.setDisable(true);
        }
    }
    private int getHow(){
        try {
            switch ((String) payBox.getSelectionModel().getSelectedItem()){
                case "Chèque":
                    return 2;
                case  "Carte Bancaire" :
                    return 1;
                case "Espèces" :
                    return 3;
                case "PayPal" :
                    return 4;
                case  "Lydia":
                    return 5;
                default: return 0;
            }
        }catch (Exception e){
            return -1;
        }
    }
    private boolean isanInt(String chaine) {
        try {
            Integer.parseInt(chaine);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
    @FXML
    private void finaliseButtonAction() throws SQLException {
        if(getHow() != -1 && isanInt(pricefield.getText())){
            consultation.setConsul_how(getHow());
            consultation.setConsul_price(Integer.parseInt(pricefield.getText()));
            if(consultation.getConsul_reason() != 6){
                consultation.setConsul_text(textLabel.getText());
            }else{
                consultation.setConsul_text("Le taux d'anxiété du patient est = " + (int) anxieteScroll.getValue() + ", Votre commentaire :" + textLabel.getText());

            }
            System.out.println(consultation);
            consultation.setConsul_text(consultation.getConsul_text().replace("'", "''"));String myQuery = "UPDATE CONSULTATIONS SET CONSULTATION_HOW = " +consultation.getConsul_how() +
                    ", CONSUL_TEXT ='" + consultation.getConsul_text() +"', CONSUL_PRICE =" + consultation.getConsul_price() + " WHERE CONSUL_ID = " + consul_id;;
            ResultSet rset = Main.database.stmt.executeQuery(myQuery);
            Psy_Frame.showInfo("Finalisation effectué !");
            Stage primaryStage = (Stage) closeButton.getScene().getWindow();
            primaryStage.close();
        }
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }
}
