package org.mortys.model.dao;

import org.junit.Test;
import org.mortys.model.objects.dto.Address;
import org.mortys.process.control.exception.DatabaseException;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AddressDAOTest {

    private Address control;
    private Address address = new Address();

    public void setUp() {
        control = new Address();
        control.setStreet("Sonnenallee");
        control.setHausnummer(28);
        control.setPlz("86010");
        control.setOrt("Augsburg");
    }

    @Test
    public void getInstance() {
        assertNotNull(AddressDAO.getInstance());
    }

    @Test
    public void fetchAllAddress () throws DatabaseException {

        List<Address> list = AddressDAO.getInstance().fetchAllAddress();
        for(Address f: list) {
            System.out.println("Address: ");
            System.out.println("Straße: " + f.getStreet());
            System.out.println("Hausnummer: " + f.getHausnummer());
            System.out.println("PLZ: " + f.getPlz());
            System.out.println("Ort: " + f.getOrt());
            System.out.println();
        }

    }

    @Test
    public void persistAddress() {

        address.setStreet("Test StraßeNeuNeu");
        address.setHausnummer(126);
        address.setPlz("53757");
        address.setOrt("Sankt Augustin");

        AddressDAO.getInstance().persistAddress(address);

    }


    @Test
    public void fetchAddressTest() {

       setUp();
       Address result = new Address();
        try {
            result = AddressDAO.getInstance().fetchAddress(20);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(control.getPlz(), result.getPlz());
        assertEquals(control.getOrt(), result.getOrt());
        assertEquals(control.getStreet(), result.getStreet());
        assertEquals(control.getHausnummer(), result.getHausnummer());

    }

}