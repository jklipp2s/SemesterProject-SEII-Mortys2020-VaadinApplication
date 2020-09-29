package org.mortys.model.dao;

import org.junit.Test;
import org.mortys.model.objects.dto.Fertigkeit;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StudentHatFertigkeitDAOTest {

    private Student student = new Student();
    private Fertigkeit fertigkeit = new Fertigkeit();
    FertigkeitDAO fertigkeitDAO = new FertigkeitDAO();


    @Test
    public void getInstanceTest () {
        assertNotNull(StudentHatFertigkeitDAO.getInstance());
    }

    @Test
    public void fetchFertigkeitForStudent () throws DatabaseException{

        List<Fertigkeit> list =
        StudentHatFertigkeitDAO.getInstance().fetchFertigkeitforStudent("999999");

        assertEquals("Java", list.get(0).getSoftskill());

        /*for(Fertigkeit f: list) {
            System.out.println("Softskill: " + f.getSoftskill());
            System.out.println("Beschreibung: " + f.getBeschreibung());
        }
         */
    }

    @Test
    public void persistStudentHatFertigkeitTest () throws DatabaseException {

        student.setMatrikelnr("999999");
        fertigkeit.setSoftskill("Java");
        fertigkeit.setBeschreibung("Experte");
        fertigkeitDAO.getFertigkeitId(fertigkeit);
        StudentHatFertigkeitDAO.getInstance().persistStudentHatFertigkeit(student, fertigkeit);
    }

}
