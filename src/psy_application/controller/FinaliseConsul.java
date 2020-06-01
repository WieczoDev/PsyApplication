package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import psy_application.Model.Consultation;
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
    String[] List = new String[]{"Non payé", "Chèque", "Carte Bancaire", "Espèces", "PayPal", "Lydia"};

    /**
     * FINALISECONSUL ->  FRAME PERMETTANT DE SAISIR UN PRIX, UN MOYEN DE PAIEMENT, NIVEAU D'ANXIETE ET DES MOTS CLES
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        payBox.setItems(FXCollections.observableArrayList(List));
        consul_id = HandleConsul.getConsul_id();
        consultation = Consultation.getConsul(consul_id);
        dateLabel.setText(dateLabel.getText() + " " + consultation.getConsul_date());
        heureLabel.setText(heureLabel.getText() + " " + consultation.getConsul_hour());
        if (consultation.getConsul_text() != null) textLabel.setText(consultation.getComment());
        patientsLabel.setText(patientsLabel.getText() + " " + consultation.getPatient_ID1() + ", " + consultation.getPatient_ID2() + ", " + consultation.getPatient_ID3());
        System.out.println(consultation.getConsul_reason());
        if (consultation.getConsul_reason() == null || !consultation.getConsul_reason().equals("Anxiété")) {
            anxieteScroll.setDisable(true);
        }
        if (consultation.getConsul_price() != 0) {
            pricefield.setText(String.valueOf(consultation.getConsul_price()));
        }
    }

    // VERIFICATION QUE LE PRIX SOIT BIEN UN INT
    private boolean isanInt(String chaine) {
        try {
            Integer.parseInt(chaine);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @FXML
    private void finaliseButtonAction() throws SQLException {
        if (isanInt(pricefield.getText())) {

            /** On va mettre a jour l'objet Consultation */

            consultation.setConsul_how(consultation.getHow(((String) payBox.getSelectionModel().getSelectedItem())));
            consultation.setConsul_price(Integer.parseInt(pricefield.getText()));
            if (consultation.getConsul_reason() == null || !consultation.getConsul_reason().equals("Anxiété")) {
                consultation.setConsul_text(textLabel.getText());
            } else {
                consultation.setConsul_text("Le taux d'anxiété du patient est = " + (int) anxieteScroll.getValue() + "; " + textLabel.getText());
            }
            System.out.println(consultation);
            consultation.setConsul_text(consultation.getConsul_text().replace("'", "''"));

            // Update la BDD
            consultation.UpdateConsultation();
            // Puis on ferme la page
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
