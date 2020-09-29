package org.mortys.model.dao;

import org.junit.Before;
import org.junit.Test;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class StudentBewirbtStellenAnzeigeDAOTest {

    private int  id = 49;
    private String matrikelnr = "999999";
    private String username = "regStu2s";

    private StellenAnzeige stellenAnzeige = new StellenAnzeige();
    private Student student = new Student();

    @Before
    public void setUp () {

        stellenAnzeige.setId(id);
        student.setMatrikelnr(matrikelnr);
        student.setUsername(username);

    }
    @Test
    public void getInstanceTest () {
        assertNotNull(StudentBewirbtStellenAnzeigeDAO.getInstance());
    }


    @Test
    public void fetchStellenanzeigeStudent () throws DatabaseException {

        List<StellenAnzeige> list = new ArrayList<StellenAnzeige>();
        list = StudentBewirbtStellenAnzeigeDAO.getInstance().fetchStellenAnzeigeStudent(student);
        System.out.println("StellenAnzeige:");
        for (StellenAnzeige f : list) {
            System.out.println("id: " + f.getId());
            System.out.println("Titel: " + f.getTitel());
            System.out.println("Beschreibung: " + f.getBeschreibung());
            System.out.println("Status: " + f.getStatus());
            System.out.println("Erstallungsdatum: " + f.getErstellungsDatum());
            System.out.println("Unternehmen: " + f.getUnternehmen());
            System.out.println("--");
        }
    }


    @Test
    public void persistStudentBewirbtStellenAnzeigeTest () throws DatabaseException {

        StudentBewirbtStellenAnzeigeDAO.getInstance().
                persistStudentBewirbtStellenanzeige(student, stellenAnzeige);
    }

    @Test
    public void changeStatusTest () throws DatabaseException {

        StudentBewirbtStellenAnzeigeDAO.getInstance().
                changeStatus(student, stellenAnzeige, "beworben");
    }

}
