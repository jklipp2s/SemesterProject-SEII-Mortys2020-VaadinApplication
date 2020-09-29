package org.mortys.model.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortys.model.objects.dto.UserDTO;


public class UserDTOTest {

    private UserDTO Tester;
    private String username;
    private String email;
    String password;
    private String matrnr;
    boolean isUnternehmer;

    @Before
    public void setUp() {
        Tester = new UserDTO();
        Tester.setUsername("Saidom");
        Tester.setEmail("UweKlug@dayrep.com");
        Tester.setPassword("aeyaiGa8sae");
        Tester.setMatrnr("9030202023");
        Tester.setUnternehmer(false);

        username = "Saidom";
        email = "UweKlug@dayrep.com";
        password = "aeyaiGa8sae";
        matrnr = "9030202023";
        isUnternehmer = false;

    }
    @Test
    public void testGetter() {
        Assert.assertEquals(username, Tester.getUsername());
        Assert.assertEquals(email, Tester.getEmail());
        Assert.assertEquals(password, Tester.getPassword());
        Assert.assertEquals(matrnr, Tester.getMatrnr());
        Assert.assertEquals(isUnternehmer, Tester.isUnternehmer());

    }

    @Test
    public void testSetter() {
        Tester.setUsername("Frompleat62");
        Tester.setEmail("GabrieleBusch@teleworm.us");
        Tester.setPassword("tohH6oong");
        Tester.setMatrnr("9030202002");
        Tester.setUnternehmer(false);

        Assert.assertEquals("Frompleat62", Tester.getUsername());
        Assert.assertEquals("GabrieleBusch@teleworm.us", Tester.getEmail());
        Assert.assertEquals("tohH6oong", Tester.getPassword());
        Assert.assertEquals("9030202002", Tester.getMatrnr());
        Assert.assertEquals(false, Tester.isUnternehmer());

    }

}