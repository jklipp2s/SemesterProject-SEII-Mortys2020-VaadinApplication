package org.mortys.services.util;

public class DBViews {
    public static final String ALL_STUDENTS = "dbs_view_all_students";
    public static final String ALL_UNTERNEHMER = "dbs_view_all_unternehmer";
    public static final String USER_HAT_ADDRESSE = "dbs_view_user_hat_addresse";
    public static final String STUDENT_HAT_FERTIGKEIT = "dbs_view_user_has_fertigkeit";
    public static final String STELLENANZEIGE_FORDERT_FERTIGKEIT = "dbs_view_stellenanzeige_fordert_fertigkeit";
    public static final String UNTERNEHMER_ERSTELLT_STELLENANZEIGE = "dbs_view_unternehmer_erstellt_stellenanzeige";
    public static final String STUDENT_BEWIRBT_STELLENANZEIGE = "dbs_view_student_bewirbt_stellenanzeige";
    public static final String FERTIGKEITEN_ZUGEORDNET = "dbs_view_fertigkeiten";


    public class Creation {

        public static final String CREATE_VIEW_ALL_STUDENTS = "CREATE VIEW " + DBViews.ALL_STUDENTS + " AS\n" +
                "SELECT s.matrikelnr, s.type, a.plz  ||' '||  a.ort As ort, u.anrede, u.geburtsdatum, s.email, u.username, u.nachname, \n" +
                "u.vorname, u.telefon, u.regdate, u.password, u.status, \n" +
                "u.adresse, u.avatar FROM " + DBTables.TAB_STUDENT + " s, " + DBTables.TAB_USER+ " u ,\n" +
                DBTables.TAB_ADRESSE +" a \n" +
                "where s.email = u.email and u.adresse = a.addr_nr; ";

        public static final String CREATE_VIEW_ALL_UNTERNEHMER = "CREATE VIEW " + DBViews.ALL_UNTERNEHMER + " AS\n" +
                "SELECT un.kunden_nr, un.firmenname, un.ustid, un.iban , u.anrede, u.geburtsdatum, u.email, u.username, u.nachname, \n" +
                "u.vorname, u.telefon, u.regdate, u.password, u.status, u.avatar, \n" +
                "u.adresse FROM " + DBTables.TAB_UNTERNEHMER + " un, " + DBTables.TAB_USER+ " u \n" +
                "where un.email = u.email; ";



        public static final String CREATE_VIEW_USER_HAS_ADDRESS = "create view " + USER_HAT_ADDRESSE + " as \n" +
                "select  email, username, addr_nr, plz, ort, street, h_nr \n" +
                "from " + DBTables.TAB_ADRESSE + " a,\n" +
                DBTables.TAB_USER + " u\n" +
                "where a.addr_nr = u.adresse;";

        private static final String zwischenView = "(Select * from " + DBTables.TAB_STUDENT + " s, \n" +
                DBTables.TAB_ST_HAT_F + " shf , \n" +
                DBTables.TAB_FERTIGKEIT + " f\n" +
                "where s.matrikelnr = shf.student\n" +
                "and f.nr = shf.fertigkeit) ";

        public static final String CREATE_VIEW_STUDENT_HAT_FERTIGKEIT = "create view " + STUDENT_HAT_FERTIGKEIT + " as\n" +
                "select s.matrikelnr, s.email, shf.fertigkeiten, shf.beschreibungen \n" +
                "from " + DBTables.TAB_STUDENT + " s,\n"  +
                "(Select s.matrikelnr AS student, string_agg(softskill, ', ') As fertigkeiten  ,  string_agg(beschreibung, ' ||| ')\n" +
                "As beschreibungen\n" +
                "From " + ALL_STUDENTS + " s ,( SELECT  Username, softskill, beschreibung \n" +
                "FROM " + ALL_STUDENTS + " s, " + zwischenView + " f where  \n" +
                "f.matrikelnr = s.matrikelnr) l where l.username = s.username \n" +
                "Group by s.matrikelnr) shf\n" +
                "where s.matrikelnr = shf.student;";


        public static final String CREATE_VIEW_STELLENANZEIGE_FORDERT_FERTIGKEIT = "create view " + STELLENANZEIGE_FORDERT_FERTIGKEIT + " as\n" +
                "select s.id AS stellenanzeige, s.titel, s.beschreibung AS st_beschreibung,\n" +
                " f.nr AS fertigkeit,  f.softskill, f.beschreibung AS f_beschreibung \n" +
                "from " + DBTables.TAB_STELLENANZEIGE + " s, \n" +
                DBTables.TAB_FERTIGKEIT + " f, \n" +
                DBTables.TAB_ST_FORDERT_F + " sff\n" +
                "where sff.fertigkeit = f.nr \n" +
                "and sff.stellenanzeige = s.id;";

        public static final String CREATE_VIEW_UNTERNEHMER_ERSTELLT_STELLENANZEIGE = "create view " + UNTERNEHMER_ERSTELLT_STELLENANZEIGE + " as     \n" +
                "select ues.erstelldatum, s.status, s.id,\n" +
                "s.titel,s.beschreibung, ues.unternehmer,\n" +
                "addr.plz || ' ' || addr.ort AS ort, u.firmenname, u.email  \n" +
                "from " + DBTables.TAB_U_ERSTELLT_ST + " ues,\n" +
                 DBTables.TAB_UNTERNEHMER + " u, " + DBTables.TAB_STELLENANZEIGE + " s,\n" +
                 DBViews.USER_HAT_ADDRESSE + " addr\n" +
                "where ues.stellenanzeige = s.id\n" +
                "and ues.unternehmer = u.kunden_nr\n" +
                "and u.email = addr.email;";



        public static final String CREATE_VIEW_STUDENT_BEWIRBT_STELLENANZEIGE = "create view " + STUDENT_BEWIRBT_STELLENANZEIGE + " as \n" +
                "select sbf.student, s.email, sbf.status As status_student, \n" +
                "u.firmenname, st.id, u.erstelldatum, st.titel, st.beschreibung, \n" +
                "st.status As status_stellenanzeige \n" +
                "from " + DBTables.TAB_STUDENT_BEWIRBT_ST + " sbf,\n" +
                 DBTables.TAB_STUDENT + " s, \n" +
                 DBTables.TAB_STELLENANZEIGE + " st,\n" +
                 UNTERNEHMER_ERSTELLT_STELLENANZEIGE + " u\n" +
                "where sbf.student = s.matrikelnr\n" +
                "and sbf.stellenanzeige = st.id\n" +
                "and u.id = sbf.stellenanzeige;";

    }



    public static final String CREATE_VIEW_FERTIGKEITEN_ZUGEORDNET = "create view as\n" +
            "Select s.matrikelnr AS username, string_agg(softskill, ', ')\n" +
            "As fertigkeiten   FROM dbs_view_all_students s ,( SELECT  Username, softskill \n" +
            "FROM dbs_view_all_students s, dbs_view_user_has_fertigkeit f where  \n" +
            "f.matrikelnr = s.matrikelnr) l where l.username = s.username \n" +
            "Group by s.matrikelnr;";


}
