package org.mortys.model.objects.dto;

public class Address {
    private String plz;
    private String ort;
    private String street;
    private int hausnummer;




    public String getPlz() { return plz; }

    public void setPlz(String plz) { this.plz = plz; }

    public String getOrt() { return ort; }

    public void setOrt(String ort) { this.ort = ort; }

    public String getStreet() { return street; }

    public void setStreet(String street) { this.street = street; }

    public int getHausnummer() { return hausnummer; }

    public void setHausnummer(int hausnummer) { this.hausnummer = hausnummer; }
}
