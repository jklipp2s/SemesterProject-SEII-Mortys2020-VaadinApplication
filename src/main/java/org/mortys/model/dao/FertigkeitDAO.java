package org.mortys.model.dao;

import org.mortys.model.objects.dto.Fertigkeit;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.DBTables;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FertigkeitDAO extends AbstractDAO {
    private static FertigkeitDAO fertigkeitDAO;
    private String table = DBTables.TAB_FERTIGKEIT;

    public static FertigkeitDAO getInstance() {

        if (fertigkeitDAO == null) fertigkeitDAO = new FertigkeitDAO();

        return fertigkeitDAO;
    }

    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------

    public List<Fertigkeit> fetchFertigkeitAll() throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        List<Fertigkeit> fertigkeitList = new ArrayList<>();

        String sqlBefehl = "Select * FROM " + table  + ";";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;

        //String sqlBefehl = "Select * FROM " + table  + ";";


        try {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Fertigkeit fertigkeit = new Fertigkeit();
                fertigkeit.setSoftskill(resultSet.getString("softskill"));
                fertigkeit.setBeschreibung(resultSet.getString("Beschreibung"));

                fertigkeitList.add(fertigkeit);

                           }

        } catch (SQLException e) {
            Logger.getLogger(FertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }


        return fertigkeitList;
    }

    // FETCHMETHODEN (LADEMETHODEN) -END

    // PERSISTMETHODEN  -START -----------------------------------------------------------------------------

    public int persistFertigkeit(Fertigkeit fertigkeit) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        if (fertigkeitAlreadyExists(fertigkeit)) {
            return getFertigkeitId(fertigkeit);
        }



        String sqlBefehl = "INSERT INTO " + table + " VALUES(default,?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, fertigkeit.getSoftskill());
            statement.setString(2, fertigkeit.getBeschreibung());

            statement.executeUpdate();
            System.out.println("Fertigkeit neu angelegt!");
        } catch (SQLException e) {
            Logger.getLogger(FertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

        return getFertigkeitId(fertigkeit);
    }

    // PERSISTMETHODEN  -END

    // HILFSMETHODEN -START --------------------------------------------------------------------------------------------

    public int getFertigkeitId(Fertigkeit fertigkeit) {

        String sqlBefehl =  "SELECT nr  FROM " + table + " WHERE softskill = ? " +
                "AND beschreibung = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;
        int fertigkeitID = 0;
        /*String sqlBefehl =  "SELECT nr  FROM " + table + " WHERE softskill = '" + fertigkeit.getSoftskill() + "'" +
                            "AND beschreibung = '" + fertigkeit.getBeschreibung() + "';";

         */

        try {
            statement.setString(1,fertigkeit.getSoftskill());
            statement.setString(2,fertigkeit.getBeschreibung());
            resultSet = statement.executeQuery();

            if (resultSet.next()) fertigkeitID = resultSet.getInt("nr");

        } catch (SQLException e) {
            Logger.getLogger(FertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return fertigkeitID;
    }

    // HILFSMETHODEN -END

    // PRÜFMETHODEN -START ----------------------------------------------------------------------------------------------

    private boolean fertigkeitAlreadyExists(Fertigkeit fertigkeit) {
        String sqlBefehl =  "SELECT nr  FROM " + table + " WHERE softskill = ? " +
                "AND beschreibung = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;
        boolean exists = false;
        /*String sqlBefehl =  "SELECT nr  FROM " + table + " WHERE softskill = '" + fertigkeit.getSoftskill() + "'" +
                "AND beschreibung = '" + fertigkeit.getBeschreibung() + "';";

         */

        try {
            statement.setString(1,fertigkeit.getSoftskill());
            statement.setString(2,fertigkeit.getBeschreibung());
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(FertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return exists;
    }

    // PRÜFMETHODEN -END



}
