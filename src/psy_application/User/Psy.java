package psy_application.User;

import psy_application.Main;
import psy_application.Consultation;

import java.util.ArrayList;
import java.util.Date;

public class Psy extends User {
    public Psy(String user_login, String user_password) {
        this.user_login = user_login;
        this.user_password = user_password;
        isFilled = false;
        this.user_ID = count.incrementAndGet();
    }
    /*
    private void addPatient(String user_login, String user_password, String patient_surname, String patient_name, Date patient_DOB, String patient_mailing, int patient_how, int patient_profession, ArrayList<Consultation> patient_consultations){
        Patient A = new Patient(user_login, user_password , patient_surname, patient_name, patient_DOB, patient_mailing,  patient_how, patient_profession, patient_consultations);

    }*/
}
