package org.mortys.model.objects.dto;

import java.time.LocalDate;

public abstract class User {
    private String anrede;
    private LocalDate geburtsdatum;
    private String username;
    private String vorname;
    private String nachname;
    private Address address;
    private String email;
    private String telefon;
    private LocalDate registrationDate;
    private String status;
    private String avatar;
    private String password;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAnrede() { return anrede; }

    public void setAnrede(String anrede) { this.anrede = anrede; }

    public LocalDate getGeburtsdatum() { return geburtsdatum; }

    public void setGeburtsdatum(LocalDate geburtsdatum) { this.geburtsdatum = geburtsdatum; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getVorname() { return vorname; }

    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }

    public void setNachname(String nachname) { this.nachname = nachname; }

    public Address getAddress() { return address; }

    public void setAddress(Address address) { this.address = address; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }

    public void setTelefon(String telefon) { this.telefon = telefon; }

    public LocalDate getRegistrationDate() { return registrationDate; }

    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getOrt() {
        return getAddress().getPlz() + " " + getAddress().getOrt();
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
