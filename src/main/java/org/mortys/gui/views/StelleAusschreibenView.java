package org.mortys.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.mortys.gui.components.ApplicationBody;
import org.mortys.gui.components.ApplicationList;
import org.mortys.gui.components.Footer;
import org.mortys.gui.components.Header;
import org.mortys.model.dao.UnternehmerErstelltStellenAnzeigeDAO;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;
import org.mortys.services.util.Roles;
import org.mortys.services.util.UserFetcher;
import org.mortys.services.util.Views;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StelleAusschreibenView extends VerticalLayout implements View {
    Student student = null;
    Unternehmer unternehmer = null;
    User user = null;
    StellenAnzeige stellenAnzeige = null;
    String mode = "list";
    ApplicationBody body;
    Header header = new Header(true);
    Footer footer = new Footer();
    ApplicationList list;

    public StelleAusschreibenView(){
        this.user = UserFetcher.getUser();
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        this.user = UserFetcher.getUser();

        if(user == null){
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        } else {
            this.setUp();
        }

    }

    public void setUp() {

        //Layout setup - START
        addComponent(header);
        this.setStyleName("layout_profile");

        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setStyleName("profile_buttonBar");

        //Layout setup - END

        // LABELS - START
        list = new ApplicationList((Unternehmer)user);
        //LABELS - END

        // Button Group - START
        Button newApplicationButton = new Button("Stelle ausschreiben");
        newApplicationButton.setStyleName("profile_editButton");

        Button newSendApplicationOfferButton = new Button("Stelle ausschreiben");
        newSendApplicationOfferButton.setStyleName("profile_saveButton");

        buttonBar.addComponent(newApplicationButton);
        // Button Group - END

        this.addComponent(list);
        this.addComponent(buttonBar);
        this.addComponent(footer);

        //Button Logic

        newSendApplicationOfferButton.addClickListener(e -> {

            this.user = UserFetcher.getUser();

            if(body.getBeschreibungField().getValue() == "" || body.getStatusFieldBasic().getValue() == "" || body.getTitelFieldBasic().getValue() == "" ){
                Notification.show("Error","Bitte alle Felder ausfüllen",Notification.Type.ERROR_MESSAGE);
            }
            else {
                stellenAnzeige = new StellenAnzeige();
                stellenAnzeige.setStatus(body.getStatusFieldBasic().getValue());
                stellenAnzeige.setBeschreibung(body.getBeschreibungField().getValue());

                stellenAnzeige.setTitel(body.getTitelFieldBasic().getValue());

                persistUnternehmerErstelltStellenanzeige((Unternehmer)user, stellenAnzeige);

                this.removeComponent(body);
                this.removeComponent(buttonBar);
                this.removeComponent(footer);
                buttonBar.removeComponent(newSendApplicationOfferButton);
                newApplicationButton.setStyleName("profile_editButton");
                newApplicationButton.setCaption("Stelle ausschreiben");
                list = new ApplicationList((Unternehmer) user);

                this.addComponent(list);
                this.addComponent(buttonBar);
                this.addComponent(footer);
                mode = "list";
            }
        });

        newApplicationButton.addClickListener(e -> {
            this.user = UserFetcher.getUser();

            if(mode.equals("list")){
                this.removeComponent(list);
                newApplicationButton.setCaption("Cancel");
                newApplicationButton.setStyleName("profile_cancelButton");
                this.removeComponent(buttonBar);
                this.removeComponent(footer);
                body = new ApplicationBody();
                this.addComponent(body);
                buttonBar.addComponent(newSendApplicationOfferButton);
                this.addComponent(buttonBar);
                this.addComponent(footer);
                mode = "";
            } else {
                mode = "list";
                this.removeComponent(footer);
                this.removeComponent(body);
                list = new ApplicationList((Unternehmer)user);
                newApplicationButton.setStyleName("profile_editButton");
                newApplicationButton.setCaption("Stelle ausschreiben");
                buttonBar.removeComponent(newSendApplicationOfferButton);
                this.addComponent(list);
                this.addComponent(buttonBar);
                this.addComponent(footer);
            }
        });
    }
    private void persistUnternehmerErstelltStellenanzeige(Unternehmer unternehmer,StellenAnzeige stellenAnzeige){
        try {
            UnternehmerErstelltStellenAnzeigeDAO.getInstance().persistUnternehmerErstelltStellenanzeige(unternehmer,stellenAnzeige);
        } catch (DatabaseException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
