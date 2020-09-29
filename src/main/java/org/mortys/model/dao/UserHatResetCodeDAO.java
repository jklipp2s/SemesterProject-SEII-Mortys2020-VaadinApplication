package org.mortys.model.dao;

import org.mortys.process.control.LoginControl;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.UserNotFoundException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.DBTables;
import org.mortys.services.util.PasswordResetData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserHatResetCodeDAO extends AbstractDAO {

    private static UserHatResetCodeDAO userHatResetCodeDAO = null;
    private String table = DBTables.TAB_USER_HAT_RESETCODE;

    private UserHatResetCodeDAO() {

    }

    public static synchronized UserHatResetCodeDAO getInstance() {
        if (userHatResetCodeDAO == null)
            userHatResetCodeDAO = new UserHatResetCodeDAO();
        return userHatResetCodeDAO;
    }


    /**
     * loescht alle Resetcodes, die aelter als das Limit seit dessen Erstellung sind
     * @return
     * @throws DatabaseException
     */
    public void deleteTooOldResetCodes() throws DatabaseException {
        // loescht alle codes, die qelter als 10min sind
        // Verbindung wird beim Aufrufer geöffnet
        String sqlBefehl = "DELETE FROM dbs_tab_user_hat_resetcode WHERE erstellt < now()-'"
                + PasswordResetData.LIMIT_VALID_VALUE + " "
                + PasswordResetData.LIMIT_VALID_UNIT + "'::interval";
        try {
            getStatement().executeUpdate(sqlBefehl);
        } catch (SQLException e) {
            Logger.getLogger(UserHatResetCodeDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        // kein close(), da nach der Methode eine weitere mit DB-Zugriff folgt
    }

    /**
     * Erzeugt ein zufällig generiertes 6-stelligen Code zum reseten des Passwortes.
     * @param email
     * @return
     * @throws DatabaseException
     */
    public String insertResetCodeByUser(String email, String resetcode) throws DatabaseException, UserNotFoundException {
        JDBCConnection.getInstance().openConnection();

        String randomCode = LoginControl.generateResetCode();

        String sqlBefehl = "INSERT INTO " + table + " VALUES (\'"
                + email + "\',\'"
                + randomCode
                + "\',default)";
        try {
            getStatement().executeUpdate(sqlBefehl);
        } catch (SQLException e) {
            Logger.getLogger(UserHatResetCodeDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseException("ResetCode konnte nicht gespeichert werden");
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
        return randomCode;
    }

    /**
     * prüft, ob User bereits ein Code angefordert hat, welches noch gültig ist
     * @param email
     * @return
     */
    public boolean resetCodeIsAlreadyExistsByUser(String email) {
        String sqlBefehl = "SELECT email FROM " + table + " WHERE email = \'" + email + "\'";
        ResultSet resultSet;
        boolean exists = false;
        try {
            resultSet = getStatement().executeQuery(sqlBefehl);
            exists = resultSet.next();
        } catch (SQLException e) {
            Logger.getLogger(UserHatResetCodeDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return exists;

    }

    /**
     * löscht von dem User gültigenn Code
     * @param email
     */
    public void deleteExistendResetCodeByUser(String email) {
        String sqlBefehl = "DELETE FROM " + table + " WHERE email = \'" + email + "\'";
        try {
            getStatement().executeUpdate(sqlBefehl);
        } catch (SQLException e) {
            Logger.getLogger(UserHatResetCodeDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String getResetCodeByUser(String email) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        deleteTooOldResetCodes();
        String sqlBefehl = "SELECT code FROM " + table + " WHERE email = '" + email + "'";
        ResultSet resultSet = null;
        String resetcode = null;

        try {
            resultSet = getStatement().executeQuery(sqlBefehl);
            resultSet.next();
            resetcode = resultSet.getString(1);
        } catch (SQLException e) {
            Logger.getLogger(UserHatResetCodeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
        return resetcode;
    }

    public void resetPasswort(String email, String password) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE dbs_tab_user SET password = '" + password + "' WHERE email = '" + email + "'";
        try {
            getStatement().executeUpdate(sqlBefehl);
        } catch (SQLException e) {
            Logger.getLogger(UserHatResetCodeDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }
}
