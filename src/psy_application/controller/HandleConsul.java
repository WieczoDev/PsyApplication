package psy_application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import psy_application.Model.Consultation;
import psy_application.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class HandleConsul implements Initializable {
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
    public TableColumn payement;
    @FXML
    public TableColumn prix;
    @FXML
    public TableColumn commentaire;
    @FXML
    public Button finaliseButton;
    @FXML
    private javafx.scene.control.Button closeButton;

    private String strDate;
    private static int consul_id;
    ObservableList<Consultation> list = FXCollections.observableArrayList();


    /**
     * HANDLE CONSUL ->  FRAME POSSEDANT LA LISTE DES CONSULTATIONS SUR UNE JOURNEE PRECISE
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /** AFIN DE DEFINIR CHAQUE COLLONE DU TABLEAU LORS DE L'OUVERTURE DE LA FRAME */

        heureCol.setCellValueFactory(new PropertyValueFactory<>("consul_hour"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("consul_ID"));
        patient1Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID1"));
        patient2Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID2"));
        patient3Col.setCellValueFactory(new PropertyValueFactory<>("patient_ID3"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("consul_reason"));
        rangeCol.setCellValueFactory(new PropertyValueFactory<>("consul_range"));
        payement.setCellValueFactory(new PropertyValueFactory<>("consul_how"));
        prix.setCellValueFactory(new PropertyValueFactory<>("consul_price"));
        commentaire.setCellValueFactory(new PropertyValueFactory<>("consul_text"));
    }

    //GETTERS
    public static int getConsul_id() {
        return consul_id;
    }

    @FXML
    private void handleDeleteConsul() {
        // Pour obtenir la ligne selectionée
        try {
            consul_id = (int) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
            try {
                Main.database.RemoveConsulstmt.setString(1, String.valueOf(consul_id));
                Main.database.RemoveConsulstmt.execute();         // On appelle la procédure pour supprimer un rdv
                tableview.getItems().remove(tableview.getSelectionModel().getSelectedIndex());
            } catch (SQLException e) {
                System.out.println("Erreur dans la suppression !");
                e.printStackTrace();
            }
        } catch (Exception e) {
            // Si on clique alors qu'on a rien selectioné
        }
    }

    // Methode permettant de remplir le tableau avec la date saisie

    @FXML
    private void findButtonAction() throws SQLException {
        try {
            strDate = Psy_Frame.convertJDatetoString(date_field);
            list = Psy_Frame.getConsulList(strDate, list);
            tableview.setItems(list);
        } catch (NullPointerException e) {
            System.out.println("Aucune date selectionnée ");
        }
    }

    // OUVERTURE DE LA FRAME MODIFICATION DE CONSULTATION ( MODIFYCONSUL)
    @FXML
    private void modifyButtonAction() throws IOException {
        try {
            ModifyConsul.consultation_id = (int) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
            Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/ModifyConsul.fxml"));
            Stage ModifyConsul = new Stage();
            ModifyConsul.setScene(new Scene(root));
            ModifyConsul.initStyle(StageStyle.UNDECORATED);
            ModifyConsul.show();
        } catch (Exception e) {
            Psy_Frame.showAlert("Aucune consultation selectionnée !");
        }
    }

    // OUVERTURE DE LA FRAME FINALISATION DE LA CONSULTATION
    @FXML
    private void finaliseButtonAction() {
        try {
            consul_id = (int) idCol.getCellObservableValue(tableview.getItems().get(tableview.getSelectionModel().getSelectedIndex())).getValue();
            if (Consultation.getConsul(consul_id).getConsul_price() != 0) {
                Psy_Frame.showInfo("La consultation selectionée à déjà été finalisé, vous pouvez désormais la modifier");
            } else {
                Parent root = FXMLLoader.load(Psy_Frame.class.getResource("../fxml/FinaliseConsul.fxml"));
                Stage FinaliseConsul = new Stage();
                FinaliseConsul.setScene(new Scene(root));
                FinaliseConsul.initStyle(StageStyle.UNDECORATED);
                FinaliseConsul.show();
            }
        } catch (Exception e) {
            Psy_Frame.showAlert("Aucune consultation séléctionnée !");
            e.printStackTrace();
        }

    }

    @FXML
    private void closeButtonAction() {
        Stage primaryStage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
        login.psyStage.show();
    }
}
