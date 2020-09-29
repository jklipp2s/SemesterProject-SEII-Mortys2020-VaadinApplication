package org.mortys.process.control;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.mortys.model.dao.StudentDAO;
import org.mortys.model.dao.UnternehmerDAO;
import org.mortys.model.dao.UserDAO;
import org.mortys.model.dao.UserHatResetCodeDAO;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.NoSuchUserOrPassword;
import org.mortys.process.control.exception.UserNotFoundException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.mail.MailSenderConnection;
import org.mortys.services.util.MailMsgTemplates;
import org.mortys.services.util.Roles;
import org.mortys.services.util.Views;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginControl {

    public static void checkAuthentication(String email, String password) throws NoSuchUserOrPassword, DatabaseException {

        User user = null;
        boolean correct = false;
        try {
            correct = UserDAO.getInstance().isPasswordCorrect(email, password);
        }
        catch (DatabaseException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
            try {
                if (correct) {
                    if (UserDAO.getInstance().isStudent(email)) {
                        user = (Student) StudentDAO.getInstance().fetchUser(email);
                    } else {
                        user = (Unternehmer) UnternehmerDAO.getInstance().fetchUser(email);
                    }
                } else {
                    throw new NoSuchUserOrPassword("Wrong Email or Password. Please try again.");
                }
            } catch (DatabaseException ex) {
                Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            }

            VaadinSession session = UI.getCurrent().getSession();
            session.setAttribute(Roles.CURRENT_USER, user);
            session.setAttribute(user instanceof Student ? Roles.STUDENT : Roles.UNTERNEHMER, user);

            UI.getCurrent().getNavigator().navigateTo(Views.PROFILE);
    }

    public static void logoutUser() {
        UI.getCurrent().getSession().close();
        UI.getCurrent().getPage().setLocation(Views.MAIN);
    }

    /**
     * Methode, zum reseten des Passwortes
     * @param email
     * @return
     * @throws UserNotFoundException
     */
    public static boolean sendResetCode(String email) throws UserNotFoundException {

        MailSenderConnection mailConnection = MailSenderConnection.getInstance();
        UserHatResetCodeDAO resetCodeDAO = UserHatResetCodeDAO.getInstance();

        if (resetCodeDAO.resetCodeIsAlreadyExistsByUser(email))
            resetCodeDAO.deleteExistendResetCodeByUser(email);

        try {
            if (!UserDAO.getInstance().emailIsAlreadyInUse(email))
                throw new UserNotFoundException("User nicht registriert!");
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            String randomCode = resetCodeDAO.insertResetCodeByUser(email,generateResetCode());
            mailConnection.send(
                    email,
                    MailMsgTemplates.resetPWSubject,
                    MailMsgTemplates.resetPWMsg +
                            randomCode.substring(0,3) + " "  + randomCode.substring(3,6));
        } catch (UnsupportedEncodingException | MessagingException | DatabaseException  e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean checkValidResetCode(String email, String code) {
        UserHatResetCodeDAO resetCodeDAO = UserHatResetCodeDAO.getInstance();

        if (code.length() == 6 && code != null || !resetCodeDAO.resetCodeIsAlreadyExistsByUser(email)) {
            String resetcodeFromDB = null;

            try {
                resetCodeDAO.deleteTooOldResetCodes();
                resetcodeFromDB = resetCodeDAO.getResetCodeByUser(email);
            } catch (DatabaseException e) {
                Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
                return false;
            }

            if (resetcodeFromDB != null) {
                if (code.equals(resetcodeFromDB)) {
                    resetCodeDAO.deleteExistendResetCodeByUser(email);
                    return true;
                }
            }
        }
        return false;
    }

    public static void changePassword(String email, String password1, String password2) {
        if (!password1.equals(password2))
            throw new IllegalArgumentException("Passwort nicht identisch");
        changePasswort(email,password1);
    }

    private static boolean changePasswort(String email, String password) {
        try {
            UserHatResetCodeDAO.getInstance().resetPasswort(email, password);
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    public static String generateResetCode() {
        SecureRandom r = new SecureRandom();
        String random = "" + Math.round(r.nextDouble() * 1000000) % 1000000;
        int length = random.length();

        if (length > 6)
            throw new IllegalStateException("Code über 6 Zeichen lang!");

        String nullValue = "";
        for (int i = 0; i < 6-length; i++) {
            nullValue += 0;
        }
        return nullValue + random;
    }

}
