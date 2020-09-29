package org.mortys.services.db.databasecreation;


import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.DBTables;
import org.mortys.services.util.DBViews;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DataBaseCreation {


    public static void createTable(String sqlBefehl) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();
        Statement statement = JDBCConnection.getInstance().getStatement();

        try {
            statement.executeUpdate(sqlBefehl);
            System.out.println("Tabelle erfolgreich erstellt!");
        } catch (SQLException e) {
            Logger.getLogger(DataBaseCreation.class.getName()).log(Level.SEVERE, null, e);
            System.out.println(sqlBefehl);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }


    public static void setUpAll() throws DatabaseException {

        //Tables
        createTable(DBTables.Creation.CREATE_TAB_ADRESSE);
        createTable(DBTables.Creation.CREATE_TAB_USER);
        createTable(DBTables.Creation.CREATE_TAB_UNTERNEHMER);
        createTable(DBTables.Creation.CREATE_TAB_STUDENT);
        createTable(DBTables.Creation.CREATE_TAB_STELLENANZEIGE);
        createTable(DBTables.Creation.CREATE_TAB_STUDENT_BEWIRBT_ST);
        createTable(DBTables.Creation.CREATE_TAB_FERTIGKEIT);
        createTable(DBTables.Creation.CREATE_TAB_ST_FORDERT_F);
        createTable(DBTables.Creation.CREATE_TAB_ST_HAT_F);
        createTable(DBTables.Creation.CREATE_TAB_U_ERSTELLT_ST);
        createTable(DBTables.Creation.CREATE_TAB_USER_HAT_RESETCODE);
        createTable(DBTables.Creation.CREATE_TAB_TOGGLE_CONFIGURATION);

        //Views
        createTable(DBViews.Creation.CREATE_VIEW_ALL_STUDENTS);
        createTable(DBViews.Creation.CREATE_VIEW_ALL_UNTERNEHMER);
        createTable(DBViews.Creation.CREATE_VIEW_USER_HAS_ADDRESS);
        createTable(DBViews.Creation.CREATE_VIEW_STUDENT_HAT_FERTIGKEIT);
        createTable(DBViews.Creation.CREATE_VIEW_STELLENANZEIGE_FORDERT_FERTIGKEIT);
        createTable(DBViews.Creation.CREATE_VIEW_UNTERNEHMER_ERSTELLT_STELLENANZEIGE);
        createTable(DBViews.Creation.CREATE_VIEW_STUDENT_BEWIRBT_STELLENANZEIGE);


    }


}
