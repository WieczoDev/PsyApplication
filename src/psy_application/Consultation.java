package psy_application;

import java.sql.Time;
import java.util.Date;

public class Consultation {
    int consul_ID;
    int patient_ID1;
    int patient_ID2;
    int patient_ID3;
    String consul_date;
    int consul_hour;
    int consul_reason;
    int consul_range;

    public int getConsul_ID() {
        return consul_ID;
    }

    public void setConsul_ID(int consul_ID) {
        this.consul_ID = consul_ID;
    }

    public int getPatient_ID1() {
        return patient_ID1;
    }

    public void setPatient_ID1(int patient_ID1) {
        this.patient_ID1 = patient_ID1;
    }

    public int getPatient_ID2() {
        return patient_ID2;
    }

    public void setPatient_ID2(int patient_ID2) {
        this.patient_ID2 = patient_ID2;
    }

    public int getPatient_ID3() {
        return patient_ID3;
    }

    public void setPatient_ID3(int patient_ID3) {
        this.patient_ID3 = patient_ID3;
    }

    public String getConsul_date() {
        return consul_date;
    }

    public void setConsul_date(String consul_date) {
        this.consul_date = consul_date;
    }

    public int getConsul_hour() {
        return consul_hour;
    }

    public void setConsul_hour(int consul_hour) {
        this.consul_hour = consul_hour;
    }

    public int getConsul_reason() {
        return consul_reason;
    }

    public void setConsul_reason(int consul_reason) {
        this.consul_reason = consul_reason;
    }

    public int getConsul_range() {
        return consul_range;
    }

    public void setConsul_range(int consul_range) {
        this.consul_range = consul_range;
    }

    public Consultation(int consul_ID, int patient_ID1, int patient_ID2, int patient_ID3, String consul_date, int consul_hour, int consul_reason, int consul_range) {
        this.consul_ID = consul_ID;
        this.patient_ID1 = patient_ID1;
        this.patient_ID2 = patient_ID2;
        this.patient_ID3 = patient_ID3;
        this.consul_date = consul_date;
        this.consul_hour = consul_hour;
        this.consul_reason = consul_reason;
        this.consul_range = consul_range;
    }
}
