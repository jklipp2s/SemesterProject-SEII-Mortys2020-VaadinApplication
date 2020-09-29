package org.mortys.model.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortys.model.objects.dto.Address;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.UserDTO;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.NoSuchUserOrPassword;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDAOTest {


    private String usernameSTU = "StudentDAOTest";
    private String emailSTU = "StudentDao@test.de";
    private String matrikelnummer = "999111";
    private String passwordSTU = "passwordTest";

    private String unsernameUN = "UNDAOtest";
    private String firmenname = "TestUNDAO GmbH";
    private String emailUN = "UNDAO@test.de";
    private String passwordUN = "passwordUNTest";

    private Address address = new Address();
    private LocalDate registrationDate = LocalDate.now();
    private LocalDate geburtsdatum = LocalDate.of(2000, 6, 24);

    private Student student = new Student();
    private Unternehmer unternehmer = new Unternehmer();
    private UserDTO user = new UserDTO();

    @Before
    public void setUp() {


        student.setEmail(emailSTU);
        student.setUsername(usernameSTU);
        student.setMatrikelnr(matrikelnummer);
        student.setPassword(passwordSTU);



        unternehmer.setEmail(emailUN);
        unternehmer.setFirmenname(firmenname);
        unternehmer.setUsername(unsernameUN);
        unternehmer.setPassword(passwordUN);

    }

    @After
    @Test
    public void removeUserTest() throws DatabaseException {

        UserDAO.getInstance().removeUser(student);
        UserDAO.getInstance().removeUser(unternehmer);

    }

    @Test
    public void getInstanceTest() {

        assertNotNull(StudentDAO.getInstance());
    }

    @Test
    public void registerStudentTest() throws DatabaseException {

        StudentDAO.getInstance().registerStudent(student, passwordSTU);

    }

    @Test
    public void registerUnternehmer() throws DatabaseException {

        UnternehmerDAO.getInstance().registerUnternehmer(unternehmer, passwordUN);
    }

    @Test
    public void emailIsAlreadyInUseTest() throws DatabaseException {

        UserDAO.getInstance().emailIsAlreadyInUse("DanielHertzog@teleworm.us");

    }

    @Test
    public void usernameIsAlreadyInUseTest() throws DatabaseException {

        UserDAO.getInstance().usernameIsAlreadyInUse("testStu1");

    }

    @Test
    public void isPasswordCorrectTest() throws DatabaseException {

        UserDAO.getInstance().isPasswordCorrect("teststu@test.de", "testStu1");

    }

    @Test
    public void isStudentTest() throws DatabaseException {

        boolean result = UserDAO.getInstance().isStudent("teststu@test.de");
        assertEquals(true, result);
        result = UserDAO.getInstance().isStudent("MarcoSchmitt@gustr.com");
        assertEquals(false, result);
    }

    @Test
    public void isUnternehmerTest() throws DatabaseException {

        boolean result = UserDAO.getInstance().isUnternehmer("MarcoSchmitt@gustr.com");
        assertEquals(true, result);
        result = UserDAO.getInstance().isUnternehmer("teststu@test.de");
        assertEquals(false, result);
    }

    @Test
    public void changeEmail() throws DatabaseException {
        UserDAO.getInstance().changeEmail(unternehmer, "newTestmail@Test.de", true);
        assertNotNull(UnternehmerDAO.getInstance().fetchUser("newTestmail@Test.de"));
        UserDAO.getInstance().changeEmail(student, "newTest@Testmail.com", false);
        assertNotNull(UnternehmerDAO.getInstance().fetchUser("newTest@Testmail.com"));
    }


    @Test
    public void changePasswordTest() throws DatabaseException {
        Unternehmer unternehmer1 = new Unternehmer();
        unternehmer1.setEmail("@exampleemail.org");
        unternehmer1.setPassword("password");
        unternehmer1.setUsername("userTest");
        unternehmer1.setIban("2312312312312312");

        UnternehmerDAO.getInstance().registerUser(unternehmer1,unternehmer1.getPassword());
        unternehmer = UnternehmerDAO.getInstance().fetchUser(unternehmer1.getEmail());
        UserDAO.getInstance().changePassword(unternehmer1, "newPassword");
        boolean loggedIn = UserDAO.getInstance().isPasswordCorrect("@exampleemail.org", "newPassword");

        assertEquals(true, loggedIn);
        UserDAO.getInstance().removeUser(unternehmer1);
    }
}