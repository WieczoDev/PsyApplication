package psy_application;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import psy_application.controller.CancelConsul;
import psy_application.controller.Psy_Frame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class Consultation {
    int consul_ID;
    String patient_1;
    String patient_2;
    String patient_3;
    String consul_date;
    double consul_hour;
    String consul_reason;
    int consul_range;
    String consul_text;
    int consul_price;
    String consul_how;


    /*
            SETTER
     */
    public void setConsul_ID(int consul_ID) {
        this.consul_ID = consul_ID;
    }

    public void setPatient_ID1(int patient_ID1)throws SQLException {
        String MyQuery3 = "SELECT Patient_surname FROM PATIENTS WHERE patient_ID = " + patient_ID1;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        rset.next();
        this.patient_1 = rset.getString(1);
    }

    public void setPatient_ID2(int patient_ID2) throws SQLException {
        String MyQuery3 = "SELECT Patient_surname FROM PATIENTS WHERE patient_ID = " + patient_ID2;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        if(rset.next())this.patient_2 = rset.getString(1);
    }

    public void setPatient_ID3(int patient_ID3) throws SQLException {
        String MyQuery3 = "SELECT Patient_surname FROM PATIENTS WHERE patient_ID = " + patient_ID3;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        if(rset.next())this.patient_3 = rset.getString(1);
    }

    public void setConsul_date(String consul_date) {
        this.consul_date = consul_date;
    }

    public void setConsul_hour(int consul_hour) {
        this.consul_hour = consul_hour;
    }

    public void setConsul_reason(int consul_reason) throws SQLException {
        String MyQuery3 = "SELECT REASON FROM REASONS WHERE REASON_ID = " + consul_reason;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        if(rset.next())this.consul_reason = rset.getString(1);
    }

    public void setConsul_range(int consul_range) {
        this.consul_range = consul_range;
    }

    public void setConsul_text(String consul_text) {
        this.consul_text = consul_text;
    }

    public void setConsul_price(int consul_price) {
        this.consul_price = consul_price;
    }

    public void setConsul_how(int consul_how)throws SQLException {
        String MyQuery3 = "SELECT PAYMENT_HOW FROM PAYMENT WHERE PAYMENT_ID = " + consul_how;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        rset.next();
        this.consul_how = rset.getString(1);
    }

    public int getConsul_ID() {
        return consul_ID;
    }

    /*
        GETTER
     */

    public int getIdFromName(String name) throws SQLException {
        String MyQuery3 = "SELECT PATIENT_ID FROM PATIENTS WHERE Patient_surname = '" + name +"'";
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        int count = 0 ;
        if(rset.next()){
            if(rset.next()){
                return -1;
            }
            return rset.getInt(1);
        }
        else return -1;
    }

    public int getIdFromMail(String name) throws SQLException {
        String MyQuery3 = "SELECT USER_ID FROM USERS WHERE USER_LOGIN = '" + name +"'";
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        if(rset.next())return rset.getInt(1);
        else return -1;
    }

    public String getPatient_ID1() {
        return patient_1;
    }

    public String getPatient_ID2() {
        return patient_2;
    }

    public String getPatient_ID3() {
        return patient_3;
    }

    public int getAnxiete(){
        int index = this.getConsul_text().indexOf(";");  // Gets the first index where a space occours
        String id = this.getConsul_text().substring(index-1, index); // Gets the first part
        return Integer.parseInt(id);
    }  // Vu que le niveau d'anxiété est contenu dans le commentaire on le recupère ici seulement si la raison de la consultation est anxiété !

    public String getComment(){
        int index = this.getConsul_text().indexOf(";");  // Gets the first index where a space occours
        String Comment = this.getConsul_text().substring(index+1); // Gets the first part
        return Comment;
    }

    public String getConsul_date() {
        return consul_date;
    }

    public double getConsul_hour() {
        return consul_hour;
    }

    public String getConsul_reason() {
        return consul_reason;
    }

    public int getConsul_range() {
        return consul_range;
    }

    public String getConsul_text() {
        return consul_text;
    }

    public int getConsul_price() {
        return consul_price;
    }

    public String getConsul_how() {
        return consul_how;
    }


    /*
        CONSTRUCTORS
     */

    public Consultation() {
        this.consul_ID = 0;
        this.patient_1 = null;
        this.patient_2 = null;
        this.patient_3 = null;
        this.consul_date = null;
        this.consul_hour = 0;
        this.consul_reason = null;
        this.consul_range = 0;
        this.consul_text = null;
        this.consul_price = 0;
        this.consul_how = null;
    }

    // On utilise deux types de constructeurs car nous avons 2 styles de consultations , l'une normalisée pour pouvoir la mettre dans la base de donnée et
    // l'autre avec les strings pour pouvoir avoir un affichage qui à du sens pour la psy !

    public Consultation(int consul_ID, String patient_1, String patient_2, String patient_3, String consul_date, double consul_hour, String consul_reason, int consul_range, String consul_text, int consul_price, String consul_how) {
        this.consul_ID = consul_ID;
        this.patient_1 = patient_1;
        this.patient_2 = patient_2;
        this.patient_3 = patient_3;
        this.consul_date = consul_date;
        this.consul_hour = consul_hour;
        this.consul_reason = consul_reason;
        this.consul_range = consul_range;
        this.consul_text = consul_text;
        this.consul_price = consul_price;
        this.consul_how = consul_how;
    }

    public Consultation(int consul_ID, int patient_ID1, int patient_ID2, int patient_ID3, String consul_date, double consul_hour, int consul_reason, int consul_range, String consul_text, int consul_price, int consul_how) throws SQLException {
        this.consul_ID = consul_ID;
        setPatient_ID1(patient_ID1);
        setPatient_ID2(patient_ID2);
        setPatient_ID3(patient_ID3);
        this.consul_date = consul_date;
        this.consul_hour = consul_hour;
        setConsul_reason(consul_reason);
        this.consul_range = consul_range;
        this.consul_text = consul_text;
        this.consul_price = consul_price;
        setConsul_how(consul_how);
    }

    /*
        METHODES
     */

    // Permet de normalisé le moyen de payement car il existe que un nombre fixe de moyen de paiement !

    public int getHow(String how){
        try {
            switch (how){
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
            return 0;
        }
    }
    // Verifie si le patient saisie existe return true si oui

    public boolean PatientExist(int id) throws SQLException {
        String MyQuery3 = "SELECT PATIENT_ID FROM PATIENTS WHERE PATIENT_ID = " + id;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        if(rset.next())return true;
        else return false;
    }

    // Permet peut-importe la valeur des saisies des patients ( mail / id ou nom ) de le transformer en ID pour pouvoir les saisirs dans la base de données

    public boolean updatepatient() throws SQLException {
        //PATIENT 1
        try {
            Integer.parseInt(this.patient_1);                                       //On regarde si c'est l'ID du patient qui est donné ou pas
            System.out.println(this.patient_1);
            if(PatientExist(Integer.parseInt(this.patient_1))){
            }else{
                this.patient_1 = String.valueOf(-1);
            }
        } catch (NumberFormatException e) {                                         //Si l'input n'est pas un int alors on va chercher via email et nom
            if(this.getIdFromName( this.patient_1) != -1){
                this.patient_1 = String.valueOf(this.getIdFromName(this.patient_1));
            }else if (this.getIdFromMail(this.patient_1) != -1){
                this.patient_1 = String.valueOf(this.getIdFromMail(this.patient_1));
            } else {
                this.patient_1 = String.valueOf(-1);
            }
        }
        if (this.patient_2 != null){
            try {
                Integer.parseInt(this.patient_2);                                       //On regarde si c'est l'ID du patient qui est donné ou pas
                System.out.println(this.patient_2);
                if(PatientExist(Integer.parseInt(this.patient_2))){
                }else{
                    this.patient_2 = String.valueOf(-1);
                }
            } catch (NumberFormatException e) {                                         //Si l'input n'est pas un int alors on va chercher via email et nom
                if(this.getIdFromName( this.patient_2) != -1){
                    this.patient_2 = String.valueOf(this.getIdFromName(this.patient_2));
                }else if (this.getIdFromMail(this.patient_2) != -1){
                    this.patient_2 = String.valueOf(this.getIdFromMail(this.patient_2));
                } else {
                    this.patient_2 = String.valueOf(-1);
                }
            }
        }
        if (this.patient_3 != null){
            try {
                Integer.parseInt(this.patient_3);                                       //On regarde si c'est l'ID du patient qui est donné ou pas
                System.out.println(this.patient_3);
                if(PatientExist(Integer.parseInt(this.patient_3))){
                }else{
                    this.patient_3 = String.valueOf(-1);
                }
            } catch (NumberFormatException e) {                                         //Si l'input n'est pas un int alors on va chercher via email et nom
                if(this.getIdFromName( this.patient_3) != -1){
                    this.patient_3 = String.valueOf(this.getIdFromName(this.patient_3));
                }else if (this.getIdFromMail(this.patient_3) != -1){
                    this.patient_3 = String.valueOf(this.getIdFromMail(this.patient_3));
                } else {
                    this.patient_3 = String.valueOf(-1);
                }
            }
        }
        if(this.patient_2 == null && this.patient_3 == null){
            if(this.patient_1.equals("-1")) return false;
        }
        else if(this.patient_2 != null && this.patient_3 == null){
            if(this.patient_1.equals("-1") || this.patient_2.equals("-1")) return false;
        }
        else if(this.patient_2 != null && this.patient_3 != null){
            if(this.patient_1.equals("-1") || this.patient_2.equals("-1") || this.patient_3.equals("-1")) return false;
        }
        return true;
    }

    // PERMET DE TRANSFORMER LA RAISON SAISIE EN SONS ID EQUIVALENT

    public boolean updatereason(){
        try {
            String myQuery3 = "SELECT Reason_ID FROM Reasons WHERE reason ='" + this.getConsul_reason() + "'";
            ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
            if (rset3.next()) {
                this.consul_reason = String.valueOf(rset3.getInt(1));
            } else if (this.getConsul_reason() == null) {
                this.consul_reason = String.valueOf(0);
            } else {
                // CREATION D'UNE RAISON ET AJOUT DANS LA DB
                myQuery3 = "SELECT MAX(reason_ID) FROM reasons";
                rset3 = Main.database.stmt.executeQuery(myQuery3);
                rset3.next();
                myQuery3 = "INSERT INTO REASONS VALUES ( " + rset3.getInt(1) + 1 + ", '" + this.getConsul_reason() + "')";
                this.consul_reason = String.valueOf(rset3.getInt(1) + 1);
                rset3 = Main.database.stmt.executeQuery(myQuery3);
            }
            return true;
        } catch (SQLException e) {
           e.printStackTrace();
            return false;
        }
    }

    // SI ON MODIFIE LA LISTE DES PATIENTS

    public void updatePatientConsul() throws SQLException {
        System.out.println("L'ID de ma consultation est : " + consul_ID);
        String myQuery3 = "DELETE FROM PATIENT_CONSUL WHERE CONSUL_ID = " + consul_ID;
        ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
        myQuery3 = "INSERT INTO Patient_Consul VALUES ("+ patient_1 + "," + consul_ID + ")";
        rset3 = Main.database.stmt.executeQuery(myQuery3);
        if(patient_2 != null && !patient_2.equals("-1") ){
            myQuery3 = "INSERT INTO Patient_Consul VALUES ("+ patient_2 + "," + consul_ID + ")";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
        }
        if(patient_3 != null && !patient_3.equals("-1")){
            myQuery3 = "INSERT INTO Patient_Consul VALUES ("+ patient_3 + "," + consul_ID + ")";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
        }
    }

    // SI ON MODIFIE LA CONSULTATION

    public boolean UpdateConsultation() throws SQLException {
        // ON VERIFIE LES NOUVEAUX INPUTS POUR LES PATIENTS
       consul_how = String.valueOf(getHow(this.getConsul_how()));
        if(!updatepatient()){
            Psy_Frame.showAlert("Attention saisie des patients incorrectes veuillez recommencer !");
            return false;
        }else{
            if(updatereason()){
                String myQuery = "UPDATE CONSULTATIONS SET " +
                        "CONSULTATION_HOW = " + consul_how +
                        ", CONSUL_TEXT ='" + consul_text +
                        "', CONSUL_PRICE =" + consul_price +
                        ", CONSUL_REASON =" + consul_reason +
                        " WHERE CONSUL_ID = " + consul_ID;
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                updatePatientConsul();
                Psy_Frame.showInfo("Modification effecuté");
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "Consultation{" +
                "consul_ID=" + consul_ID +
                ", patient_ID1=" + patient_1 +
                ", patient_ID2=" + patient_2 +
                ", patient_ID3=" + patient_3 +
                ", consul_date='" + consul_date + '\'' +
                ", consul_hour=" + consul_hour +
                ", consul_reason=" + consul_reason +
                ", consul_range=" + consul_range +
                ", consul_text='" + consul_text + '\'' +
                ", consul_price='" + consul_price + '\'' +
                ", consul_how=" + consul_how +
                '}';
    }

}
