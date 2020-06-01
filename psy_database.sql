/**
    LORS DE LA CREATION DE LA BASE DE DONNE IL FAUT DES LE DEPART CREER LE USER admin admin QUI A TOUT LES DROITS
    à faire en console après être identifier comme le createur de la base de donnée
*/

--1
alter session set "_ORACLE_SCRIPT"=true;
--2
CREATE USER admin IDENTIFIED BY admin;
--3
GRANT ALL PRIVILEGES TO admin;

//** CODE A LANCER POUR CREER UN ECHANTILLON DE BASE */

DROP TABLE PATIENT_CONSUL;
DROP TABLE HOW;
DROP TABLE PAYMENT;
DROP TABLE REASONS;
DROP TABLE CONSULTATIONS;
DROP TABLE PATIENTS;
DROP TABLE USERS;
DROP TABLE PROFESSIONS;

CREATE TABLE Users
(
    user_ID    int PRIMARY KEY,
    user_login varchar2(50) NOT NULL,
    user_pass  varchar2(20) NOT NULL
);

CREATE TABLE Reasons
(
    reason_ID int PRIMARY KEY,
    reason    varchar2(20)
);

CREATE TABLE Professions
(
    prof_ID    int PRIMARY KEY,
    profession varchar2(20)
);

CREATE TABLE How
(
    how_ID int PRIMARY KEY,
    how_s  varchar2(30)
);

CREATE TABLE Patients
(
    patient_ID         int          not NULL,
    CONSTRAINT FK_patient_ID FOREIGN KEY (patient_ID) REFERENCES Users (user_ID),
    patient_surname    varchar2(20) not null,
    patient_name       varchar2(20) not null,
    patient_DOB        date         not null,
    patient_mailing    varchar2(50),
    patient_how        int,
    CONSTRAINT patient_how FOREIGN KEY (patient_how) REFERENCES How (how_ID),
    patient_profession int,
    CONSTRAINT FK_patient_profession FOREIGN KEY (patient_profession) REFERENCES Professions (prof_ID)
);

ALTER TABLE Patients
    ADD CONSTRAINT PK_patient_ID PRIMARY KEY (patient_ID);

CREATE TABLE Consultations
(
    consul_ID        int PRIMARY KEY not NULL,
    consul_date      date NOT NULL,
    consul_hour      float NOT NULL,
    consul_reason    int,
    CONSTRAINT FK_consul_reason FOREIGN KEY (consul_reason) REFERENCES Reasons (reason_ID),
    Consultation_how int,
    consul_text varchar2(500),
    consul_price int,
    consultation_range varchar2(20)
);

CREATE TABLE PAYMENT
(
    payment_ID  int PRIMARY KEY,
    payment_how varchar2(20)
);

ALTER TABLE Consultations
    ADD CONSTRAINT FK_Consultation_how FOREIGN KEY (Consultation_how) REFERENCES PAYMENT (payment_ID);

CREATE TABLE Patient_Consul
(
    patient_ID int,
    consul_ID  int,
    CONSTRAINT PK_Patient_Consul PRIMARY KEY (patient_ID, consul_ID)
);

ALTER TABLE Patient_Consul
    ADD CONSTRAINT FK_patient_C_ID FOREIGN KEY (patient_ID) REFERENCES PATIENTS (patient_ID);
ALTER TABLE Patient_Consul
    ADD CONSTRAINT FK_consul_P_ID FOREIGN KEY (consul_ID) REFERENCES Consultations (consul_ID);

/* INSERT */

INSERT INTO Users
VALUES (1, 'admin', 'admin');
INSERT INTO Users
VALUES (2, 'toto@gmail.com', 'toto');
INSERT INTO Users
VALUES (3, 'guy@gmail.com', 'guy');
INSERT INTO Users
VALUES (4, 'anto@gmail.com', 'anto');
INSERT INTO Users
VALUES (5, 'laulau@gmail.com', 'laulau');


INSERT INTO PROFESSIONS
VALUES (0, null);
INSERT INTO PROFESSIONS
VALUES (1, 'Kiné');
INSERT INTO PROFESSIONS
VALUES (2, 'Pompier');
INSERT INTO PROFESSIONS
VALUES (3, 'Policier');
INSERT INTO PROFESSIONS
VALUES (4, 'Boulanger');
INSERT INTO PROFESSIONS
VALUES (5, 'Vendeur');

INSERT INTO PAYMENT
VALUES (0, null);
INSERT INTO PAYMENT
VALUES (1, 'CB');
INSERT INTO PAYMENT
VALUES (2, 'Chèque');
INSERT INTO PAYMENT
VALUES (3, 'Espèces');
INSERT INTO PAYMENT
VALUES (4, 'Paypal');
INSERT INTO PAYMENT
VALUES (5, 'Lydia');

INSERT INTO REASONS
VALUES (0, null);
INSERT INTO REASONS
VALUES (1, 'Divorce');
INSERT INTO REASONS
VALUES (2, 'Violence');
INSERT INTO REASONS
VALUES (3, 'Suicide');


INSERT INTO HOW
VALUES (0, null);
INSERT INTO HOW
VALUES (1, 'Facebook');
INSERT INTO HOW
VALUES (2, 'Amis');
INSERT INTO HOW
VALUES (3, 'Courrier');
INSERT INTO HOW
VALUES (4, 'Mail');
INSERT INTO HOW
VALUES (5, 'Mairie');

INSERT INTO PATIENTS
VALUES (2, 'Guillaume', 'Thomas', TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry', 2, 1);
INSERT INTO PATIENTS
VALUES (3, 'Fourcauld', 'Guilhem', TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry', 2,2);
INSERT INTO PATIENTS
VALUES (4, 'Wieczorek', 'Antonin', TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry', 1, 3);
INSERT INTO PATIENTS
VALUES (5, 'Forestier', 'Laurine', TO_DATE('17-06-1987', 'dd-mm-yyyy'), 'Ivry', 3,5);

INSERT INTO CONSULTATIONS VALUES ( 4,  TO_DATE('2020-05-30', 'yyyy-MM-dd'), 10,  2 ,null , null ,null ,  null);
INSERT INTO Patient_Consul VALUES (3, 4 );
INSERT INTO CONSULTATIONS VALUES ( 5,  TO_DATE('2020-05-30', 'yyyy-MM-dd'), 10.5,  2 ,null, null ,null ,  null);
INSERT INTO Patient_Consul VALUES (3, 5 );
INSERT INTO CONSULTATIONS VALUES ( 6,  TO_DATE('2020-05-30', 'yyyy-MM-dd'), 8,  2 ,null, null ,null ,  null);
INSERT INTO Patient_Consul VALUES (3, 6 );


/* PROCEDURE */

CREATE OR REPLACE PROCEDURE remove_consul (consul_id_in NUMBER) AS
BEGIN
    DELETE FROM PATIENT_CONSUL
    WHERE PATIENT_CONSUL.Consul_ID = consul_id_in;
    DELETE FROM CONSULTATIONS
    WHERE Consultations.Consul_ID = consul_id_in;
    COMMIT;
END;

CREATE OR REPLACE PROCEDURE remove_patient (patient_id_in NUMBER) AS
BEGIN
    DELETE FROM PATIENT_CONSUL
    WHERE Patient_Consul.patient_ID = patient_id_in;
    DELETE FROM PATIENTS
    WHERE PATIENTS.patient_ID = patient_id_in;
    DELETE FROM USERS
    WHERE USERS.user_ID = patient_id_in;
    COMMIT;
END;

/** FIN DU CODE A LANCER */
