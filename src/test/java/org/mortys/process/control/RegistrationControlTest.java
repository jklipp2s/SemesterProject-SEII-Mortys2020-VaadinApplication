package org.mortys.process.control;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mortys.model.dao.UnternehmerDAO;
import org.mortys.model.dao.UserDAO;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.UserDTO;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.NoSuchUserOrPassword;
import org.mortys.process.control.exception.RegisterFailException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

//Ein Selenium Test hier in java bauen!!
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith( MockitoJUnitRunner.class )
public class RegistrationControlTest {

    private UserDTO student;
    @Mock
    private UI myUi;
    @Mock
    private VaadinSession session;

    @Before
    public void setup() {
        //Test-Daten für den Student
        student = new UserDTO();
        student.setEmail("alextest1@vmail.inf.h-brs.de");
        student.setUsername("atest2s");
        student.setPassword("testmann");
        student.setMatrnr("1001001");
        student.setUnternehmer(false);

        UI.setCurrent( myUi );
        when ( myUi.getSession() ).thenReturn( session );
    }

    @Test
    public void _01_emptyLogin() {
        boolean empty0 = false;
        try {
            LoginControl.checkAuthentication("", "");
        } catch (NoSuchUserOrPassword noSuchUserOrPassword) {
            empty0 = true;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(true, empty0);

        boolean empty1 = false;
        try {
            LoginControl.checkAuthentication(student.getEmail(), "");
        } catch (NoSuchUserOrPassword noSuchUserOrPassword) {
            empty1 = true;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(true, empty1);

        boolean empty2 = false;
        try {
            LoginControl.checkAuthentication("", student.getPassword());
        } catch (NoSuchUserOrPassword noSuchUserOrPassword) {
            empty2 = true;
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(true, empty2);
    }

    @Test
    public void _02_registerStudent() throws DatabaseException, RegisterFailException {
        boolean success = RegistrationControl.register(student);
        assertEquals(true, success);
    }

    @Test
    public void _02_registerUnternehmer() throws DatabaseException, RegisterFailException {
       UserDTO userDTO = new UserDTO();

       userDTO.setEmail("Test@testuntenehmer.de");
       userDTO.setUsername("TestUser");
       userDTO.setUnternehmer(true);
       userDTO.setFirma("TestFirma");
       userDTO.setPassword("password");



        boolean success = RegistrationControl.register(userDTO);
        assertEquals(true, success);

        Unternehmer unternehmer = UnternehmerDAO.getInstance().fetchUser("Test@testuntenehmer.de");
        UserDAO.getInstance().removeUser(unternehmer);


    }

    @Test
    public void _03_login() {
        try {
            LoginControl.checkAuthentication(student.getEmail(), student.getPassword());
        } catch (NoSuchUserOrPassword noSuchUserOrPassword) {
            noSuchUserOrPassword.printStackTrace();
        } catch (DatabaseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            //Fängt das navigateTo(Views.PROFILE) auf!
        }
        System.out.println(session.getLocale());
    }

    @Test
    public void _04_delete() {
        Student studentUser = new Student();
        studentUser.setEmail(student.getEmail());
        studentUser.setUsername(student.getUsername());
        studentUser.setMatrikelnr(student.getMatrnr());
        try {
            UserControl.deleteUser(studentUser);
        } catch (NullPointerException e) {
            //Fängt das DeleteUserConfirmationWindow auf!
        }
    }


    @Test(expected = RegisterFailException.class)
    public void _05_test_RegisterFailException() throws DatabaseException, RegisterFailException {
        UserDTO userDTO = new UserDTO();

        // Zu langer Name
        userDTO.setUsername("TestUserThatIsMuuuuuuuchhToooLoooooooooooooooooooooooooooooooooong");
        // Zu kurzes Passwort
        userDTO.setPassword("passwor");
        //Email schon vergeben
        userDTO.setEmail("BrigitteKunze@einrot.com");
        // Zu lange MatrikelNummer
        userDTO.setMatrnr("12346652652652568464464686864684646846626262262+2");

        RegistrationControl.register(userDTO);

    }



}
