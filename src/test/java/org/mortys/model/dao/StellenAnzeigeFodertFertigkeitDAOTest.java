package org.mortys.model.dao;

import org.junit.Test;
import org.mortys.model.objects.dto.Fertigkeit;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.process.control.exception.DatabaseException;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class StellenAnzeigeFodertFertigkeitDAOTest {

    private Fertigkeit fertigkeit = new Fertigkeit();
    private StellenAnzeige stellenanzeige = new StellenAnzeige();

    @Test
    public void getInstanceTest () {

        assertNotNull(StellenAnzeigeFordertFertigkeitDAO.getInstance());
    }
    @Test
    public void fetchFertigkeitForStellenAnzeigeTest () throws DatabaseException {

        List <Fertigkeit> list = StellenAnzeigeFordertFertigkeitDAO.getInstance().
                fetchFertigkeitforStellenAnzeige(1);
        for(Fertigkeit f: list) {
            System.out.println("Fertigkeit für Stellenanzeige:");
            System.out.println("Softskill: " + f.getSoftskill());
            System.out.println("Beschreibung: " + f.getBeschreibung());
        }

    }
    @Test
    public void persistStellenAnzeigeFordertFertigkeit () throws DatabaseException {

        fertigkeit.setSoftskill("Java");
        fertigkeit.setBeschreibung("Experte");
        stellenanzeige.setId(5);


        StellenAnzeigeFordertFertigkeitDAO.getInstance().
                persistStellenAnzeigeFordertFertigkeit(stellenanzeige, fertigkeit);


    }

}
