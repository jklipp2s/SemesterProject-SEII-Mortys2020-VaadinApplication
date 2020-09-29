package org.mortys.model.objects.dto;

import org.mortys.model.dao.StellenAnzeigeFordertFertigkeitDAO;
import org.mortys.process.control.exception.DatabaseException;

import java.time.LocalDate;
import java.util.List;

public class StellenAnzeige {
    private int id;
    private String unternehmen;
    private String ort;
    private String titel;
    private String beschreibung;
    private String status;
    private List<Fertigkeit> fertigkeitenList;
    LocalDate erstellungsDatum;


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUnternehmen() { return unternehmen; }

    public void setUnternehmen(String unternehmen) { this.unternehmen = unternehmen; }

    public String getOrt() { return ort; }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getTitel() { return titel; }

    public void setTitel(String titel) { this.titel = titel; }

    public String getBeschreibung() { return beschreibung; }

    public void setBeschreibung(String beschreibung) { this.beschreibung = beschreibung; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public LocalDate getErstellungsDatum() { return erstellungsDatum; }


    public void addFertigkeit(Fertigkeit fertigkeit) {
        try {
            StellenAnzeigeFordertFertigkeitDAO.getInstance().persistStellenAnzeigeFordertFertigkeit(this, fertigkeit);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        this.fertigkeitenList.add(fertigkeit);
    }


    public List<Fertigkeit> getFertigkeiten() {

        try {
            if (fertigkeitenList == null)    fertigkeitenList = StellenAnzeigeFordertFertigkeitDAO.getInstance().fetchFertigkeitforStellenAnzeige(this.id);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return fertigkeitenList;
    }





    public String getSoftskills() {
        String softskills ="";
        List<Fertigkeit> fertigkeitList = getFertigkeiten();
        for (int i = 0; i<fertigkeitList.size(); i++ ){

            String fertigkeit = fertigkeitList.get(i).getSoftskill();

            softskills += softskills.contains(fertigkeit) ? "" : fertigkeit;
            if(i<fertigkeitList.size()-1){
                softskills += ", ";
            }
        }

        return softskills;
    }




    public void setErstellungsDatum(LocalDate erstellungsDatum) { this.erstellungsDatum = erstellungsDatum; }
}
