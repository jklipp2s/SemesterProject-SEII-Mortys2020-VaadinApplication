package org.mortys.services.util;

public class DBTables {
    public static final String TAB_ADRESSE = "dbs_tab_adresse";
    public static final String TAB_USER = "dbs_tab_user";
    public static final String TAB_UNTERNEHMER = "dbs_tab_unternehmer";
    public static final String TAB_STUDENT = "dbs_tab_student";
    public static final String TAB_STELLENANZEIGE = "dbs_tab_stellenanzeige";
    public static final String TAB_U_ERSTELLT_ST = "dbs_tab_unternehmer_erstellt_stellenanzeige";
    public static final String TAB_FERTIGKEIT = "dbs_tab_fertigkeit";
    public static final String TAB_ST_FORDERT_F = "dbs_tab_stellenanzeige_fordert_fertigkeit";
    public static final String TAB_ST_HAT_F = "dbs_tab_student_hat_fertigkeit";
    public static final String TAB_STUDENT_BEWIRBT_ST = "dbs_tab_student_bewirbt_sich_stellenanzeige";
    public static final String TAB_USER_HAT_RESETCODE = "dbs_tab_user_hat_resetcode";
    public static final String TAB_STUDENT_TYPE = "dbs_tab_student_type";
    public static final String TAB_TOGGLE_CONFIGURATION = "dbs_tab_toggle_configuration";


    public class Creation {

        public static final String CREATE_TAB_ADRESSE = "create table " + TAB_ADRESSE +
                "(Addr_nr BIGSERIAL,\n" +
                "PLZ char(5) not null,\n" +
                "Ort varchar(35) not null,\n" +
                "Street varchar(80) not null,\n" +
                "h_nr numeric(4) not null,\n" +
                "UNIQUE(plz,ort,street, h_nr),\n" +
                "primary key (Addr_nr));";


        public static final String CREATE_TAB_USER = "create table " + TAB_USER + " (\n" +
                "Anrede varchar(10),\n" +
                "Geburtsdatum date,\n" +
                "Nachname varchar(60),\n" +
                "Vorname varchar(35),\n" +
                "email varchar(300),\n" +
                "Telefon varchar(15),\n" +
                "regDate  timestamp default current_timestamp ,\n" +
                "username varchar(30) not null,\n" +
                "password varchar(30) not null,\n" +
                "status varchar(10) ,\n" +
                "avatar varchar(400) ,\n" +
                "Adresse bigint REFERENCES " + TAB_ADRESSE + "(Addr_nr),\n" +
                "primary key (email));";


        public static final String CREATE_TAB_UNTERNEHMER = "create table   " + TAB_UNTERNEHMER + "  (\n" +
                "kunden_nr BIGSERIAL,\n" +
                "email varchar(300) REFERENCES " + TAB_USER + "(email) not null,\n" +
                "Firmenname varchar(60) not null,\n" +
                "ustID varchar(30) ,\n" +
                "IBAN char(22)     ,\n" +
                "unique(email),\n" +
                "primary key (kunden_nr));";


        public static final String CREATE_TAB_STUDENT = "create table " + TAB_STUDENT + " (\n" +
                "matrikelNr varchar(22) not null,\n" +
                "email varchar(300) REFERENCES " + TAB_USER + "(email) not null,\n" +
                "type varchar(30),\n" +
                "unique(email),\n" +
                "primary key (matrikelNr));";


        public static final String CREATE_TAB_STELLENANZEIGE = "create table " + TAB_STELLENANZEIGE + " (\n" +
                "id SERIAL ,\n" +
                "titel varchar(30) not null,\n" +
                "beschreibung varchar(300) ,\n" +
                "status varchar(10),\n" +
                "primary key (id));";


        public static final String CREATE_TAB_STUDENT_BEWIRBT_ST = "create table " + TAB_STUDENT_BEWIRBT_ST + " (\n" +
                "student varchar(22) REFERENCES  " + TAB_STUDENT + "(matrikelNr)  not null,\n" +
                "stellenanzeige bigint REFERENCES " + TAB_STELLENANZEIGE + "(id)  not null,\n" +
                "nr BIGSERIAL,\n" +
                "status varchar(10),\n" +
                "primary key (stellenanzeige, student));";


        public static final String CREATE_TAB_FERTIGKEIT = "create table " + TAB_FERTIGKEIT + " (\n" +
                "nr BIGSERIAL,\n" +
                "Softskill varchar(30) not null,\n" +
                "beschreibung varchar(100),\n" +
                "unique(softskill,beschreibung),\n" +
                "primary key (nr));";


        public static final String CREATE_TAB_ST_FORDERT_F = "create table " + TAB_ST_FORDERT_F + " (\n" +
                "fertigkeit bigint REFERENCES " + TAB_FERTIGKEIT + "(nr),\n" +
                "stellenanzeige bigint REFERENCES " + TAB_STELLENANZEIGE + "(id),\n" +
                "primary key (fertigkeit, stellenanzeige));";


        public static final String CREATE_TAB_ST_HAT_F = "create table " + TAB_ST_HAT_F + " (\n" +
                "student varchar(22) REFERENCES " + TAB_STUDENT + "(matrikelNr),\n" +
                "fertigkeit bigint REFERENCES " + TAB_FERTIGKEIT + "(nr),\n" +
                "primary key (fertigkeit,  student));";


        public static final String CREATE_TAB_U_ERSTELLT_ST = "create table " + TAB_U_ERSTELLT_ST + " (\n" +
                "unternehmer bigint REFERENCES " + TAB_UNTERNEHMER + "(kunden_nr),\n" +
                "stellenanzeige bigint REFERENCES " + TAB_STELLENANZEIGE + "(id),\n" +
                "erstelldatum timestamp default current_timestamp,\n" +
                "primary key (stellenanzeige, erstelldatum));";

        public static final String CREATE_TAB_USER_HAT_RESETCODE = "create table " + TAB_USER_HAT_RESETCODE + "  (\n" +
                "email varchar(300) REFERENCES " + TAB_USER + "(email) not null,\n" +
                "code character(6) not null,\n" +
                "erstellt timestamp default current_timestamp ,\n" +
                "unique(email),\n" +
                "primary key (email));";

        public static final String CREATE_TAB_TOGGLE_CONFIGURATION = "create table " + TAB_TOGGLE_CONFIGURATION + " (\n" +
                "feature varchar(100) not null, \n" +
                "status boolean not null, \n" +
                "caption varchar(300)," +
                "primary key (feature));";


    }


}
