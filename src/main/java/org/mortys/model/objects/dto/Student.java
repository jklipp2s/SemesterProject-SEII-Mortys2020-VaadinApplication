package org.mortys.model.objects.dto;

import org.mortys.model.dao.StudentBewirbtStellenAnzeigeDAO;
import org.mortys.model.dao.StudentHatFertigkeitDAO;
import org.mortys.process.control.exception.DatabaseException;

import java.util.List;

public class Student extends User {
    private String matrikelnr;
    private String beschäftigung;
    private List<Fertigkeit> fertigkeiten;
    private List<StellenAnzeige> abgesendeteBewerbungen;

    public String getMatrikelnr() {
        return matrikelnr;
    }

    public void setMatrikelnr(String matrikelnr) {
        this.matrikelnr = matrikelnr;
    }


    public String getBeschäftigung() { return beschäftigung; }

    public void setBeschäftigung(String beschäftigung) { this.beschäftigung = beschäftigung; }

    public List<Fertigkeit> getFertigkeiten() {

        try {
        if (fertigkeiten == null)    fertigkeiten = StudentHatFertigkeitDAO.getInstance().fetchFertigkeitforStudent(matrikelnr);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        return fertigkeiten;
    }



    public String getSoftskills() {
        String softskills ="";
        List<Fertigkeit> fertigkeitList = getFertigkeiten();
        for (int i = 0; i<fertigkeitList.size(); i++ ){
            softskills += fertigkeitList.get(i).getSoftskill();
            if(i<fertigkeitList.size()-1){
                softskills += ", ";
            }
        }

        return softskills;
    }


    public void addFertigkeit(Fertigkeit fertigkeit) throws DatabaseException {

        StudentHatFertigkeitDAO.getInstance().persistStudentHatFertigkeit(this, fertigkeit);

        fertigkeiten.add(fertigkeit);
    }

    public List<StellenAnzeige> getAbgesendeteBewerbungen() throws DatabaseException {

        abgesendeteBewerbungen = StudentBewirbtStellenAnzeigeDAO.getInstance().fetchStellenAnzeigeStudent(this);

        return abgesendeteBewerbungen;
    }

    public void addAbgesendeteBewerbung(StellenAnzeige stellenAnzeige) throws DatabaseException {

        StudentBewirbtStellenAnzeigeDAO.getInstance().persistStudentBewirbtStellenanzeige(this,stellenAnzeige);
        abgesendeteBewerbungen.add(stellenAnzeige);
    }
}
