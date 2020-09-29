package org.mortys.model.dao;


import org.junit.Test;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class StudentDAOTest {

    private Student student = new Student();

    @Test
    public void getIstanceTest () {

        assertNotNull(StudentDAO.getInstance());
    }
    @Test
    public void fetchAllUsersTest () throws DatabaseException {

        List<Student> list = StudentDAO.getInstance().fetchAllUsers();
        System.out.println("Studenten-Liste:");
        for(Student f: list) {
            System.out.println("Vorname: " + f.getVorname());
            System.out.println("Nachname: " + f.getNachname());
            System.out.println("Email: " + f.getEmail());
            System.out.println("Username: " + f.getUsername());
            System.out.println("Password: " + f.getPassword());
            System.out.println();
        }
    }

    @Test
    public void fetchUserTest () throws DatabaseException {

        StudentDAO.getInstance().fetchUser("UweKlug@dayrep.com");
    }

    @Test
    public void setBeschäftigungTest () throws DatabaseException {

        student.setMatrikelnr("123456789");
        student.setEmail("k@s.de");
        StudentDAO.getInstance().setBeschäftigung(student, "Werkstudent");

    }

    @Test
    public void setAnrede() throws DatabaseException {

        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().setAnrede(student, "Frau");

    }

    @Test
    public void setGeburtsDatum() throws DatabaseException {

        LocalDate gebDatum = LocalDate.of(2000, 1, 20);
        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().setGeburtsDatum(student, gebDatum);
    }

    @Test
    public void setNachname() throws DatabaseException {

        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().setNachname(student, "NachnameNeu");

    }

    @Test
    public void setVorname() throws DatabaseException {

        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().setVorname(student, "VornameNeu");

    }

    @Test
    public void setTelefon() throws DatabaseException {

        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().setTelefon(student, "0223344");

    }

    @Test
    public void setAvatar() throws DatabaseException {

        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().setAvatar(student, "AvatarNeu");

    }

    @Test
    public void changeUsername() throws DatabaseException {

        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().changeUsername(student, "UsernameNeu");

    }

    @Test
    public void setStatus() throws DatabaseException {

        student.setEmail("UserDaoNeu@test.de");
        UserDAO.getInstance().setStatus(student, "StatusNeu");

    }


}


