package org.mortys.model.dao;


import org.junit.Before;
import org.junit.Test;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.process.control.exception.DatabaseException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class StellenAnzeigeDAOTest {

    private int id = 12;
    private String unternehmen = "5";
    private String ort = "Sankt Augustin";
    private String titel = "Testprogrammierer";
    private String beschreibung = "Tester gesucht";
    private String status = "offen";
    LocalDate erstellungsDatum = LocalDate.now();

    private StellenAnzeige stellenAnzeige = new StellenAnzeige();

    @Before
    public void setUp () {

        stellenAnzeige.setId(id);
        stellenAnzeige.setBeschreibung(beschreibung);
        stellenAnzeige.setStatus(status);
        stellenAnzeige.setTitel(titel);
        stellenAnzeige.setUnternehmen(unternehmen);
        stellenAnzeige.setErstellungsDatum(erstellungsDatum);
        stellenAnzeige.setOrt(ort);


    }

    @Test
    public void getInstanceTest () {

        assertNotNull(StellenAnzeigeDAO.getInstance());
    }

    @Test
    public void fetchAllStellenAnzeigenTest () throws DatabaseException {

        List<StellenAnzeige> list = StellenAnzeigeDAO.getInstance().fetchAllStellenAnzeigen();
        for(StellenAnzeige f: list) {
            System.out.println("Stellenanzeige:");
            System.out.println("id: " + f.getId());
            System.out.println("Titel: " + f.getTitel());
            System.out.println("Beschreibung: " + f.getBeschreibung());
            System.out.println("Status: " + f.getStatus());
            System.out.println("Firmenname: " + f.getUnternehmen());
            System.out.println("Ort: " + f.getOrt());
            System.out.println("Erstellungsdatum: " + f.getErstellungsDatum());
            System.out.println();
        }
    }

    @Test
    public void persistStellenanzeigeTest () throws DatabaseException {

        StellenAnzeigeDAO.getInstance().persistStellenAnzeige(stellenAnzeige);
        assertNotNull(stellenAnzeige);

    }

    @Test
    public void changeStatusTest () throws DatabaseException {

        StellenAnzeigeDAO.getInstance().changeStatus(stellenAnzeige, "offen");
        assertNotEquals("closed", stellenAnzeige.getStatus());
        assertEquals("offen", stellenAnzeige.getStatus());

    }

}