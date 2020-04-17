DROP TABLE Users;

CREATE TABLE Users(
                      user_ID int PRIMARY KEY,
                      user_login varchar2(50) NOT NULL,
                      user_pass varchar2(20) NOT NULL
);

CREATE TABLE Reasons
(
    reason_ID int PRIMARY KEY,
    reason varchar2(20)
);

CREATE TABLE Professions
(
    prof_ID int PRIMARY KEY,
    profession varchar2(20)
);

CREATE TABLE How
(
    how_ID int PRIMARY KEY,
    how_s  varchar2(30)
);

CREATE TABLE Patients(
                         patient_ID int not NULL,
                         CONSTRAINT FK_patient_ID FOREIGN KEY (patient_ID) REFERENCES Users(user_ID),
                         patient_surname varchar2(20) not null ,
                         patient_name varchar2(20) not null ,
                         patient_DOB date not null ,
                         patient_mailing varchar2(50),
                         patient_how int,
                         CONSTRAINT patient_how FOREIGN KEY (patient_how) REFERENCES How(how_ID),
                         patient_profession int,
                         CONSTRAINT FK_patient_profession FOREIGN KEY (patient_profession) REFERENCES Professions(prof_ID)
);

ALTER TABLE Patients
    ADD CONSTRAINT PK_patient_ID PRIMARY KEY (patient_ID);


CREATE TABLE Consultations
(
    consul_ID     int PRIMARY KEY,
    consul_date   date,
    consul_hour   int,
    consul_reason int,
    CONSTRAINT FK_consul_reason FOREIGN KEY (consul_reason) REFERENCES Reasons (reason_ID),
    Consultation_how int
);

CREATE TABLE PAYMENT(
                        payment_ID int PRIMARY KEY,
                        payment_how varchar2(20)
);

ALTER TABLE Consultations
    ADD CONSTRAINT FK_Consultation_how FOREIGN KEY (Consultation_how) REFERENCES PAYMENT(payment_ID);

CREATE TABLE Patient_Consul(
                               patient_ID int,
                               consul_ID int,
                               CONSTRAINT PK_Patient_Consul PRIMARY KEY ( patient_ID , consul_ID)
);

ALTER TABLE Patient_Consul
    ADD CONSTRAINT FK_patient_C_ID  FOREIGN KEY (patient_ID) REFERENCES PATIENTS(patient_ID);
ALTER TABLE Patient_Consul
    ADD CONSTRAINT FK_consul_P_ID  FOREIGN KEY (consul_ID) REFERENCES Consultations(consul_ID);

SELECT * from Users;

INSERT INTO Users VALUES ( 1 , 'admin' , 'admin' );
INSERT INTO Users VALUES ( 2 , 'toto@gmail.com' , 'toto' );
INSERT INTO Users VALUES ( 3 , 'guy@gmail.com' , 'guy' );
INSERT INTO Users VALUES ( 4 , 'anto@gmail.com' , 'anto' );
INSERT INTO Users VALUES ( 5 , 'laulau@gmail.com' , 'laulau' );

INSERT INTO PATIENTS VALUES ( 2 , 'Guillaume' , 'Thomas' , TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry' , null, null);
INSERT INTO PATIENTS VALUES ( 3 , 'Fourcauld' , 'Guilhem' , TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry' , null, null);
INSERT INTO PATIENTS VALUES ( 4 , 'Wieczorek' , 'Antonin' , TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry' , null, null);
INSERT INTO PATIENTS VALUES ( 5 , 'Forestier' , 'Laurine' , TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry' , null, null);

SELECT patient_ID, user_login, user_pass, patient_surname, patient_name, patient_DOB, patient_mailing, patient_how, patient_profession, patient_consultations from Patients INNER JOIN Users U on Patients.patient_ID = U.user_ID;

INSERT INTO HOW VALUES ( 0 , null);
INSERT INTO HOW VALUES ( 1 , 'Facebook');
INSERT INTO HOW VALUES ( 2 , 'Amis');
INSERT INTO HOW VALUES ( 3 , 'Courrier');
INSERT INTO HOW VALUES ( 4 , 'Mail');
INSERT INTO HOW VALUES ( 5 , 'Mairie');

INSERT INTO PROFESSIONS VALUES ( 0 , null);
INSERT INTO PROFESSIONS VALUES ( 1 , 'Kiné');
INSERT INTO PROFESSIONS VALUES ( 2 , 'Pompier');
INSERT INTO PROFESSIONS VALUES ( 3 , 'Policier');
INSERT INTO PROFESSIONS VALUES ( 4 , 'Boulanger');
INSERT INTO PROFESSIONS VALUES ( 5 , 'Vendeur');

INSERT INTO PAYMENT VALUES ( 0 , null);
INSERT INTO PAYMENT VALUES ( 1 , 'CB');
INSERT INTO PAYMENT VALUES ( 2 , 'Chèque');
INSERT INTO PAYMENT VALUES ( 3 , 'Espèces');

DELETE FROM USERS
WHERE user_ID > 6;

DELETE FROM HOW
WHERE how_ID > 5;

SELECT * FROM PATIENTS;

SELECT * FROM USERS;

SELECT MAX(prof_ID) FROM PROFESSIONS;
SELECT MAX(how_ID) FROm HOW;
COMMIT;