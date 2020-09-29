package org.mortys.model.dao;

import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
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

public class StellenAnzeigeDAO extends AbstractDAO {
    private static StellenAnzeigeDAO stellenAnzeigeDAO;
    private String table = DBTables.TAB_STELLENANZEIGE;
    private String view = DBViews.UNTERNEHMER_ERSTELLT_STELLENANZEIGE;

    public static StellenAnzeigeDAO getInstance() {

        if (stellenAnzeigeDAO == null) stellenAnzeigeDAO = new StellenAnzeigeDAO();

        return stellenAnzeigeDAO;
    }


    public List<StellenAnzeige> utilFetcher(List<StellenAnzeige> stellenAnzeigeList, ResultSet resultSet) throws DatabaseException {
        try {
            while (resultSet.next()) {
                StellenAnzeige stellenAnzeige = new StellenAnzeige();
                stellenAnzeige.setId(resultSet.getInt("id"));
                stellenAnzeige.setTitel(resultSet.getString("titel"));
                stellenAnzeige.setBeschreibung(resultSet.getString("beschreibung"));
                stellenAnzeige.setStatus(resultSet.getString("status"));
                stellenAnzeige.setUnternehmen(resultSet.getString("firmenname"));
                stellenAnzeige.setOrt(resultSet.getString("ort"));
                stellenAnzeige.setErstellungsDatum(resultSet.getDate("erstelldatum").toLocalDate());
                stellenAnzeigeList.add(stellenAnzeige);

            }

        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) {
                Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
        return stellenAnzeigeList;
    }


    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------


    public List<StellenAnzeige> fetchAllStellenAnzeigen() throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        List<StellenAnzeige> stellenAnzeigeList = new ArrayList<>();

        String sqlBefehl = "Select * FROM " + view + ";";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery();
        } catch (SQLException throwables) {
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, throwables);
        }
        //String sqlBefehl = "Select * FROM " + view + ";";

        assert resultSet != null;
        return utilFetcher(stellenAnzeigeList, resultSet);
    }

    /*
    public List<StellenAnzeige> fetchStellenAnzeigenByUnternehmerId(int id) throws DatabaseException{

        JDBCConnection.getInstance().openConnection();

        List<StellenAnzeige> stellenAnzeigeList = new ArrayList<>();

        ResultSet resultSet = null;

        String sqlBefehl = "SELECT dbs_tab_stellenanzeige.id" +
                ", dbs_tab_stellenanzeige.titel" +
                ", dbs_tab_stellenanzeige.beschreibung" +
                " FROM dbs_tab_stellenanzeige" +
                " INNER JOIN dbs_tab_unternehmer_erstellt_stellenanzeige" +
                " ON dbs_tab_stellenanzeige.id=dbs_tab_unternehmer_erstellt_stellenanzeige.stellenanzeige" +
                " WHERE unternehmer = ?;";
*/
        /*
        String sqlBefehl = "SELECT dbs_tab_stellenanzeige.id, dbs_tab_stellenanzeige.titel, dbs_tab_stellenanzeige.beschreibung
         FROM dbs_tab_stellenanzeige
         INNER JOIN dbs_tab_unternehmer_erstellt_stellenanzeige
         ON dbs_tab_stellenanzeige.id=dbs_tab_unternehmer_erstellt_stellenanzeige.stellenanzeige
         WHERE unternehmer = '" + id + "'";
         */
/*
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try{
            statement.setInt(1,id);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                StellenAnzeige stellenAnzeige = new StellenAnzeige();
                stellenAnzeige.setId(resultSet.getInt("id"));
                stellenAnzeige.setTitel(resultSet.getString("titel"));
                stellenAnzeige.setBeschreibung(resultSet.getString("beschreibung"));
                stellenAnzeigeList.add(stellenAnzeige);
            }
        } catch (SQLException e){
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) {
                Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
        return stellenAnzeigeList;
    }
*/
    // FETCHMETHODEN (LADEMETHODEN) -END


    // PERSISTMETHODEN -START -----------------------------------------------------------------------------

    public void persistStellenAnzeige(StellenAnzeige stellenAnzeige) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "INSERT INTO " + table + " VALUES(default,?,?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, stellenAnzeige.getTitel());
            statement.setString(2, stellenAnzeige.getBeschreibung());
            statement.setString(3, stellenAnzeige.getStatus());

            statement.executeUpdate();
            stellenAnzeige.setId(getMaxStellenAnzeigeId());
            System.out.println("Stellenanzeige " + stellenAnzeige.getId() +  "neu angelegt!");
        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // PERSISTMETHODEN  -END

    // HILFSMETHODEN -START --------------------------------------------------------------------------------------------

    public int getMaxStellenAnzeigeId() {

        String sqlBefehl = "SELECT Max(id) AS id  FROM " + table + ";";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;
        int stellenAnzeigeID = 0;
        //String sqlBefehl = "SELECT Max(id) AS id  FROM " + table + ";";


        try {
            resultSet = statement.executeQuery();

            if (resultSet.next()) stellenAnzeigeID = resultSet.getInt("id");

        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return stellenAnzeigeID;
    }

    // HILFSMETHODEN -END

    // SETTERMETHODEN -START -----------------------------------------------------------------------------------------------

    public void changeStatus(StellenAnzeige stellenAnzeige, String status) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET status = '" + status + "' WHERE id = '"
                + stellenAnzeige.getId() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Status geändert.");
        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // SETTERMETHODEN -END


    // SEARCHMETHODEN -START -------------------------------------------------------------------------------------------

    public List<StellenAnzeige> fetchSpecificStellenAnzeigen(String firma, String location) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        List<StellenAnzeige> stellenAnzeigeList = new ArrayList<>();

        Statement statement = getStatement();
        ResultSet resultSet = null;

        String firmaSearch = "(firmenname ILIKE  '%" + firma + "%' or titel ILIKE  '%" + firma + "%' or beschreibung ILIKE  '%" + firma + "%')";
        String ortSearch =  "ort ILIKE  '%" + location + "%'";

        String sqlBefehl = firma.isEmpty() && location.isEmpty() ? "Select * FROM " + view + ";" :
                firma.isEmpty() && !location.isEmpty() ? "Select * FROM " + view + " where " +  ortSearch + ";" :
                        !firma.isEmpty() && location.isEmpty() ? "Select * FROM " + view + " where " + firmaSearch + ";" :
                                "Select * FROM " + view + " where " + ortSearch + " and " + firmaSearch + ";";

        try {
            resultSet = statement.executeQuery(sqlBefehl);
        } catch (SQLException throwables) {
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, throwables);
        }
        assert resultSet != null;
        return utilFetcher(stellenAnzeigeList, resultSet);
    }


    // SEARCHMETHODEN -END

    // LOESCHMETHODEN -START -----------------------------------------------------------------------------------------------

    public void deleteStellenAnzeigeById(int id) throws DatabaseException{

        JDBCConnection.getInstance().openConnection();

        UnternehmerErstelltStellenAnzeigeDAO.getInstance().deleteUnternehmerErstelltStellenAnzeige(id);

        ResultSet resultSet = null;

        String sqlBefehl = "DELETE FROM " + table + " WHERE id = '" + id + "'";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Stellenanzeige " + id + " aus der Datenbank enfernt.");
        } catch (SQLException e) {
            Logger.getLogger(StellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

        // LOESCHMETHODEN -END

    }

}
