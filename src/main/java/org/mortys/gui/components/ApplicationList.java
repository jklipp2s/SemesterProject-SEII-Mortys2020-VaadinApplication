package org.mortys.gui.components;

import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.mortys.model.dao.StellenAnzeigeDAO;
import org.mortys.model.dao.UnternehmerErstelltStellenAnzeigeDAO;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationList extends VerticalLayout {

    List<StellenAnzeige> list = new ArrayList<>();
    Label titelFieldLabel = null, beschreibungfieldLabel = null, statusFieldLabel = null;
    TextField titelFieldBasic, beschreibungFieldbasic, statusFieldBasic;
    TextFieldWithIcon titelField, beschreibungField, statusField;
    Unternehmer user;

    public ApplicationList(Unternehmer user) {
        this.user = user;
        this.setStyleName("application_labelContainer");

        if(!fetchBewerbungen().isEmpty()) {
            VerticalLayout body = this.setUp();
            this.addComponent(body);
        }
    }

    public List<StellenAnzeige> fetchBewerbungen(){

        try {
            list = UnternehmerErstelltStellenAnzeigeDAO.getInstance().fetchStellenanzeigeForUnternehmer(user);
        } catch (DatabaseException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return list;
    }

    private VerticalLayout setUp() {
        List<StellenAnzeige> allStellen = fetchBewerbungen();

        Grid<StellenAnzeige> grid = new Grid<>();
        grid.setItems(allStellen);
        grid.addColumn(StellenAnzeige::getTitel).setCaption("Titel");
        grid.addColumn(StellenAnzeige::getStatus).setCaption("Status");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.getDefaultHeaderRow().setStyleName("mytableheader");
        grid.setStyleName("mytable");
        grid.setHeightMode(HeightMode.ROW);
        grid.setHeightByRows(allStellen.size());
        grid.setWidthFull();




        grid.addColumn(stellenAnzeige -> "Delete", new ButtonRenderer<StellenAnzeige>(clickEvent->{
            try {
                StellenAnzeigeDAO.getInstance().deleteStellenAnzeigeById(clickEvent.getItem().getId());
            } catch (DatabaseException ex) {
                Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
            }

            allStellen.remove(clickEvent.getItem());
            grid.setItems(allStellen);
        }));

        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.addSelectionListener(event -> {
            Set<StellenAnzeige> selected = event.getAllSelectedItems();
            Notification.show(selected.size() + " stellen ausgewählt");
        });
        VerticalLayout bodyTwo = new VerticalLayout();
        bodyTwo.addStyleName("application_container");

        bodyTwo.addComponent(grid);

        return bodyTwo;
    }
}
