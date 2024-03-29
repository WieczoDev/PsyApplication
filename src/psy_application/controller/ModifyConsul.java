package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import psy_application.Model.Consultation;
import psy_application.Model.User.Patient;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
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
    @FXML
    public CheckBox couplebox;
    @FXML
    public CheckBox hommebox;
    @FXML
    public CheckBox femmebox;

    String[] List = new String[]{"Non payé", "Chèque", "Carte Bancaire", "Espèces", "PayPal", "Lydia"};
    public static int consultation_id;
    Consultation consultation;

    /**
     *  MODIFY CONSUL -> PERMET LA MODIFICATION DE LA CONSULTATION SELECTIONEE
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consultation = Consultation.getConsul(consultation_id);
        dateLabel.setText(dateLabel.getText() + " " + consultation.getConsul_date());
        heureLabel.setText(heureLabel.getText() + " " + consultation.getConsul_hour());
        payBox.setItems(FXCollections.observableArrayList(List));
        reasonfield.setText(consultation.getConsul_reason());
        payBox.setPromptText(consultation.getConsul_how());
        pricefield.setText(consultation.getConsul_price() + "");
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

    /** METHODS */

    private String getRange(){
        if(couplebox.isSelected()){
                System.out.println(p2field.getText() + " , " + p3field.getText());
                return "Couple";
        }
        else if(hommebox.isSelected() && femmebox.isSelected()){
            return "-1";
        }else if(hommebox.isSelected() && !femmebox.isSelected()){
            return "Homme";
        }else if (!hommebox.isSelected() && femmebox.isSelected()){
            return "Femme";
        }
        return "0";
    }

    private boolean isanInt(String chaine) {
        try {
            Integer.parseInt(chaine);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /** FXML */

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    @FXML
    private void modifyButtonAction() throws SQLException {
        // Comme la range est connue on attribue la range de la consulation en fonction de ce qui à été coché
        String range = getRange();
        try {
            if (range.equals("Couple") && p2field.getText().equals("null") && p3field.getText().equals("null")){
                Psy_Frame.showAlert("Vous devez avoir plusieurs patients pour un couple");
                range = "-1";
            } else if (range.equals("0")) {
                int Patient1 = Consultation.findaddPatient(p1field.getText()); // On cherche si le patient existe
                range = Consultation.findPatientRange(Patient1, range);
                if (range.equals("-1")) {
                    Psy_Frame.showAlert("Vous devez cocher la case Homme ou Femme");
                }
            }
            System.out.println(range);
        } catch (ParseException | NullPointerException e) {
            Psy_Frame.showAlert("Vous devez avoir plusieurs patients pour un couple");
            range = "-1";
        }

        // Si la range à été trouvé correctement et pas d'erreur

        if( !range.equals("-1")){
            int price = 0;
            if (isanInt(pricefield.getText()) && Integer.parseInt(pricefield.getText()) > 0 ){
                price = Integer.parseInt(pricefield.getText());
            }else {
                price = consultation.getConsul_price();
                Psy_Frame.showInfo("Le changement du prix n'a pas effectué car la saisie etait erroné");
            }
            String payement;
            if ((String) payBox.getSelectionModel().getSelectedItem() == null) {
                payement = consultation.getConsul_how();
            } else payement = (String) payBox.getSelectionModel().getSelectedItem();
            // Creation d'un objet consultation temporaire qui va permettre l'update
            Consultation tmpConsul = new Consultation(consultation_id, p1field.getText(), p2field.getText(), p3field.getText(), consultation.getConsul_date(), consultation.getConsul_hour(),
                    reasonfield.getText(), String.valueOf(range) , textField.getText(), price, payement);

            // Gestion du taux d'anxiété dans les commentaires de la consul
            if (tmpConsul.getConsul_reason() != null) {
                if (tmpConsul.getConsul_reason().equals("6") || tmpConsul.getConsul_reason().equals("Anxiété")) {
                    tmpConsul.setConsul_text("Le taux d'anxiété du patient est = " + (int) anxieteScroll.getValue() + "; " + textField.getText());
                }
            }
            tmpConsul.setConsul_text(tmpConsul.getConsul_text().replace("'", "''"));
            // Puis on update la BDD et si aucune erreur est apparu on peut fermer la fenetre
            if(tmpConsul.UpdateConsultation()) closeButtonAction();
        }
    }
}
