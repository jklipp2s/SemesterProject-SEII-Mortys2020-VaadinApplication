package org.mortys.model.objects.dto;

import org.mortys.model.dao.UnternehmerErstelltStellenAnzeigeDAO;
import org.mortys.process.control.exception.DatabaseException;

import java.util.List;

public class Unternehmer extends User {
    private int kundennummer;
    private String firmenname;
    private String ustId;
    private String iban;
    private List<StellenAnzeige> stellenAnzeigen;

    public int getKundennummer() { return kundennummer; }

    public void setKundennummer(int kundennummer) { this.kundennummer = kundennummer; }

    public String getFirmenname() { return firmenname; }

    public void setFirmenname(String firmenname) { this.firmenname = firmenname; }

    public String getUstId() { return ustId; }

    public void setUstId(String ustId) { this.ustId = ustId; }

    public String getIban() { return iban; }

    public void setIban(String iban) { this.iban = iban; }

    public List<StellenAnzeige> getStellenAnzeigen() throws DatabaseException {

        stellenAnzeigen = UnternehmerErstelltStellenAnzeigeDAO.getInstance().fetchStellenanzeigeForUnternehmer(this);

        return stellenAnzeigen;
    }

    public void addStellenAnzeige(StellenAnzeige stellenAnzeige) throws DatabaseException {
        UnternehmerErstelltStellenAnzeigeDAO.getInstance().persistUnternehmerErstelltStellenanzeige(this,stellenAnzeige);
        stellenAnzeigen.add(stellenAnzeige);
    }
}
