package org.mortys.model.dao;

import org.mortys.model.objects.dto.Fertigkeit;
import org.mortys.model.objects.dto.StellenAnzeige;
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

public class StellenAnzeigeFordertFertigkeitDAO extends AbstractDAO {
    private static StellenAnzeigeFordertFertigkeitDAO stellenAnzeigeFordertFertigkeitDAO;
    private String table = DBTables.TAB_ST_FORDERT_F;
    private String view = DBViews.STELLENANZEIGE_FORDERT_FERTIGKEIT;

    public static StellenAnzeigeFordertFertigkeitDAO getInstance() {

        if (stellenAnzeigeFordertFertigkeitDAO == null) {
            stellenAnzeigeFordertFertigkeitDAO = new StellenAnzeigeFordertFertigkeitDAO();
        }

        return stellenAnzeigeFordertFertigkeitDAO;
    }

    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------


    public List<Fertigkeit> fetchFertigkeitforStellenAnzeige(int id) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        List<Fertigkeit> fertigkeitList = new ArrayList<>();

        ResultSet resultSet = null;

        /*
        String sqlBefehl = "SELECT * FROM " + view + " WHERE stellenanzeige = '" +
                id + "';";
         */
        String sqlBefehl = "SELECT * FROM " + view + " WHERE stellenanzeige = ?;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setInt(1,id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Fertigkeit fertigkeit = new Fertigkeit();
                fertigkeit.setSoftskill(resultSet.getString("softskill"));
                fertigkeit.setBeschreibung(resultSet.getString("f_beschreibung"));

                fertigkeitList.add(fertigkeit);

            }

        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeFordertFertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }
        return fertigkeitList;
    }

    // FETCHMETHODEN (LADEMETHODEN) -END

    // PERSISTMETHODEN  -START -----------------------------------------------------------------------------------------


    public void persistStellenAnzeigeFordertFertigkeit(StellenAnzeige stellenAnzeige, Fertigkeit fertigkeit) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();


        if (stellenAnzeige.getId() == 0) {
            System.out.println("Stellenanzeige wurde noch nicht erstellt");
            return;
        }

        if (connectionAlreadyExists(stellenAnzeige, fertigkeit)) {
            System.out.println("Fertigkeit wurde bereits zugewiesen!");
            return;
        }

        String sqlBefehl = "INSERT INTO " + table + " VALUES(?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {

            statement.setInt(1, stellenAnzeige.getId());
            int fertigkeitId = FertigkeitDAO.getInstance().persistFertigkeit(fertigkeit);
            statement.setInt(2, fertigkeitId);

            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeFordertFertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }

    // PERSISTMETHODEN  -END


    // PRÜFMETHODEN -START ---------------------------------------------------------------------------------------------

    private boolean connectionAlreadyExists(StellenAnzeige stellenAnzeige, Fertigkeit fertigkeit) {

        ResultSet resultSet = null;
        boolean exists = false;

        /*
        String sqlBefehl = "SELECT *  FROM " + table + " WHERE stellenanzeige = '" +
                stellenAnzeige.getId()  + "'" +
                "AND fertigkeit = '" + FertigkeitDAO.getInstance().getFertigkeitId(fertigkeit) + "';";
         */
        String sqlBefehl = "SELECT *  FROM " + table + " WHERE stellenanzeige = ? AND fertigkeit = ?;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setInt(1,stellenAnzeige.getId());
            statement.setInt(2,FertigkeitDAO.getInstance().getFertigkeitId(fertigkeit));
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeFordertFertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return exists;
    }


    // PRÜFMETHODEN -END


}
