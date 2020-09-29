package org.mortys.model.dao;


import org.junit.Test;
import org.mortys.model.objects.dto.Address;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.process.control.exception.DatabaseException;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;

public class UnternehmerDAOTest {

    private Unternehmer unternehmer = new Unternehmer();
    private UserDAO user = new UserDAO();
    LocalDate geb = LocalDate.of(2000, 1, 20);

    @Test
    public void getInstance() {

        assertNotNull(UnternehmerDAO.getInstance());

    }

    @Test
    public void fetchUserTest () throws DatabaseException {

        UnternehmerDAO.getInstance().fetchUser("DanielHertzog@teleworm.us");

    }

    @Test
    public void persistUser() throws DatabaseException {

        Address address = new Address();
        address.setStreet("Test StraßeNeuNeu");
        address.setHausnummer(126);
        address.setPlz("53757");
        address.setOrt("Sankt Augustin");

        unternehmer.setAnrede("Herr");
        unternehmer.setAddress(address);
        unternehmer.setGeburtsdatum(geb);
        unternehmer.setEmail("UnDAO1@test.de");
        unternehmer.setUsername("UnDAOUname");
        unternehmer.setVorname("UnDAOvName");
        unternehmer.setNachname("UnDAOnName");
        unternehmer.setTelefon("022933");
        unternehmer.setStatus("StatusDAO");
        unternehmer.setUstId("4545345");
        unternehmer.setIban("DE 67656568796554");
        unternehmer.setFirmenname("Firmenname");

        UnternehmerDAO.getInstance().persistUser(unternehmer, address, "regUNtest" );
        UserDAO.getInstance().removeUser(unternehmer);

    }

    @Test
    public void setFirmenname() throws DatabaseException {

        unternehmer.setEmail("testun@test.de");
        UnternehmerDAO.getInstance().setFirmenname(unternehmer, "TestUnternehmen");

    }

    @Test
    public void setUstid() throws DatabaseException {

        unternehmer.setEmail("testun@test.de");
        UnternehmerDAO.getInstance().setUstid(unternehmer, "12345678");

    }

    @Test
    public void setIban() throws DatabaseException {

        unternehmer.setEmail("testun@test.de");
        UnternehmerDAO.getInstance().setIban(unternehmer, "DE 726336363");

    }

}