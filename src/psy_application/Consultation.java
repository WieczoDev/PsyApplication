package psy_application;

import java.sql.Time;
import java.util.Date;

public class Consultation {
    int consul_ID;
    int patient_ID1;
    int patient_ID2;
    int patient_ID3 ;
    Date consul_date;
    Time consul_hour;
    int consul_reason;
    int consul_range;

    public Consultation(int consul_ID, int patient_ID1, int patient_ID2, int patient_ID3, Date consul_date, Time consul_hour, int consul_reason, int consul_range) {
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
