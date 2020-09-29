package org.mortys.model.dao;

import org.junit.Test;
import org.mortys.model.objects.dto.Address;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.process.control.exception.DatabaseException;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UnternehmerErstelltStellenAnzeigeDAOTest {

    private Unternehmer createUnternehmerWithAddress() throws DatabaseException {
        Address address = AddressDAO.getInstance().fetchAllAddress().get(0);

        Unternehmer unternehmer = new Unternehmer();
        unternehmer.setEmail("mail8.@TestStellenanzeige");
        unternehmer.setUsername("UnDAOUname");
        unternehmer.setFirmenname("Firmenname");


        UnternehmerDAO.getInstance().registerUnternehmer(unternehmer, "password");
        UnternehmerDAO.getInstance().setAdresse(unternehmer, address);

        return unternehmer;

    }

    @Test
    public void getInstance() {
        assertNotNull(UnternehmerErstelltStellenAnzeigeDAO.getInstance());
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void deleteUnternehmerErstelltStellenAnzeigeTest() throws DatabaseException {

        createUnternehmerWithAddress();

       Unternehmer unternehmer = UnternehmerDAO.getInstance().fetchUser("mail8.@TestStellenanzeige");
        StellenAnzeige stellenAnzeige1 = new StellenAnzeige();
        stellenAnzeige1.setTitel("Testtitel1");


        UnternehmerErstelltStellenAnzeigeDAO.getInstance().persistUnternehmerErstelltStellenanzeige(unternehmer, stellenAnzeige1);
        int expectedIdStellenAnzeige1 = StellenAnzeigeDAO.getInstance().getMaxStellenAnzeigeId();


        StellenAnzeige got = UnternehmerErstelltStellenAnzeigeDAO.getInstance().fetchStellenanzeigeForUnternehmer(unternehmer).get(0);

        assertEquals(expectedIdStellenAnzeige1, got.getId());

        StellenAnzeigeDAO.getInstance().deleteStellenAnzeigeById(expectedIdStellenAnzeige1);
        UserDAO.getInstance().removeUser(unternehmer);
        got = UnternehmerErstelltStellenAnzeigeDAO.getInstance().fetchStellenanzeigeForUnternehmer(unternehmer).get(0);

    }


    @Test
    public void persistUnternehmerErstelltStellenanzeigeTest() throws DatabaseException {

        createUnternehmerWithAddress();

        Unternehmer unternehmer = UnternehmerDAO.getInstance().fetchUser("mail8.@TestStellenanzeige");

        StellenAnzeige stellenAnzeige1 = new StellenAnzeige();
        stellenAnzeige1.setTitel("Testtitel1");


        UnternehmerErstelltStellenAnzeigeDAO.getInstance().persistUnternehmerErstelltStellenanzeige(unternehmer, stellenAnzeige1);
        int expectedIdStellenAnzeige1 = StellenAnzeigeDAO.getInstance().getMaxStellenAnzeigeId();

        StellenAnzeige got = (StellenAnzeige) UnternehmerErstelltStellenAnzeigeDAO.getInstance().fetchStellenanzeigeForUnternehmer(unternehmer).get(0);

        assertEquals(expectedIdStellenAnzeige1, got.getId());

        StellenAnzeigeDAO.getInstance().deleteStellenAnzeigeById(expectedIdStellenAnzeige1);
        UserDAO.getInstance().removeUser(unternehmer);

    }


    @Test
    public void fetchStellenanzeigeForUnternehmerTest() throws DatabaseException {

        createUnternehmerWithAddress();


        Unternehmer unternehmer = UnternehmerDAO.getInstance().fetchUser("mail8.@TestStellenanzeige");

        StellenAnzeige stellenAnzeige1 = new StellenAnzeige();
        stellenAnzeige1.setTitel("Testtitel1");

        StellenAnzeige stellenAnzeige2 = new StellenAnzeige();
        stellenAnzeige2.setTitel("Testtitel2");


        UnternehmerErstelltStellenAnzeigeDAO.getInstance().persistUnternehmerErstelltStellenanzeige(unternehmer, stellenAnzeige1);
        int expectedIdStellenAnzeige1 = StellenAnzeigeDAO.getInstance().getMaxStellenAnzeigeId();

        UnternehmerErstelltStellenAnzeigeDAO.getInstance().persistUnternehmerErstelltStellenanzeige(unternehmer, stellenAnzeige2);
        int expectedIdStellenAnzeige2 = StellenAnzeigeDAO.getInstance().getMaxStellenAnzeigeId();


        List<StellenAnzeige> stellenAnzeigeList = UnternehmerErstelltStellenAnzeigeDAO.getInstance().fetchStellenanzeigeForUnternehmer(unternehmer);
        stellenAnzeigeList.sort(new StellenAnzeigenComparator());


        StellenAnzeigeDAO.getInstance().deleteStellenAnzeigeById(expectedIdStellenAnzeige1);
        StellenAnzeigeDAO.getInstance().deleteStellenAnzeigeById(expectedIdStellenAnzeige2);

        UserDAO.getInstance().removeUser(unternehmer);


        assertEquals(2, stellenAnzeigeList.size());
        assertEquals(expectedIdStellenAnzeige1, stellenAnzeigeList.get(0).getId());
        assertEquals(expectedIdStellenAnzeige2, stellenAnzeigeList.get(1).getId());
    }


    // Sort-Methode Nur für Testzwecke

    public class StellenAnzeigenComparator implements Comparator<StellenAnzeige> {
        @Override
        public int compare(StellenAnzeige o1, StellenAnzeige o2) {
            return o1.getId() - o2.getId();
        }
    }


}
