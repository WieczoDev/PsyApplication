package psy_application.Model;

import psy_application.Main;
import psy_application.controller.Psy_Frame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Consultation {
    /**
     * VARIABLE
     */

    int consul_ID;
    String patient_1;
    String patient_2;
    String patient_3;
    String consul_date;
    double consul_hour;
    String consul_reason;
    String consul_range;
    String consul_text;
    int consul_price;
    String consul_how;

    /**
     * SETTER
     * Certains ont été modifié pour permettre un affichage plus clair ( ex : setPatientID1)
     */
    public void setConsul_ID(int consul_ID) {
        this.consul_ID = consul_ID;
    }

    public void setPatient_ID1(int patient_ID1) throws SQLException {
        String MyQuery3 = "SELECT Patient_surname FROM PATIENTS WHERE patient_ID = " + patient_ID1;
        ResultSet rset3 = Main.database.stmt3.executeQuery(MyQuery3);
        rset3.next();
        this.patient_1 = rset3.getString(1);
    }

    public void setPatient_ID2(int patient_ID2) throws SQLException {
        String MyQuery3 = "SELECT Patient_surname FROM PATIENTS WHERE patient_ID = " + patient_ID2;
        ResultSet rset3 = Main.database.stmt3.executeQuery(MyQuery3);
        if (rset3.next()) this.patient_2 = rset3.getString(1);
    }

    public void setPatient_ID3(int patient_ID3) throws SQLException {
        String MyQuery3 = "SELECT Patient_surname FROM PATIENTS WHERE patient_ID = " + patient_ID3;
        ResultSet rset3 = Main.database.stmt3.executeQuery(MyQuery3);
        if (rset3.next()) this.patient_3 = rset3.getString(1);
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
        if (rset.next()) this.consul_reason = rset.getString(1);
    }

    public void setConsul_range(String consul_range) throws SQLException {
        this.consul_range = consul_range;
    }

    public void setConsul_text(String consul_text) {
        this.consul_text = consul_text;
    }

    public void setConsul_price(int consul_price) {
        this.consul_price = consul_price;
    }

    public void setConsul_how(int consul_how) throws SQLException {
        String MyQuery3 = "SELECT PAYMENT_HOW FROM PAYMENT WHERE PAYMENT_ID = " + consul_how;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        rset.next();
        this.consul_how = rset.getString(1);
    }

    /**
     * GETTER
     */

    public static int getIdFromName(String name) throws SQLException {
        String MyQuery3 = "SELECT PATIENT_ID FROM PATIENTS WHERE Patient_surname = '" + name + "'";
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        int count = 0;
        int id = -1;
        while (rset.next()) {
            count += 1;
            id = rset.getInt(1);
        }
        if (count == 1) {
            return id;
        } else return -1;
    }

    public static int getIdFromMail(String name) throws SQLException {
        String MyQuery3 = "SELECT USER_ID FROM USERS WHERE USER_LOGIN = '" + name + "'";
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        if (rset.next()) return rset.getInt(1);
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

    public int getAnxiete() {
        try {
            int index = this.getConsul_text().indexOf(";");  // Gets the first index where a ; occours
            String id = this.getConsul_text().substring(index - 1, index); // Gets the first part
            return Integer.parseInt(id);
        } catch (Exception e) {
            System.out.println("Il n'y a pas encore d'anxiété");
            return 0;
        }

    }  // Vu que le niveau d'anxiété est contenu dans le commentaire on le recupère ici seulement si la raison de la consultation est anxiété !

    public String getComment() {
        int index = this.getConsul_text().indexOf(";");  // Gets the first index where a ; occours
        String Comment = this.getConsul_text().substring(index + 1); // Gets the first part
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

    public String getConsul_range() {
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


    /**
     * CONSTRUCTORS
     * Encore une fois chaque constructeur et différent en fonction de l'utilité qu'on veut de l'objet
     * Que ce soit de l'affichage ou de l'insertion en base de donnée
     */

    public Consultation() {
        this.consul_ID = 0;
        this.patient_1 = null;
        this.patient_2 = null;
        this.patient_3 = null;
        this.consul_date = null;
        this.consul_hour = 0;
        this.consul_reason = null;
        this.consul_range = null;
        this.consul_text = null;
        this.consul_price = 0;
        this.consul_how = null;
    }

    public Consultation(int consul_ID, String patient_1, String patient_2, String patient_3, String consul_date, double consul_hour, String consul_reason, String consul_range, String consul_text, int consul_price, String consul_how) throws SQLException {
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

    public Consultation(int consul_ID, int patient_ID1, int patient_ID2, int patient_ID3, String consul_date, double consul_hour, int consul_reason, String consul_range, String consul_text, int consul_price, int consul_how) throws SQLException {
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

    /**
     * METHODS
     */

    // Ajout dans la BDD en 2 temps car la table Patient_consul et dépendante de la table Consultation
    public void addConsulDB() throws SQLException {
        String myQuery3 = "INSERT INTO CONSULTATIONS VALUES ( " + this.consul_ID + "," + " TO_DATE( '" + this.consul_date + "','yyyy-MM-dd')," + this.consul_hour + ", " + this.consul_reason + ", " + null + ", " + null + ", " + null + ", '" + this.consul_range + "')";
        ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3); // On ajoute cette consultation dans la base de donnée
        myQuery3 = " INSERT INTO Patient_Consul VALUES (" + this.patient_1 + "," + this.consul_ID + ")";
        rset3 = Main.database.stmt.executeQuery(myQuery3);
        if (!this.patient_2.equals("0")) {
            myQuery3 = " INSERT INTO Patient_Consul VALUES (" + this.patient_2 + "," + this.consul_ID + ")";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
        }
        if (!this.patient_3.equals("0")) {
            myQuery3 = " INSERT INTO Patient_Consul VALUES (" + this.patient_3 + "," + this.consul_ID + ")";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
        }
    }

    private static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // Pour trouver la range d'un patient en fonction de son age, Ado ou Enfant, sinon c'est saisies par la psy
    public static String findPatientRange(int patient_id, String range) throws SQLException, ParseException {
        int age = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String myQuery3 = "SELECT PATIENT_DOB FROM PATIENTS WHERE Patient_ID = " + patient_id;
        ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
        if (rset3.next()) {
            LocalDate now = convertToLocalDateViaInstant(sdf.parse(java.time.LocalDate.now().toString()));        //Date d'aujourd'hui
            LocalDate dob = convertToLocalDateViaInstant(sdf.parse(rset3.getString(1)));   // Date de Naissance
            if ((dob != null) && (now != null)) {
                age = Period.between(dob, now).getYears();
            } else {
                age = -1;
            }
        }
        if (age < 11) return "Enfant";
        else if (age >= 11 && age <= 18) return "Ado";
        else if (range.equals("0")) {
            return "-1";
        }
        return "-1";
    }

    // On cherche à trouvé la clé de la raison saisie, si cette derniere n'existe pas on la rajoute
    public static int findaddReason(String reason) throws SQLException {
        String myQuery3 = "SELECT Reason_ID FROM Reasons WHERE reason ='" + reason + "'";
        ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
        if (rset3.next()) {
            return rset3.getInt(1);
        } else if (reason.equals("")) {
            return 0;
        } else {
            // CREATION D'UNE RAISON ET AJOUT DANS LA DB
            myQuery3 = "SELECT MAX(reason_ID) FROM reasons";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
            rset3.next();
            int reason_id = rset3.getInt(1) + 1;
            myQuery3 = "INSERT INTO REASONS VALUES ( " + reason_id + ", '" + reason + "')";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
            return reason_id;
        }
    }

    // Permet de normalisé le moyen de payement car il existe que un nombre fixe de moyen de paiement !
    public int getHow(String how) {
        try {
            switch (how) {
                case "Chèque":
                    return 2;
                case "Carte Bancaire":
                    return 1;
                case "Espèces":
                    return 3;
                case "PayPal":
                    return 4;
                case "Lydia":
                    return 5;
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // Verifie si le patient saisie existe return true si oui
    public static boolean PatientExist(int id) throws SQLException {
        String MyQuery3 = "SELECT PATIENT_ID FROM PATIENTS WHERE PATIENT_ID = " + id;
        ResultSet rset = Main.database.stmt3.executeQuery(MyQuery3);
        if (rset.next() && !rset.next()) return true;
        else return false;
    }

    public static int findaddPatient(String Patient) throws SQLException {
        try {
            Integer.parseInt(Patient);          //On regarde si c'est l'ID du patient qui est donné ou pas
            if (PatientExist(Integer.parseInt(Patient))) {
                return Integer.parseInt(Patient);
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {           //Si l'input n'est pas un int alors on va chercher via email et nom
            if (getIdFromName(Patient) != -1) {
                return getIdFromName(Patient);
            } else if (getIdFromMail(Patient) != -1) {
                return getIdFromMail(Patient);
            } else {
                Psy_Frame.showAlert("Le nom que vous avez saisie n'est pas unique");
                return 0;
            }
        }
    }


    // Permet peut-importe la valeur des saisies des patients ( mail / id ou nom ) de le transformer en ID pour pouvoir les saisirs dans la base de données
    // et ce pour chaque patient
    public boolean updatepatient() throws SQLException {
        //PATIENT 1
        try {
            Integer.parseInt(this.patient_1);                                       //On regarde si c'est l'ID du patient qui est donné ou pas
            if (PatientExist(Integer.parseInt(this.patient_1))) {
            } else {
                this.patient_1 = String.valueOf(-1);
            }
        } catch (NumberFormatException e) {                                         //Si l'input n'est pas un int alors on va chercher via email et nom
            if (this.getIdFromName(this.patient_1) != -1) {
                this.patient_1 = String.valueOf(this.getIdFromName(this.patient_1));
            } else if (this.getIdFromMail(this.patient_1) != -1) {
                this.patient_1 = String.valueOf(this.getIdFromMail(this.patient_1));
            } else {
                this.patient_1 = String.valueOf(-1);
            }
        }
        if (this.patient_2 != null && !this.patient_2.equals("")) {
            try {
                Integer.parseInt(this.patient_2);                                       //On regarde si c'est l'ID du patient qui est donné ou pas
                if (PatientExist(Integer.parseInt(this.patient_2))) {
                } else {
                    this.patient_2 = String.valueOf(-1);
                }
            } catch (NumberFormatException e) {                                         //Si l'input n'est pas un int alors on va chercher via email et nom
                if (this.getIdFromName(this.patient_2) != -1) {
                    this.patient_2 = String.valueOf(this.getIdFromName(this.patient_2));
                } else if (this.getIdFromMail(this.patient_2) != -1) {
                    this.patient_2 = String.valueOf(this.getIdFromMail(this.patient_2));
                } else {
                    this.patient_2 = String.valueOf(-1);
                }
            }
        }
        if (this.patient_3 != null && !this.patient_3.equals("")) {
            try {
                Integer.parseInt(this.patient_3);                                       //On regarde si c'est l'ID du patient qui est donné ou pas
                if (PatientExist(Integer.parseInt(this.patient_3))) {
                } else {
                    this.patient_3 = String.valueOf(-1);
                }
            } catch (NumberFormatException e) {                                         //Si l'input n'est pas un int alors on va chercher via email et nom
                if (this.getIdFromName(this.patient_3) != -1) {
                    this.patient_3 = String.valueOf(this.getIdFromName(this.patient_3));
                } else if (this.getIdFromMail(this.patient_3) != -1) {
                    this.patient_3 = String.valueOf(this.getIdFromMail(this.patient_3));
                } else {
                    this.patient_3 = String.valueOf(-1);
                }
            }
        }
        if (this.patient_2 == null && this.patient_3 == null) {
            if (this.patient_1.equals("-1")) return false;
        } else if (this.patient_2 != null && this.patient_3 == null) {
            if (this.patient_1.equals("-1") || this.patient_2.equals("-1")) return false;
        } else if (this.patient_2 != null && this.patient_3 != null) {
            if (this.patient_1.equals("-1") || this.patient_2.equals("-1") || this.patient_3.equals("-1")) return false;
        }
        return true;
    }

    // PERMET DE TRANSFORMER LA RAISON SAISIE EN SONT ID EQUIVALENT
    public boolean updatereason() {
        try {
            String myQuery3 = "SELECT Reason_ID FROM Reasons WHERE reason ='" + this.getConsul_reason() + "'";
            ResultSet rset3 = Main.database.stmt3.executeQuery(myQuery3);
            if (rset3.next()) {
                this.consul_reason = String.valueOf(rset3.getInt(1));
            } else if (this.getConsul_reason() == null) {
                this.consul_reason = String.valueOf(0);
            } else {
                // CREATION D'UNE RAISON ET AJOUT DANS LA DB
                myQuery3 = "SELECT MAX(reason_ID) FROM reasons";
                rset3 = Main.database.stmt3.executeQuery(myQuery3);
                rset3.next();
                int reason_id = rset3.getInt(1) + 1;
                myQuery3 = "INSERT INTO REASONS VALUES ( " + reason_id + ", '" + this.getConsul_reason() + "')";
                this.consul_reason = String.valueOf(reason_id);
                rset3 = Main.database.stmt3.executeQuery(myQuery3);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SI ON MODIFIE LA LISTE DES PATIENTS
    public void updatePatientConsul() throws SQLException {
        String myQuery3 = "DELETE FROM PATIENT_CONSUL WHERE CONSUL_ID = " + consul_ID;
        ResultSet rset3 = Main.database.stmt.executeQuery(myQuery3);
        myQuery3 = "INSERT INTO Patient_Consul VALUES (" + patient_1 + "," + consul_ID + ")";
        rset3 = Main.database.stmt.executeQuery(myQuery3);
        if (patient_2 != null && !patient_2.equals("-1") && !patient_2.equals("")) {
            myQuery3 = "INSERT INTO Patient_Consul VALUES (" + patient_2 + "," + consul_ID + ")";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
        }
        if (patient_3 != null && !patient_3.equals("-1") && !patient_3.equals("")) {
            myQuery3 = "INSERT INTO Patient_Consul VALUES (" + patient_3 + "," + consul_ID + ")";
            rset3 = Main.database.stmt.executeQuery(myQuery3);
        }
    }

    // SI ON MODIFIE LA CONSULTATION
    public boolean UpdateConsultation() throws SQLException {
        // ON VERIFIE LES NOUVEAUX INPUTS POUR LES PATIENTS
        consul_how = String.valueOf(getHow(this.getConsul_how()));
        if (!updatepatient()) {
            return false;
        } else {
            if (updatereason()) {
                String myQuery = "UPDATE CONSULTATIONS SET " +
                        "CONSULTATION_HOW = " + consul_how +
                        ", CONSUL_TEXT ='" + consul_text +
                        "', CONSUL_PRICE =" + consul_price +
                        ", CONSUL_REASON =" + consul_reason +
                        ", CONSULTATION_RANGE ='" + consul_range +
                        "' WHERE CONSUL_ID = " + consul_ID;
                ResultSet rset = Main.database.stmt.executeQuery(myQuery);
                updatePatientConsul();
                Psy_Frame.showInfo("Modification effecuté");
                return true;
            }
        }
        return false;
    }

    public static Consultation getConsul(int consul_id) {
        Consultation Consul1;
        ArrayList<Integer> listPatient = new ArrayList<>();
        try {
            String myQuery = "SELECT consul_ID, consul_hour, consul_reason, consul_text, consul_price, consultation_how, consul_date FROM Consultations WHERE consul_id = " + consul_id;
            ResultSet rset1 = Main.database.stmt.executeQuery(myQuery);
            if (rset1.next()) {
                try {
                    String myQuery2 = "SELECT patient_ID FROM PATIENT_CONSUL WHERE CONSUL_ID = " + consul_id;
                    ResultSet rset2 = Main.database.stmt2.executeQuery(myQuery2);
                    int count = 0;
                    while (rset2.next()) {
                        listPatient.add(rset2.getInt(1));
                    }
                    while (listPatient.size() < 3) {
                        listPatient.add(0);
                    }
                    Consul1 = new Consultation(rset1.getInt(1), listPatient.get(0), listPatient.get(1), //
                            listPatient.get(2), rset1.getString(7).substring(0, 10), rset1.getInt(2), rset1.getInt(3), null, rset1.getString(4), rset1.getInt(5), rset1.getInt(6));
                    return Consul1;
                } catch (SQLException e) {
                    System.out.println("Erreur de connexion avec la database");
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion avec la database");
            e.printStackTrace();
            return null;
        }
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
