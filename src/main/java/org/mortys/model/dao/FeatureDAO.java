package org.mortys.model.dao;

import org.mortys.gui.components.CheckBoxFeature;
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

public class FeatureDAO extends AbstractDAO {

    private static FeatureDAO featureDAO;
    private String table = DBTables.TAB_TOGGLE_CONFIGURATION;

    private FeatureDAO() {

    }

    public static synchronized FeatureDAO getInstance() {

        if (featureDAO == null)
            featureDAO = new FeatureDAO();

        return featureDAO;
    }

    public void insertFeatureToggle(CheckBoxFeature feature) throws DatabaseException {
        if (feature.getFeature() == null || feature.getFeature().length() == 0 || feature.getFeature().contains(" "))
            throw new IllegalArgumentException("Feature nicht beschriftet!");

        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "INSERT INTO " + table + " VALUES(?,?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);


        try {
            final String featureStr, captionStr;
            featureStr = feature.getFeature();
            captionStr = feature.getCaption();

            statement.setString(1, featureStr);
            statement.setBoolean(2, feature.getValue());
            statement.setString(3, captionStr == null ? "" : captionStr);

            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(FeatureDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    public CheckBoxFeature fetchFeatureToggle(String feature) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet toggleData = null;
        String sqlBefehl = "SELECT * FROM " + table + " WHERE feature = ?;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        CheckBoxFeature checkBoxFeature = new CheckBoxFeature();

        try {
            statement.setString(1,feature);

            toggleData = statement.executeQuery();

            if (toggleData == null)
                return null;

            if (toggleData.next()) {
                String caption = toggleData.getString(3);
                checkBoxFeature.setFeature(toggleData.getString(1));
                checkBoxFeature.setValue(toggleData.getBoolean(2));
                checkBoxFeature.setCaption((caption.equals("") ? null : caption));
            } else {
                return null;
            }
        } catch (SQLException e) {
            Logger.getLogger(FeatureDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (toggleData != null) toggleData.close(); } catch (Exception exc) { }
        }

        return checkBoxFeature;
    }

    public List<CheckBoxFeature> fetchAllToggles() throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "SELECT * FROM " + table + ";";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        //String sqlBefehl = "SELECT * FROM " + table + ";";

        List<CheckBoxFeature> checkBoxFeatureList = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                CheckBoxFeature tmp = new CheckBoxFeature();

                tmp.setFeature(resultSet.getString(1));
                tmp.setValue(resultSet.getBoolean(2));
                tmp.setCaption(resultSet.getString(3));

                checkBoxFeatureList.add(tmp);

            }
        } catch (SQLException e) {
            Logger.getLogger(FeatureDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return checkBoxFeatureList;

    }

    public void deleteToggle(CheckBoxFeature checkBoxFeature) throws DatabaseException {
        String featureString = checkBoxFeature.getFeature();
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "DELETE FROM " + table + " WHERE feature = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1,checkBoxFeature.getFeature());
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(FeatureDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();

        }
    }

    public void deleteAllToggle() throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "DELETE FROM " + table + ";";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(FeatureDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }


    public void changeToggleStatus(String feature, boolean changeTo) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        //String sql = "UPDATE " + table + " SET status = " + changeTo + " WHERE feature = '" + feature + "';";
        String sql = "UPDATE " + table + " SET status = ? WHERE feature = ?;";
        PreparedStatement statement = getPreparedStatement(sql);
        try {
            statement.setBoolean(1,changeTo);
            statement.setString(2,feature);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(FeatureDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }



    /*
    public void changeToggleFeature(String feature, String changeTo) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sql = "UPDATE " + table + " SET feature = '" + changeTo + "' WHERE feature = '" + feature + "';";
        try {
            detectSQLInjection(feature);
            detectSQLInjection(changeTo);
            getStatement().executeUpdate(sql);
        } catch (SQLException e) {
            Logger.geFeatureDAO(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }

     */

    public void changeToggleCaption(String feature, String changeTo) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        //String sql = "UPDATE " + table + " SET caption = '" + changeTo + "' WHERE feature = '" + feature + "'";
        String sql = "UPDATE " + table + " SET caption = ? WHERE feature = ?";
        PreparedStatement statement = getPreparedStatement(sql);

        try {
            statement.setString(1,changeTo);
            statement.setString(2,feature);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(FeatureDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

}
