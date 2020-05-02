package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import psy_application.Consultation;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyConsul implements Initializable {
    @FXML
    public Label dateLabel;
    @FXML
    public Label heureLabel;
    @FXML
    public ComboBox payBox;
    @FXML
    public TextField pricefield;
    @FXML
    public TextField textField;
    @FXML
    public Slider anxieteScroll;
    @FXML
    public TextField p1field;
    @FXML
    public TextField p2field;
    @FXML
    public TextField p3field;
    @FXML
    public Button closeButton;
    @FXML
    public TextField reasonfield;

    String[] List = new String[]{"Non payé", "Chèque", "Carte Bancaire", "Espèces", "PayPal", "Lydia"};
    public static int consultation_id;

    Consultation consultation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consultation = Psy_Frame.getConsul(consultation_id);
        dateLabel.setText(dateLabel.getText() + " " + consultation.getConsul_date());
        heureLabel.setText(heureLabel.getText() + " " + consultation.getConsul_hour());
        payBox.setItems(FXCollections.observableArrayList(List));
        reasonfield.setText(consultation.getConsul_reason());
        payBox.setPromptText(consultation.getConsul_how());
        pricefield.setText(consultation.getConsul_price() + "");
        System.out.println(consultation.getConsul_reason());
        try {
            textField.setText(consultation.getComment());
            if (!consultation.getConsul_reason().equals("Anxiété") || consultation.getConsul_reason() == null) {
                anxieteScroll.setDisable(true);
            } else {
                anxieteScroll.setValue(consultation.getAnxiete());
            }
        } catch (NullPointerException e) {
        }
        p1field.setText(consultation.getPatient_ID1());
        p2field.setText(consultation.getPatient_ID2());
        p3field.setText(consultation.getPatient_ID3());
    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();

    }

    @FXML
    private void modifyButtonAction() throws SQLException {
        int price = Integer.parseInt(pricefield.getText());
        String payement;
        if ((String) payBox.getSelectionModel().getSelectedItem() == null) {
            payement = consultation.getConsul_how();
        } else payement = (String) payBox.getSelectionModel().getSelectedItem();
        Consultation tmpConsul = new Consultation(consultation_id, p1field.getText(), p2field.getText(), p3field.getText(), consultation.getConsul_date(), consultation.getConsul_hour(),
                reasonfield.getText(), 0, textField.getText(), price, payement);
        if (tmpConsul.getConsul_reason() != null) {
            if (tmpConsul.getConsul_reason().equals("6")) {
                tmpConsul.setConsul_text("Le taux d'anxiété du patient est = " + (int) anxieteScroll.getValue() + "; " + textField.getText());
            }
        }
        tmpConsul.setConsul_text(tmpConsul.getConsul_text().replace("'", "''"));
        if(tmpConsul.UpdateConsultation()) closeButtonAction();
    }
}
