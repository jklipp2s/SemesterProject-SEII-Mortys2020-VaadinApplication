package org.mortys.model.dao;

import org.mortys.model.objects.dto.Address;
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


public class AddressDAO extends AbstractDAO {
    private static AddressDAO addressDAO;
    private String view = DBViews.USER_HAT_ADDRESSE;
    private String table = DBTables.TAB_ADRESSE;

    public static AddressDAO getInstance() {

        if (addressDAO == null) addressDAO = new AddressDAO();

        return addressDAO;
    }

    // FETCHMETHODEN (LADEMETHODEN) -START -----------------------------------------------------------------------------

    public List<Address> fetchAllAddress() throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        List<Address> addressList = new ArrayList<>();
        String sqlBefehl = "Select * FROM " + table + ";";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Address address = new Address();
                address.setPlz(resultSet.getString("plz"));
                address.setOrt(resultSet.getString("ort"));
                address.setStreet(resultSet.getString("street"));
                address.setStreet(resultSet.getString("h_nr"));


                addressList.add(address);
            }

        } catch (SQLException e) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return addressList;
    }



    public Address fetchAddress(int id) throws DatabaseException {
        Address address = new Address();

        String sqlBefehl = "Select * FROM " + view + " where addr_nr = ?;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;

        //String sqlBefehl = "Select * FROM " + view + " where addr_nr = '" + id + "';";

        try {
            statement.setInt(1,id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                address.setPlz(resultSet.getString("plz"));
                address.setOrt(resultSet.getString("ort"));
                address.setStreet(resultSet.getString("street"));
                address.setHausnummer(resultSet.getInt("h_nr"));

            }

        } catch (SQLException e) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return address;
    }
    // FETCHMETHODEN (LADEMETHODEN) -END

    // PERSISTMETHODEN  -START -----------------------------------------------------------------------------------------


    public int persistAddress(Address address) {

        if (addressAlreadyExists(address)) {
            return getAddressId(address);
        }

        String sqlBefehl = "INSERT INTO " + table + " VALUES(default,?,?,?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, address.getPlz());
            statement.setString(2, address.getOrt());
            statement.setString(3, address.getStreet());
            statement.setInt(4, address.getHausnummer());


            statement.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        System.out.println("Adresse erfolgreich gespeichert!");
        return getAddressId(address);
    }

    // PERSISTMETHODEN  -END

    // HILFSMETHODEN -START --------------------------------------------------------------------------------------------

    private int getAddressId(Address address) {

        String sqlBefehl = "SELECT addr_nr  FROM " + table + " WHERE plz = ?" +
                "AND ort = ? " +
                "AND street = ? " +
                "AND h_nr = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;
        int addressID = 0;
        /*String sqlBefehl = "SELECT addr_nr  FROM " + table + " WHERE plz = '" + address.getPlz() + "'" +
                "AND ort = '" + address.getOrt() + "'\n" +
                "AND street = '" + address.getStreet() + "'\n" +
                "AND h_nr = '" + address.getHausnummer() + "';";

         */

        try {
            statement.setString(1,address.getPlz());
            statement.setString(2,address.getOrt());
            statement.setString(3,address.getStreet());
            statement.setInt(4,address.getHausnummer());

            resultSet = statement.executeQuery();

            if (resultSet.next()) addressID = resultSet.getInt("addr_nr");

        } catch (SQLException e) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return addressID;
    }

    // HILFSMETHODEN -END

    // PRÜFMETHODEN -START ---------------------------------------------------------------------------------------------

    private boolean addressAlreadyExists(Address address) {
        String sqlBefehl = "SELECT addr_nr  FROM " + table + " WHERE plz = ? " +
                "AND ort = ? " +
                "AND street = ? " +
                "AND h_nr = ?;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        ResultSet resultSet = null;
        boolean exists = false;
        /*String sqlBefehl = "SELECT addr_nr  FROM " + table + " WHERE plz = '" + address.getPlz() + "'" +
                "AND ort = '" + address.getOrt() + "'\n" +
                "AND street = '" + address.getStreet() + "'\n" +
                "AND h_nr = '" + address.getHausnummer() + "';";

         */

        try {
            statement.setString(1,address.getPlz());
            statement.setString(2,address.getOrt());
            statement.setString(3,address.getStreet());
            statement.setInt(4,address.getHausnummer());
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, e);;
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return exists;
    }

    // PRÜFMETHODEN -END



}
