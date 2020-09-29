package org.mortys.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ApplicationBody extends VerticalLayout {

    Label titelFieldLabel = null, beschreibungfieldLabel = null, statusFieldLabel = null;
     TextField titelFieldBasic, beschreibungFieldbasic, statusFieldBasic;
    TextFieldWithIcon titelField, statusField;
    TextArea beschreibungField;
    public ApplicationBody() {
        this.setStyleName("application_labelContainer");

        VerticalLayout body = this.setUp();
        this.addComponent(body);
    }

    public VerticalLayout setUp(){
        VerticalLayout bodyTwo = new VerticalLayout();
        bodyTwo.addStyleName("application_container");

        titelFieldLabel = new Label();
        titelFieldBasic = new TextField();
        titelFieldLabel.setIcon(VaadinIcons.USER_CARD);
        titelField = new TextFieldWithIcon(titelFieldBasic,"Titel",titelFieldLabel);


        beschreibungField = new TextArea();
        beschreibungField.setPlaceholder("Beschreibung");

        statusFieldLabel = new Label();
        statusFieldBasic = new TextField();
        statusFieldLabel.setIcon(VaadinIcons.USER_CARD);
        statusField = new TextFieldWithIcon(statusFieldBasic,"Status",statusFieldLabel);

        bodyTwo.addComponent(titelField);
        bodyTwo.addComponent(beschreibungField);
        bodyTwo.addComponent(statusField);

        return bodyTwo;
    }

    public TextField getTitelFieldBasic(){  return titelFieldBasic;  }
    public TextArea getBeschreibungField() {  return beschreibungField;  }
    public TextField getStatusFieldBasic() {  return statusFieldBasic;  }
}
