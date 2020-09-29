package org.mortys.services.db;

import org.junit.Test;
import org.mortys.model.dao.AddressDAO;
import org.mortys.process.control.exception.DatabaseException;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JDBCConnectionTest {

    private String url = "jdbc:postgresql://dumbo.inf.h-brs.de/mmuel72s";
    private String uName = "mmuel72s";
    private String uPass = "mmuel72s";
    private Connection conn = null;

    public void close(Statement st, ResultSet rs) {
        try {
            if (conn != null) conn.close();
            if (st != null) st.close();
            if (rs != null) rs.close();
        } catch (Exception exc) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, exc);
        }
    }


    @Test
    public void JDBCConnectionTest() throws SQLException {

        ResultSet rs = null;
        Statement st = null;
        try {
            conn = DriverManager.getConnection(url, uName, uPass);

            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM public.dbs_tab_user");
            while (rs.next()) {
                System.out.println("User: " + rs.getString("vorname"));
            }
        } catch (SQLException e) {
            Logger.getLogger(AddressDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
                close(st, rs);
        }

    }

    /*
     * dieser Test überprüft, ob der Treiber geladen werden kann
     */

    @Test
    public void dbDriverTest() {
        System.out.println("Driver ist Loading ...");

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver!", e);
        }
    }

    /*
     * Überprüfung des Singelten und not null
     */
    @Test
    public void getInstanceTest() throws DatabaseException {
        JDBCConnection con = JDBCConnection.getInstance();

        assertNotNull(con);
        assertSame(con, JDBCConnection.getInstance());
    }

    /*
     * Überprüfung auf Schließung der Verbindung
     */
    @Test
    public void closeConnection() throws SQLException {
        conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(url, uName, uPass);
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM public.dbs_tab_user");
            while (rs.next()) {
                System.out.println("User: " + rs.getString("nachname"));
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCConnectionTest.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close(st, rs);
        }

        assertTrue(conn.isClosed());
    }

}