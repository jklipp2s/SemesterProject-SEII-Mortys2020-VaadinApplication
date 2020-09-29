package org.mortys.services.util;

import org.mortys.model.dao.StudentBewirbtStellenAnzeigeDAO;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOfetcherForStudUntBewerbtErstelltStellenanzeige {
    public static List<StellenAnzeige> studUntDaoFetcher(PreparedStatement statement, List<StellenAnzeige> stellenAnzeigeList, String user) throws DatabaseException {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                StellenAnzeige stellenAnzeige = new StellenAnzeige();
                stellenAnzeige.setId(resultSet.getInt("id"));
                stellenAnzeige.setTitel(resultSet.getString("titel"));
                stellenAnzeige.setBeschreibung(resultSet.getString("beschreibung"));
                if(user.equals("student")){
                    stellenAnzeige.setStatus(resultSet.getString("status_stellenanzeige"));
                } else {
                    stellenAnzeige.setStatus(resultSet.getString("status"));
                }
                stellenAnzeige.setErstellungsDatum(resultSet.getDate("erstelldatum").toLocalDate());
                stellenAnzeige.setUnternehmen(resultSet.getString("firmenname"));
                stellenAnzeigeList.add(stellenAnzeige);
            }

        } catch (SQLException e) {
            Logger.getLogger(StudentBewirbtStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) {
                Logger.getLogger(StudentBewirbtStellenAnzeigeDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
        return stellenAnzeigeList;
    }
}
