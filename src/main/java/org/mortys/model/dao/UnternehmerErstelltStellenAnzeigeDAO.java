package org.mortys.model.dao;

import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.DAOfetcherForStudUntBewerbtErstelltStellenanzeige;
import org.mortys.services.util.DBTables;
import org.mortys.services.util.DBViews;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnternehmerErstelltStellenAnzeigeDAO extends AbstractDAO {
    private static UnternehmerErstelltStellenAnzeigeDAO unternehmerErstelltStellenAnzeigeDAO;
    private String table = DBTables.TAB_U_ERSTELLT_ST;
    private String view = DBViews.UNTERNEHMER_ERSTELLT_STELLENANZEIGE;

    public static UnternehmerErstelltStellenAnzeigeDAO getInstance() {

        if (unternehmerErstelltStellenAnzeigeDAO == null) {
            unternehmerErstelltStellenAnzeigeDAO = new UnternehmerErstelltStellenAnzeigeDAO();
        }
        return unternehmerErstelltStellenAnzeigeDAO;
    }


    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------

    public List<StellenAnzeige> fetchStellenanzeigeForUnternehmer(Unternehmer unternehmer) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();

        List<StellenAnzeige> stellenAnzeigeList = new ArrayList<>();

        ResultSet resultSet = null;

        //String sqlBefehl = "SELECT * FROM " + view + " WHERE unternehmer = '" +  unternehmer.getKundennummer() + "';";
        String sqlBefehl = "SELECT * FROM " + view + " WHERE unternehmer = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setInt(1,unternehmer.getKundennummer());
        } catch (SQLException throwables) {
            Logger.getLogger(StudentBewirbtStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, throwables);
        }
        return DAOfetcherForStudUntBewerbtErstelltStellenanzeige.studUntDaoFetcher(statement, stellenAnzeigeList, "");
    }

    // FETCHMETHODEN (LADEMETHODEN) -END


    // PERSISTMETHODEN -START -----------------------------------------------------------------------------

    public void persistUnternehmerErstelltStellenanzeige(Unternehmer unternehmer, StellenAnzeige stellenAnzeige) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();


        if (stellenAnzeige.getId() == 0)
            StellenAnzeigeDAO.getInstance().persistStellenAnzeige(stellenAnzeige);

        String sqlBefehl = "INSERT INTO " + table + " VALUES(?,?,default)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setInt(1, unternehmer.getKundennummer());
            statement.setInt(2, stellenAnzeige.getId());
            statement.executeUpdate();
            stellenAnzeige.setErstellungsDatum(getErstellungsDate(stellenAnzeige));
            stellenAnzeige.setUnternehmen(getFirma(stellenAnzeige));
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerErstelltStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }
    // PERSISTMETHODEN  -END

    // HILFSMETHODEN -START --------------------------------------------------------------------------------------------

    protected LocalDate getErstellungsDate(StellenAnzeige stellenAnzeige) {
        ResultSet resultSet = null;
        Date date = null;

        /* String sqlBefehl = "SELECT *  FROM " + table + " WHERE stellenanzeige = '" +
                stellenAnzeige.getId()  + "';";

         */
        String sqlBefehl = "SELECT *  FROM " + table + " WHERE stellenanzeige = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setInt(1,stellenAnzeige.getId());
            resultSet = statement.executeQuery();
            if(resultSet.next()) date = resultSet.getDate("erstelldatum");
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerErstelltStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }
        return date.toLocalDate();
    }


    protected String getFirma(StellenAnzeige stellenAnzeige) {
        ResultSet resultSet = null;
        String unternehmen = null;

        /* String sqlBefehl = "SELECT firmenname  FROM " + view + " WHERE id = '" +
                stellenAnzeige.getId()  + "';";

         */
        String sqlBefehl = "SELECT firmenname  FROM " + view + " WHERE id = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.setInt(1,stellenAnzeige.getId());
            resultSet = statement.executeQuery();
            if(resultSet.next()) unternehmen = resultSet.getString("firmenname");
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerErstelltStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return unternehmen;
    }

    // HILFSMETHODEN -END

    // LOESCHMETHODEN -START -----------------------------------------------------------------------------------------------

    public void deleteUnternehmerErstelltStellenAnzeige(int id) throws DatabaseException{
        JDBCConnection.getInstance().openConnection();

        ResultSet resultSet = null;

        String sqlBefehl = "DELETE FROM " + table + " WHERE stellenanzeige = '" + id + "'";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerErstelltStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }

    // LOESCHMETHODEN -END
}
