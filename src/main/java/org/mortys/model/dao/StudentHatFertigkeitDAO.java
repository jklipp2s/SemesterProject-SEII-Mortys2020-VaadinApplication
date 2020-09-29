package org.mortys.model.dao;

import org.mortys.model.objects.dto.Fertigkeit;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.DBTables;
import org.mortys.services.util.DBViews;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StudentHatFertigkeitDAO extends AbstractDAO {
    private static StudentHatFertigkeitDAO studentHatFertigkeitDAO;
    private String table = DBTables.TAB_ST_HAT_F;
    private String view = DBViews.STUDENT_HAT_FERTIGKEIT;

    public static StudentHatFertigkeitDAO getInstance() {

        if (studentHatFertigkeitDAO == null) studentHatFertigkeitDAO = new StudentHatFertigkeitDAO();

        return studentHatFertigkeitDAO;
    }

    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------

    public List<Fertigkeit> fetchFertigkeitforStudent(String matrikelnummer) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        List<Fertigkeit> fertigkeitList = new ArrayList<>();

        String sqlBefehl = "SELECT * FROM " + table  + " shf, " + DBTables.TAB_FERTIGKEIT + " f, " + DBTables.TAB_STUDENT + " s "  + " WHERE matrikelnr = ? and shf.student = s.matrikelnr and shf.fertigkeit = f.nr;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;

        /*String sqlBefehl = "SELECT * FROM " + table  + " shf, " + DBTables.TAB_FERTIGKEIT + " f, " + DBTables.TAB_STUDENT + " s "  + " WHERE matrikelnr = '" +
                            matrikelnummer + "' and shf.student = s.matrikelnr and shf.fertigkeit = f.nr;";

         */

        try {
            statement.setString(1,matrikelnummer);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Fertigkeit fertigkeit = new Fertigkeit();
                fertigkeit.setSoftskill(resultSet.getString("softskill"));
                fertigkeit.setBeschreibung(resultSet.getString("Beschreibung"));

                fertigkeitList.add(fertigkeit);
            }

        } catch (SQLException e) {
            Logger.getLogger(StudentHatFertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }
        return fertigkeitList;
    }
    // FETCHMETHODEN (LADEMETHODEN) -END

    // PERSISTMETHODEN -START -----------------------------------------------------------------------------

    public void persistStudentHatFertigkeit(Student student, Fertigkeit fertigkeit) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();

        if (connectionAlreadyExists(student, fertigkeit)) {
            System.out.println("Fertigkeit wurde bereits zugewiesen!");
             return;
        }

        String sqlBefehl = "INSERT INTO " + table + " VALUES(?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, student.getMatrikelnr());
            int fertigkeitId = FertigkeitDAO.getInstance().persistFertigkeit(fertigkeit);
            statement.setInt(2, fertigkeitId);
            statement.executeUpdate();

            System.out.println("Fertigkeit neu angelegt!");
        } catch (SQLException e) {
            Logger.getLogger(StudentHatFertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }

    // PERSISTMETHODEN  -END




    // PRÜFMETHODEN -START ----------------------------------------------------------------------------------------------


    public boolean connectionAlreadyExists( Student student, Fertigkeit fertigkeit) {

        ResultSet resultSet = null;
        boolean exists = false;

        /*String preSelection = "(SELECT * FROM " + table  + " shf, " + DBTables.TAB_FERTIGKEIT + " f, " + DBTables.TAB_STUDENT + " s "  + " WHERE matrikelnr = '" +
                student.getMatrikelnr() + "' and shf.student = s.matrikelnr and shf.fertigkeit = f.nr) v";


        String sqlBefehl =  "SELECT fertigkeit  FROM " + preSelection + " WHERE matrikelnr = '" + student.getMatrikelnr() + "'" +
                "AND fertigkeit = '" + FertigkeitDAO.getInstance().getFertigkeitId(fertigkeit) + "';";

         */

        String preSelection = "(SELECT * FROM " + table  + " shf, " + DBTables.TAB_FERTIGKEIT + " f, " + DBTables.TAB_STUDENT + " s "  + " WHERE matrikelnr = ? and shf.student = s.matrikelnr and shf.fertigkeit = f.nr) v";


        String sqlBefehl =  "SELECT fertigkeit  FROM " + preSelection + " WHERE matrikelnr = ?" +
                "AND fertigkeit = ?;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1,student.getMatrikelnr());
            statement.setString(2,student.getMatrikelnr());
            statement.setInt(3,FertigkeitDAO.getInstance().getFertigkeitId(fertigkeit));
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(StudentHatFertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return exists;
    }

    // PRÜFMETHODEN -END



}
