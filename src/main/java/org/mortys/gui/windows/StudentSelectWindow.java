package org.mortys.gui.windows;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.mortys.model.dao.StudentHatFertigkeitDAO;
import org.mortys.model.objects.dto.Fertigkeit;
import org.mortys.model.objects.dto.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentSelectWindow extends Window {

   public StudentSelectWindow(Student student) {
       super(" Student");
       setIcon(VaadinIcons.ACADEMY_CAP);
       center();
        setResizable(false);
        VerticalLayout overLayout = new VerticalLayout();

        Label name = new Label("Name: " + student.getNachname() + " " + student.getVorname());
        Label email = new Label ("Email: " + student.getEmail());
        Label telefon = new Label("Telefon: " + student.getTelefon());
        Label beschaeftigung = new Label("Beschäftigung: " + student.getBeschäftigung());
        Grid<Fertigkeit> fertigkeiten = new Grid<>();
        fertigkeiten.addColumn(Fertigkeit::getSoftskill).setCaption("Softskill");
        fertigkeiten.addColumn(Fertigkeit::getBeschreibung).setCaption("Beschreibung");
        fertigkeiten.setItems(student.getFertigkeiten());
        fertigkeiten.setHeightByRows(student.getFertigkeiten().size() > 0 ? student.getFertigkeiten().size() : 1);
        Label address = new Label("Adresse: <br>" + student.getAddress().getPlz() + " " + student.getAddress().getOrt() + "<br>" +
        student.getAddress().getStreet() + " " + student.getAddress().getHausnummer(), ContentMode.HTML);

       SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
       Date birthday = null;
       Date regDate = null;
       try {
           birthday = parser.parse(student.getGeburtsdatum().toString());
           regDate = parser.parse(student.getRegistrationDate().toString());
       } catch (ParseException e) {
           Logger.getLogger(StudentHatFertigkeitDAO.class.getName()).log(Level.SEVERE, null, e);
       }
       SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
       String birthdayFormatted = formatter.format(birthday);
       String regDateFormatted = formatter.format(regDate);





       Label registriert = new Label("erstellt am " + regDateFormatted);

       Label calendar = new Label();
       calendar.setIcon(VaadinIcons.CALENDAR);
       HorizontalLayout geburtstag = new HorizontalLayout(calendar, new Label("Geburtstag: " + birthdayFormatted));
        Label matrikelnr = new Label("Matrikelnummer: " +student.getMatrikelnr());

      Label age = new Label("Alter: " +calculateAge(student.getGeburtsdatum(), LocalDate.now()));


        overLayout.addComponents(registriert, name, age, email, telefon, beschaeftigung, fertigkeiten, address,  geburtstag, matrikelnr);


        this.setContent(overLayout);


    }

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }


}
