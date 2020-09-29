package org.mortys.model.dao;

import org.mortys.model.objects.dto.Address;
import org.mortys.model.objects.dto.Unternehmer;
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


public class UnternehmerDAO extends UserDAO {
    private static UnternehmerDAO unternehmerDAO;
    private String table = DBTables.TAB_UNTERNEHMER;
    private String view = DBViews.ALL_UNTERNEHMER;

    public static UnternehmerDAO getInstance() {

        if (unternehmerDAO == null) unternehmerDAO = new UnternehmerDAO();

        return unternehmerDAO;
    }

    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------

    public List<Unternehmer> fetchAllUsers() throws DatabaseException {
        ResultSet unternehmerData = null;
        List<Unternehmer> unternehmerList = new ArrayList<>();

        //String sqlBefehl = "SELECT * FROM " + view + ";";
        String sqlBefehl = "SELECT * FROM " + view + ";";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {

            unternehmerData = statement.executeQuery();

            while (unternehmerData.next()) {

                Unternehmer unternehmer = new Unternehmer();
                unternehmer.setAnrede(unternehmerData.getString("anrede"));
                unternehmer.setGeburtsdatum(unternehmerData.getDate("geburtsdatum").toLocalDate());
                unternehmer.setUsername(unternehmerData.getString("username"));
                unternehmer.setNachname(unternehmerData.getString("nachname"));
                unternehmer.setVorname(unternehmerData.getString("vorname"));
                unternehmer.setEmail(unternehmerData.getString("email"));
                unternehmer.setTelefon(unternehmerData.getString("telefon"));
                unternehmer.setRegistrationDate(unternehmerData.getDate("regdate").toLocalDate());
                unternehmer.setAddress(AddressDAO.getInstance().fetchAddress(unternehmerData.getInt("adresse")));
                unternehmer.setKundennummer(unternehmerData.getInt("kunden_nr"));
                unternehmer.setFirmenname(unternehmerData.getString("firmenname"));
                unternehmer.setUstId(unternehmerData.getString("ustid"));
                unternehmer.setIban(unternehmerData.getString("iban"));
                unternehmer.setAvatar(unternehmerData.getString("avatar"));

                unternehmerList.add(unternehmer);
            }

        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (unternehmerData != null) unternehmerData.close(); } catch (Exception exc) { }
        }
        return unternehmerList;
    }


    public Unternehmer fetchUser(String email) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet unternehmerdata = null;
        Unternehmer unternehmer = new Unternehmer();

        //String sqlBefehl = "SELECT * FROM " + view + " WHERE email = '" + email + "';";
        String sqlBefehl = "SELECT * FROM " + view + " WHERE email = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1,email);

            unternehmerdata = statement.executeQuery();

            if(unternehmerdata.next()) {
                unternehmer.setAnrede(unternehmerdata.getString("anrede"));
                if(unternehmerdata.getDate("geburtsdatum") != null) unternehmer.setGeburtsdatum(unternehmerdata.getDate("geburtsdatum").toLocalDate());
                unternehmer.setUsername(unternehmerdata.getString("username"));
                unternehmer.setNachname(unternehmerdata.getString("nachname"));
                unternehmer.setVorname(unternehmerdata.getString("vorname"));
                unternehmer.setEmail(unternehmerdata.getString("email"));
                unternehmer.setTelefon(unternehmerdata.getString("telefon"));
                unternehmer.setRegistrationDate(unternehmerdata.getDate("regdate").toLocalDate());
                unternehmer.setAddress(AddressDAO.getInstance().fetchAddress(unternehmerdata.getInt("adresse")));
                unternehmer.setKundennummer(unternehmerdata.getInt("kunden_nr"));
                unternehmer.setFirmenname(unternehmerdata.getString("firmenname"));
                unternehmer.setUstId(unternehmerdata.getString("ustid"));
                unternehmer.setIban(unternehmerdata.getString("iban"));
                unternehmer.setAvatar(unternehmerdata.getString("avatar"));
            }

        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (unternehmerdata != null) unternehmerdata.close(); } catch (Exception exc) { }
        }
        return unternehmer;
    }



    // FETCHMETHODEN (LADEMETHODEN) -END

    // PERSISTMETHODEN  -START -----------------------------------------------------------------------------


    public void registerUnternehmer(Unternehmer unternehmer, String password) throws DatabaseException {

        registerUser(unternehmer, password);

        String sqlBefehl = "INSERT INTO " + table + " VALUES(default,?,?,null,null)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, unternehmer.getEmail());
            statement.setString(2, unternehmer.getFirmenname());

            statement.executeUpdate();
            unternehmer.setKundennummer(getKundenNummer(unternehmer.getEmail()));
            System.out.println("Unternehmer " + unternehmer.getUsername() + " erfolgreich registriert!");
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }
    public void persistUser(Unternehmer unternehmer, Address address, String password) throws DatabaseException {

        UserDAO.getInstance().prepersistUser(unternehmer, address, password);

        String sqlBefehl = "INSERT INTO " + table + " VALUES(default,?,?,?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, unternehmer.getEmail());
            statement.setString(2, unternehmer.getFirmenname());
            statement.setString(3, unternehmer.getUstId());
            statement.setString(4, unternehmer.getIban());

            statement.executeUpdate();
            unternehmer.setKundennummer(getKundenNummer(unternehmer.getEmail()));
            System.out.println("Unternehmer " + unternehmer.getUsername() + " erfolgreich registriert!");
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // PERSISTMETHODEN  -END


    // HILFSMETHODEN -START --------------------------------------------------------------------------------------------

    private int getKundenNummer(String email) {
        int kundenNummer = 0;
        ResultSet resultSet = null;

        //String sqlBefehl = "SELECT kunden_nr FROM " + table + " WHERE email = '" + email + "';";
        String sqlBefehl = "SELECT kunden_nr FROM " + table + " WHERE email = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1,email);
            resultSet = statement.executeQuery();

           if(resultSet.next()) kundenNummer = resultSet.getInt("kunden_nr");

        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }
        return kundenNummer;
    }


    // HILFSMETHODEN -END

    // PRÜFMETHODEN -START ---------------------------------------------------------------------------------------------
    public boolean stIdAlreadyExists(String ustId) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet resultSet = null;
        boolean exists = false;
        //String sqlBefehl = "SELECT ustid  FROM " + table + " WHERE ustid = '" + ustId + "';";
        String sqlBefehl = "SELECT ustid  FROM " + table + " WHERE ustid = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1,ustId);
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return exists;
    }


    // PRÜFMETHODEN -END



    // SETTERMETHODEN -START -----------------------------------------------------------------------------------------------
    // Setter Method's bitte für die ProfilPage verwenden
    // Bitte auch immer checken ob die Bedingungen -> TABLECONSTRAINTS, USE-CASE-ANFORDERUNGEN  <- erfüllt sind

    public void setFirmenname(Unternehmer unternehmer, String firmenname) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET firmenname = '" + firmenname +"' WHERE email = '"
                + unternehmer.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Firmenname geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }


    public void setUstid(Unternehmer unternehmer, String ustid) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET ustid = '" + ustid +"' WHERE email = '"
                + unternehmer.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("UmsatzSteuerID geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }


    public void setIban(Unternehmer unternehmer, String iban) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET iban = '" + iban + "' WHERE email = '"
                + unternehmer.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Iban geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UnternehmerDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // SETTERMETHODEN -END


}
