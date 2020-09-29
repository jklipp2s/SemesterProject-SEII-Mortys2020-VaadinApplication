package org.mortys.model.dao;

import org.junit.Test;
import org.mortys.model.objects.dto.Fertigkeit;
import org.mortys.process.control.exception.DatabaseException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FertigkeitDAOTest {

    private Fertigkeit fertigkeit = new Fertigkeit();


    @Test
    public void getInstance() {

        assertNotNull(FertigkeitDAO.getInstance());
    }

    @Test
    public void persistFertigkeit() throws DatabaseException {

        fertigkeit.setSoftskill("Datenbank");
        fertigkeit.setBeschreibung("Gute Kenntnisse");
        FertigkeitDAO.getInstance().persistFertigkeit(fertigkeit);

    }

    @Test
    public void fetchFertigkeitAllTest() throws DatabaseException {

        String soft = "Python";
        String besch = "Kleine Anwendungen und Programme  schreiben";
        List<Fertigkeit> list = new ArrayList<Fertigkeit>();
        list = FertigkeitDAO.getInstance().fetchFertigkeitAll();
        System.out.println("Fertigkeiten:");
        for (Fertigkeit f : list) {
            System.out.println("Softskill: " + f.getSoftskill());
            System.out.println("Beschreibung: " + f.getBeschreibung());
            System.out.println("--");
        }
        assertEquals(soft, list.get(0).getSoftskill());
        assertEquals(besch, list.get(0).getBeschreibung());
    }
}