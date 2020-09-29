package org.mortys.model.dto;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mortys.model.objects.dto.Address;
import org.mortys.model.objects.dto.Student;

import java.time.LocalDate;

public class StudentTest {

    private String matrikelnummer = "998877";
    private String beschäftigung = "";
    private String anrede = "Herr";
    private LocalDate geburtsdatum = LocalDate.of(2000,6,24);
    private String username = "testa2s";
    private String vorname = "test";
    private String nachname = "test";
    private Address address = new Address();
    private String email = "testa@test.de";
    private String telefon = "02241 112233";
    private LocalDate registrationDate = LocalDate.now();
    private String status = "";
    private String avatar = "";

    private Student student;

    @Before
    public void setUp () {

        student = new Student();
        student.setMatrikelnr("998877");
        student.setBeschäftigung("");
        student.setAnrede("Herr");
        student.setGeburtsdatum(geburtsdatum);
        student.setEmail("testa@test.de");
        student.setUsername("testa2s");
        student.setVorname("test");
        student.setNachname("test");
        student.setAddress(address);
        student.setTelefon("02241 112233");
        student.setStatus("");
        student.setAvatar("");

        matrikelnummer = "998877";
        beschäftigung = "";
        anrede = "Herr";
        this.geburtsdatum = geburtsdatum;
        email = "testa@test.de";
        username = "testa2s";
        vorname = "test";
        nachname = "test";
        this.address = address;
        telefon = "02241 112233";
        status = "";
        avatar = "";

    }

    @Test
    public void testGetter () {

        Assert.assertEquals(username, student.getUsername());
        Assert.assertEquals(email, student.getEmail());
        Assert.assertEquals(address, student.getAddress());
        Assert.assertEquals(matrikelnummer, student.getMatrikelnr());
        Assert.assertEquals(vorname, student.getVorname());
        Assert.assertEquals(nachname, student.getVorname());
        Assert.assertEquals(beschäftigung, student.getBeschäftigung());
        Assert.assertEquals(telefon, student.getTelefon());
        Assert.assertEquals(status, student.getStatus());
        Assert.assertEquals(avatar, student.getAvatar());
        Assert.assertEquals(geburtsdatum, student.getGeburtsdatum());
        Assert.assertEquals(anrede, student.getAnrede());

    }


}