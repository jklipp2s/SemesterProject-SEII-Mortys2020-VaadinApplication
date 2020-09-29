package org.mortys.model.dao;

import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.BeschäftigungsArt;
import org.mortys.services.util.DBTables;
import org.mortys.services.util.DBViews;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StudentDAO extends UserDAO {
    private static StudentDAO studentDAO;
    private String table = DBTables.TAB_STUDENT;
    private String view = DBViews.ALL_STUDENTS;

    public static StudentDAO getInstance() {

        if (studentDAO == null) studentDAO = new StudentDAO();

        return studentDAO;
    }

    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------

    public List<Student> fetchAllUsers() throws DatabaseException {
        ResultSet studentdata = null;
        String sqlBefehl = "SELECT * FROM " + view + ";";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        List<Student> userList = new ArrayList<>();


        //String sqlBefehl = "SELECT * FROM " + view + ";";

        try {

            studentdata = statement.executeQuery();

            while (studentdata.next()) {
                userList.add(fetchStudent(studentdata));
            }

        } catch (SQLException e) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (studentdata != null) studentdata.close(); } catch (Exception exc) { }
        }

        return userList;
    }


    public Student fetchUser(String email) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet studentdata = null;
        String sqlBefehl = "SELECT * FROM " + view + " WHERE email = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);


        //String sqlBefehl = "SELECT * FROM " + view + " WHERE email = '" + email + "';";
        Student student = new Student();

        try {
            statement.setString(1,email);
            studentdata = statement.executeQuery();
            if(studentdata.next()) {
                student = fetchStudent(studentdata);
            } else {
                /*sqlBefehl = "SELECT s.matrikelnr, s.type, u.anrede, u.geburtsdatum, s.email, u.username, u.nachname,\n" +
                        "                u.vorname, u.telefon, u.regdate, u.password, u.status,\n" +
                        "                u.adresse, u.avatar FROM " + table +" s, " + DBTables.TAB_USER +" u\n" +
                        "where s.email = u.email and s.email = '"+ email.trim().toLowerCase() +"';";

                 */
                sqlBefehl = "SELECT s.matrikelnr, s.type, u.anrede, u.geburtsdatum, s.email, u.username, u.nachname,\n" +
                        "                u.vorname, u.telefon, u.regdate, u.password, u.status,\n" +
                        "                u.adresse, u.avatar FROM " + table +" s, " + DBTables.TAB_USER +" u\n" +
                        "where s.email = u.email and s.email = ?;";
                statement = getPreparedStatement(sqlBefehl);
                statement.setString(1,email.trim().toLowerCase());
                studentdata = statement.executeQuery();
                if(studentdata.next()){
                    student = fetchStudent(studentdata);
                }
            }

        } catch (SQLException e) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (studentdata != null) studentdata.close(); } catch (Exception exc) { }
        }

        return student;
    }

    // FETCHMETHODEN (LADEMETHODEN) -END

    // PERSISTMETHODEN -START -----------------------------------------------------------------------------



    public void registerStudent(Student student, String password) throws DatabaseException {
        registerUser(student,password);

        String sqlBefehl = "INSERT INTO " + table + " VALUES(?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, student.getMatrikelnr());
            statement.setString(2, student.getEmail());
            statement.executeUpdate();
            System.out.println("Student " + student.getUsername() + " erfolgreich registriert!");
        } catch (SQLException e) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // PERSISTMETHODEN  -END

    // HILFSMETHODEN -START --------------------------------------------------------------------------------------------

    private Student fetchStudent(ResultSet studentdata) throws SQLException, DatabaseException {
        Student student = new Student();
        student.setAnrede(studentdata.getString("anrede"));
        if(studentdata.getDate("geburtsdatum") != null) student.setGeburtsdatum(studentdata.getDate("geburtsdatum").toLocalDate());
        student.setUsername(studentdata.getString("username"));
        student.setNachname(studentdata.getString("nachname"));
        student.setVorname(studentdata.getString("vorname"));
        student.setEmail(studentdata.getString("email"));
        student.setTelefon(studentdata.getString("telefon"));
        student.setRegistrationDate(studentdata.getDate("regdate").toLocalDate());
        student.setAddress(AddressDAO.getInstance().fetchAddress(studentdata.getInt("adresse")));
        student.setBeschäftigung(studentdata.getString("type"));
        student.setMatrikelnr(studentdata.getString("matrikelnr"));
        student.setAvatar(studentdata.getString("avatar"));

        return student;
    }

    // HILFSMETHODEN -END

    // PRÜFMETHODEN -START ---------------------------------------------------------------------------------------------


    public boolean matrNrAlreadyExists(String matrikelnummer) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "SELECT matrikelnr  FROM " + table + " WHERE matrikelnr = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;
        boolean exists = false;
        //String sqlBefehl = "SELECT matrikelnr  FROM " + table + " WHERE matrikelnr = '" + matrikelnummer + "';";

        try {
            statement.setString(1,matrikelnummer);
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return exists;
    }

    // PRÜFMETHODEN -END

    // SETTERMETHODEN -START ---------------------------------------------------------------------------------------------


    public void setBeschäftigung(Student student, String beschäftigungsart) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET type = '" + beschäftigungsart +"' WHERE email = '"
                + student.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Beschäftigungsart geändert.");
        } catch (SQLException e) {
            Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }



    // SEARCHMETHODEN -START -------------------------------------------------------------------------------------------

    public List<Student> fetchSpecificStudents(String student, String location, String beschaeftigung, String faehigkeiten) throws DatabaseException {
        ResultSet studentdata = null;
        Statement statement = getStatement();
        List<Student> userList = new ArrayList<>();


        String sqlBefehl ="";

        if (!faehigkeiten.isEmpty()) sqlBefehl += "";

            String studentSearch = "(username ILIKE '%" + student + "%' or nachname ILIKE '%" + student + "%'\n" +
                    "or vorname ILIKE '%" + student + "%' or s.matrikelnr ILIKE '%" + student + "%'  or s.email ILIKE '%" + student + "%')";
            String locationSearch = "ort ILIKE '%" + location + "%'";
            String beschaeftigungSearch = "type ILIKE '" + beschaeftigung + "'";
            String faehigkeitenSearch = "" + separatedStatement(faehigkeiten, "fertigkeiten") + " or " + separatedStatement(faehigkeiten,"beschreibungen") + ") l";


            sqlBefehl = student.isEmpty() && location.isEmpty() && beschaeftigung.isEmpty() && faehigkeiten.isEmpty() ?
                    "SELECT * FROM " + view + ";" : !faehigkeiten.isEmpty() ?
                    "SELECT * FROM (SELECT * FROM ( SELECT DISTINCT ON (username) * FROM " + view + " s, " + DBViews.STUDENT_HAT_FERTIGKEIT + " f " + "where  " +
                            "f.matrikelnr = s.matrikelnr " :
                    " SELECT * FROM " + view + " s where ";

            if (!student.isEmpty()) sqlBefehl += faehigkeiten.isEmpty() ? studentSearch : " and " + studentSearch;
            if (!location.isEmpty())
                sqlBefehl += faehigkeiten.isEmpty() && student.isEmpty() ? locationSearch : " and " + locationSearch;
            if (!beschaeftigung.isEmpty() && !beschaeftigung.equals(BeschäftigungsArt.ALL))
                sqlBefehl += faehigkeiten.isEmpty() && student.isEmpty() && location.isEmpty() ? beschaeftigungSearch : " and " + beschaeftigungSearch;
            if (!faehigkeiten.isEmpty()) sqlBefehl += ") i where " + faehigkeitenSearch;
            sqlBefehl += ";";
            try {

                studentdata = statement.executeQuery(sqlBefehl);

                while (studentdata.next()) {
                    userList.add(fetchStudent(studentdata));
                }

            } catch (SQLException e) {
                Logger.getLogger(StudentDAO.class.getName()).log(Level.SEVERE, null, e);
                System.out.println(sqlBefehl);

            } finally {
                JDBCConnection.getInstance().closeConnection();
                try { if (studentdata != null) studentdata.close(); } catch (Exception exc) { }
                System.out.println(sqlBefehl);
            }

            return userList;
        }



    private String separatedStatement(String faehigkeiten, String column) {

        String[] output = faehigkeiten.split(",");


        String faehigkeitenSeparated = "(";

            for (int i = 0; i < output.length; i++) {
                faehigkeitenSeparated +=  column + " ILIKE ( '%" + output[i].trim() + "%')";
                if(i < output.length-1) {
                    faehigkeitenSeparated += " and ";
                }
            }

            faehigkeitenSeparated += ")";


            return faehigkeitenSeparated;
        }


    }









