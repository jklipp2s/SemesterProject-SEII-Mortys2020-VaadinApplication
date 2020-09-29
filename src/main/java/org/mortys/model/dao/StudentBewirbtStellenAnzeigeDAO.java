package org.mortys.model.dao;

import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.DAOfetcherForStudUntBewerbtErstelltStellenanzeige;
import org.mortys.services.util.DBTables;
import org.mortys.services.util.DBViews;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StudentBewirbtStellenAnzeigeDAO extends AbstractDAO {
    private static StudentBewirbtStellenAnzeigeDAO studentBewirbtStellenAnzeigeDAO;
    private String table = DBTables.TAB_STUDENT_BEWIRBT_ST;
    private String view = DBViews.STUDENT_BEWIRBT_STELLENANZEIGE;


    public static StudentBewirbtStellenAnzeigeDAO getInstance() {

        if (studentBewirbtStellenAnzeigeDAO == null) {
            studentBewirbtStellenAnzeigeDAO = new StudentBewirbtStellenAnzeigeDAO();
        }
        return studentBewirbtStellenAnzeigeDAO;
    }


    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------

    public List<StellenAnzeige> fetchStellenAnzeigeStudent(Student student) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        List<StellenAnzeige> stellenAnzeigeList = new ArrayList<>();
        ResultSet resultSet = null;

        /*String sqlBefehl = "SELECT * FROM " + view + " WHERE student = '" +
                student.getMatrikelnr() + "';";
         */
        String sqlBefehl = "SELECT * FROM " + view + " WHERE student = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1,student.getMatrikelnr());
        } catch (SQLException throwables) {
            Logger.getLogger(StudentBewirbtStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, throwables);
        }
        return DAOfetcherForStudUntBewerbtErstelltStellenanzeige.studUntDaoFetcher(statement, stellenAnzeigeList, "student");

    }

    // FETCHMETHODEN (LADEMETHODEN) -END

    // PERSISTMETHODEN -START -----------------------------------------------------------------------------

    public void persistStudentBewirbtStellenanzeige(Student student, StellenAnzeige stellenAnzeige) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        if (stellenAnzeige.getId() == 0) {
            System.out.println("Diese StellenAnzeige gibt es nicht!");
            return;
        }

        if(connectionAlreadyExists(student, stellenAnzeige)) {
            System.out.println("Der Student hat sich auf diese Stellenanzeige schon beworben");
            return;
        }

        String sqlBefehl = "INSERT INTO " + table + " VALUES(?,?,default)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, student.getMatrikelnr());
            statement.setInt(2, stellenAnzeige.getId());
            statement.executeUpdate();

            System.out.println(student.getUsername() + " hat sich erfolgreich beworben!");
        } catch (SQLException e) {
            Logger.getLogger(StudentBewirbtStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // PERSISTMETHODEN  -END

    // PRÜFMETHODEN -START ----------------------------------------------------------------------------------------------

    private boolean connectionAlreadyExists(Student student, StellenAnzeige stellenAnzeige) {

        String sqlBefehl = "SELECT *  FROM " + table + " WHERE stellenanzeige = ? AND student = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;
        boolean exists = false;

        /*String sqlBefehl = "SELECT *  FROM " + table + " WHERE stellenanzeige = '" +
                stellenAnzeige.getId()  + "'" +
                "AND student = '" + student.getMatrikelnr() + "';";

         */

        try {
            statement.setInt(1,stellenAnzeige.getId());
            statement.setString(2,student.getMatrikelnr());
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(StudentBewirbtStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return exists;
    }

    // PRÜFMETHODEN -END
    // SETTERMETHODEN -START -----------------------------------------------------------------------------------------------

    public void changeStatus(Student student, StellenAnzeige stellenAnzeige, String status) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET status = '" + status + "' WHERE student = '" + student.getMatrikelnr() +
                "' AND stellenanzeige = '"
                + stellenAnzeige.getId() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Status geändert.");
        } catch (SQLException e) {
            Logger.getLogger(StudentBewirbtStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }


    // SETTERMETHODEN -END







}
