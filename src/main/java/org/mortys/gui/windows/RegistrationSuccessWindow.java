package org.mortys.gui.windows;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.mortys.services.util.Views;

public class RegistrationSuccessWindow extends Window {

    public RegistrationSuccessWindow(String user){
        this.setStyleName("RegistrationSuccessWindow");
        this.setResizable(false);
        this.setClosable(false);
        this.setCaption(" Registrierung erfolgreich");
        this.setIcon(VaadinIcons.INFO_CIRCLE_O
        );
        this.center();
        VerticalLayout verticalLayout = new VerticalLayout();


        Label success = new Label("Willkommen bei Mortys " + user + "." );

        verticalLayout.addComponent(success);

        Button ok =new Button("ok");
        ok.setStyleName("landing_registerButton");
        ok.setId("okButton");
        ok.setWidth("120px");



        verticalLayout.addComponent(ok);
        verticalLayout.setComponentAlignment(ok, Alignment.MIDDLE_CENTER);
        this.setContent(verticalLayout);

        //Logic

        ok.addClickListener(clickEvent -> {
            this.close();
            UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
        });




    }

}
