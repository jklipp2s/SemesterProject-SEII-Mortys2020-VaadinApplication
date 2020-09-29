package org.mortys.gui.windows;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.mortys.model.objects.dto.StellenAnzeige;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class StellenAnzeigeSelectWindow extends Window {
    Logger logger = Logger.getLogger(StellenAnzeigeSelectWindow.class.getName());
   public StellenAnzeigeSelectWindow(StellenAnzeige stellenAnzeige) {
       super(" Stellenanzeige");
       setIcon(VaadinIcons.WORKPLACE);
       center();
        setResizable(false);
        VerticalLayout overLayout = new VerticalLayout();




        Label firma = new Label("Firma: " + stellenAnzeige.getUnternehmen());
        Label ort = new Label ("Ort: " + stellenAnzeige.getOrt());
        Label titel = new Label("<b>" + stellenAnzeige.getTitel() + "</b><br>" + stellenAnzeige.getBeschreibung(), ContentMode.HTML);


       SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
       Date regDate = null;
       try {
           regDate = parser.parse(stellenAnzeige.getErstellungsDatum().toString());
       } catch (ParseException e) {
           logger.log(null,"error",e);
       }
       SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
       String regDateFormatted = formatter.format(regDate);



       Label registriert = new Label("registriert am " + regDateFormatted);

       Label calendar = new Label();
       calendar.setIcon(VaadinIcons.CALENDAR);
       Label fertigkeiten = new Label("Diese Fähigkeiten sollten Sie mitbringen: " + stellenAnzeige.getSoftskills());
       Label status = new Label("Status: " +stellenAnzeige.getStatus());

        overLayout.addComponents(registriert, firma, ort, titel, status, fertigkeiten);


        this.setContent(overLayout);


    }


}
