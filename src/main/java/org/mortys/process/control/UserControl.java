package org.mortys.process.control;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.mortys.gui.windows.DeleteUserConfirmationWindow;
import org.mortys.model.dao.StudentDAO;
import org.mortys.model.dao.UnternehmerDAO;
import org.mortys.model.dao.UserDAO;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.Roles;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserControl {

    private static UserControl userControl = null;
    private UserControl(){}
    public static UserControl getInstance(){
        if (userControl == null) return new UserControl();
        return userControl;
    }

    public static void deleteUser(User user){
        try {
            UserDAO.getInstance().removeUser(user);
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        UI.getCurrent().addWindow(new DeleteUserConfirmationWindow("Benutzer wurde erfolgreich gelöscht"));
    }



    public static void loadUserToSession (String email){
        User user = null;

        try {
            if (UserDAO.getInstance().isStudent(email)) {
                user = (Student) StudentDAO.getInstance().fetchUser(email);
            } else {
                user = (Unternehmer) UnternehmerDAO.getInstance().fetchUser(email);
            }
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }

        VaadinSession session = UI.getCurrent().getSession();
        session.setAttribute(user instanceof Student ? Roles.STUDENT : Roles.UNTERNEHMER, user);

    }
}