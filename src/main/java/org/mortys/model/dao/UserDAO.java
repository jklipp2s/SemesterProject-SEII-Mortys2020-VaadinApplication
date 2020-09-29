package org.mortys.model.dao;

import org.mortys.model.objects.dto.Address;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.DBTables;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO extends AbstractDAO {
    private static UserDAO userDAO;
    private String table = DBTables.TAB_USER;

    public static synchronized UserDAO getInstance() {

        if (userDAO == null) userDAO = new UserDAO();

        return userDAO;
    }

    public Boolean resultGetter(PreparedStatement statement, int index, String condition) throws DatabaseException {
        ResultSet resultSet = null;
        boolean exists = false;

        try {
            statement.setString(index,condition);
            resultSet = statement.executeQuery();
            exists = resultSet.next();
        }
        catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
        return exists;
    }


    // PERSIST-METHODEN -START -----------------------------------------------------------------------------------------

    public void registerUser(User user, String password) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "INSERT INTO " + table + " VALUES(null,null,null,null,?,null,default,?,?,null,null)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUsername());
            statement.setString(3, password);

            statement.executeUpdate();

            user.setRegistrationDate(getRegistrationDate(user));

        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    protected void prepersistUser(User user, Address address, String password) throws DatabaseException {

        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "INSERT INTO " + table + " VALUES(?,?,?,?,?,?,DEFAULT,?,?,?,?,?)";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.setString(1, user.getAnrede());
            statement.setDate(2, Date.valueOf(user.getGeburtsdatum()));
            statement.setString(3, user.getNachname());
            statement.setString(4, user.getVorname());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getTelefon());
            statement.setString(7, user.getUsername());
            statement.setString(8, password);
            statement.setString(9, user.getStatus());
            statement.setString(10, user.getAvatar());
            statement.setString(11, null);

            int adressId = AddressDAO.getInstance().persistAddress(address);

            statement.setInt(11, adressId);

            user.setRegistrationDate(getRegistrationDate(user));

            statement.executeUpdate();



        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // PERSIST-METHODEN -END

    // HILFSMETHODEN -START --------------------------------------------------------------------------------------------

    protected LocalDate getRegistrationDate(User user) {

        ResultSet resultSet = null;
        /*
        String sqlBefehl = "SELECT regdate FROM " + table + " WHERE email = '" + user.getEmail() +
                "';";
         */
        String sqlBefehl = "SELECT regdate FROM " + table + " WHERE email = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        LocalDate localDate = null;

        try {
            statement.setString(1,user.getEmail());
            resultSet = statement.executeQuery();
            if(resultSet.next())
            localDate = resultSet.getDate(1).toLocalDate();

        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try { if (resultSet != null) resultSet.close(); } catch (Exception exc) { }
        }

        return localDate;

    }
    // HILFSMETHODEN -END

    // PRÜFMETHODEN -START ----------------------------------------------------------------------------------------------

    public boolean emailIsAlreadyInUse(String email) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet resultSet = null;
        boolean exists = false;
        //String sqlBefehl = "SELECT email  FROM " + table + " WHERE email = '" + email + "';";
        String sqlBefehl = "SELECT email  FROM " + table + " WHERE email = ?;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);


        return resultGetter(statement,1,email);
    }

    public boolean usernameIsAlreadyInUse(String username) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet resultSet = null;
        boolean exists = false;
        //String sqlBefehl = "SELECT username  FROM " + table + " WHERE username = '" + username + "';";
        String sqlBefehl = "SELECT username  FROM " + table + " WHERE username = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        return resultGetter(statement, 1, username);
    }


    public boolean isPasswordCorrect(String email, String password) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet resultSet = null;

        /*
        String sqlBefehl = "SELECT email  FROM " + table + " WHERE email ILIKE '" + email + "'" +
                "AND password = '" + password + "';";
         */
        String sqlBefehl = "SELECT email  FROM " + table + " WHERE email ILIKE ? AND password = ?;";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        boolean result = false;
        try {
            statement.setString(1,email);
        } catch (SQLException throwables) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, throwables);
        }
        return resultGetter(statement,2,password);

    }


    public boolean isStudent(String email) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet resultSet = null;

        /*
           String sqlBefehl = "SELECT u.email  FROM " + table + " u, " + DBTables.TAB_STUDENT + " s WHERE u.email = '" + email + "'\n" +
                "AND u.email = s.email;";
         */
        String sqlBefehl = "SELECT u.email  FROM " + table + " u, " + DBTables.TAB_STUDENT + " s WHERE u.email = ? AND u.email = s.email;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        boolean result = false;
        return resultGetter(statement,1,email);
    }


    public boolean isUnternehmer(String email) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        ResultSet resultSet = null;

        /*
        String sqlBefehl = "SELECT u.email  FROM " + table + " u, " + DBTables.TAB_UNTERNEHMER + " un WHERE u.email = '" + email + "'\n" +
                "AND u.email = un.email;";
         */

        String sqlBefehl = "SELECT u.email  FROM " + table + " u, " + DBTables.TAB_UNTERNEHMER + " un WHERE u.email = ? AND u.email = un.email;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        boolean result = false;
        return resultGetter(statement,1,email);
    }

    // PRÜFMETHODEN -END



    // SETTERMETHODEN -START -----------------------------------------------------------------------------------------------
    // Setter Method's bitte für die ProfilPage verwenden
    // Bitte auch immer checken ob die Bedingungen -> TABLECONSTRAINTS, USE-CASE-ANFORDERUNGEN  <- erfüllt sind

    public void setAnrede(User user, String anrede) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET anrede = '" + anrede + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Anrede geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }


    public void setGeburtsDatum(User user, LocalDate localDate) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET geburtsdatum = '" + Date.valueOf(localDate) + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("GeburtsDatum geändert.");
        } catch (SQLException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }




    public void changeEmail(User user, String email, boolean isUnternehmer) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();


        String sqlBefehl = isUnternehmer ? "ALTER TABLE " + DBTables.TAB_UNTERNEHMER + " ALTER COLUMN  email DROP not null;\n"
                : "ALTER TABLE " + DBTables.TAB_STUDENT + " ALTER COLUMN  email DROP not null;\n";

        sqlBefehl += isUnternehmer ? "UPDATE " + DBTables.TAB_UNTERNEHMER + " SET email = null WHERE email = '"
                + user.getEmail() + "';\n\n\n" : "" +
                "UPDATE " + DBTables.TAB_STUDENT + " SET email = null WHERE email = '"
                + user.getEmail() + "';\n\n\n";

        sqlBefehl += "UPDATE " + table + " SET email = '" + email + "' WHERE email = '"
                + user.getEmail() + "';";


        sqlBefehl += isUnternehmer ? "UPDATE " + DBTables.TAB_UNTERNEHMER + " SET email = '" + email + "' WHERE kunden_nr = '"
                + ((Unternehmer) user).getKundennummer() + "';\n\n\n" : "" +
                "UPDATE " + DBTables.TAB_STUDENT + " SET email = '" + email + "' WHERE matrikelnr = '"
                + ((Student) user).getMatrikelnr() + "';\n\n\n";

        sqlBefehl += isUnternehmer ? "ALTER TABLE " + DBTables.TAB_UNTERNEHMER + " ALTER COLUMN  email SET not null;"
                : "ALTER TABLE " + DBTables.TAB_STUDENT + " ALTER COLUMN  email SET not null;";

        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Email geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }


    public void setNachname(User user, String nachname) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET nachname = '" + nachname + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Nachname geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }

    public void setVorname(User user, String vorname) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET vorname = '" + vorname + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Vorname geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }


    public void setTelefon(User user, String telefon) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET telefon = '" + telefon + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Telefonnummer geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }

    }

    public void setAvatar(User user, String avatar) throws DatabaseException{
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET avatar = '" + avatar + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Avatar geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }


    public void changeUsername(User user, String username) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET username = '" + username + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Username geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }


    public void changePassword(User user, String password) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET password = '" + password + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Password geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    public void setStatus(User user, String satus) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        String sqlBefehl = "UPDATE " + table + " SET status = '" + satus + "' WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);

        try {
            statement.executeUpdate();
            System.out.println("Status geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    public void setAdresse(User user, Address adresse) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();
        int adressid = AddressDAO.getInstance().persistAddress(adresse);
        String sqlBefehl = "UPDATE " + table + " SET adresse = " + adressid + " WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("Adresse geändert.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // SETTERMETHODEN -END

   // LOESCHMETHODEN -START -----------------------------------------------------------------------------------------------

    public void removeUser(User user) throws DatabaseException {
        JDBCConnection.getInstance().openConnection();

        String sqlBefehl = user instanceof Unternehmer ? "DELETE FROM " + DBTables.TAB_U_ERSTELLT_ST + " WHERE unternehmer = '" +
                ((Unternehmer) user).getKundennummer() + "';\n" +
                "DELETE FROM " + DBTables.TAB_UNTERNEHMER + " WHERE email = '" +
                user.getEmail() + "';\n" +
                "UPDATE " + DBTables.TAB_STELLENANZEIGE + " s SET status = 'deleted' where not exists (Select  " +
                "from " + DBTables.TAB_U_ERSTELLT_ST  + "\n" +
                "WHERE stellenanzeige = s.id);\n" +
                "UPDATE " + DBTables.TAB_STUDENT_BEWIRBT_ST + " s SET status = 'deleted' where not exists (Select  " +
                "from " + DBTables.TAB_U_ERSTELLT_ST  + "\n" +
                "WHERE stellenanzeige = s.stellenanzeige);\n"
                                        :
                "DELETE FROM " + DBTables.TAB_STUDENT_BEWIRBT_ST + " WHERE student = '" + ((Student) user).getMatrikelnr() + "';\n" +
                        "DELETE FROM " + DBTables.TAB_ST_HAT_F + " WHERE student = '" + ((Student) user).getMatrikelnr() + "';\n" +
                        "DELETE FROM " + DBTables.TAB_STUDENT + " WHERE email = '" +
                        user.getEmail() + "';\n";


        sqlBefehl += "DELETE FROM " + table + " WHERE email = '"
                + user.getEmail() + "';";
        PreparedStatement statement = getPreparedStatement(sqlBefehl);
        try {
            statement.executeUpdate();
            System.out.println("User " + user.getUsername() + " aus der Datenbank enfernt.");
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            JDBCConnection.getInstance().closeConnection();
        }
    }

    // LOESCHMETHODEN -END


}
