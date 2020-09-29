package org.mortys.gui.windows;

import com.vaadin.ui.*;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.LoginControl;
import org.mortys.process.control.UserControl;
import org.mortys.services.util.Roles;

public class DeleteUserConfirmationWindow extends Window {

    public DeleteUserConfirmationWindow(String text){

        super("Profil löschen");
        center();

        VerticalLayout content = new VerticalLayout();
        HorizontalLayout layout = new HorizontalLayout();

        content.addComponent(new Label(text));
        layout.setMargin(true);
        setContent(content);

        Button deleteButton = new Button("JA");
        deleteButton.setStyleName("landing_registerButton");
        deleteButton.setId("jaButton");

        User user = (User)UI.getCurrent().getSession().getAttribute(Roles.CURRENT_USER);
        deleteButton.addClickListener(e -> {
            if(user == null){
                Notification.show("Fehler", "Kein Benutzer eingeloggt!", Notification.Type.ERROR_MESSAGE);
            } else {
                UserControl.getInstance().deleteUser(user);
                UI.getCurrent().addWindow(new UserDeletedWindow("Profil wurde gelöscht"));
                close();
                LoginControl.logoutUser();
            }
        });
        Button closeButton = new Button("NEIN");
        closeButton.setStyleName("landing_loginButton");
        closeButton.setId("neinButton");

        closeButton.addClickListener( e -> { close(); } );

        layout.addComponent(deleteButton);
        layout.addComponent(closeButton);


        layout.setComponentAlignment(deleteButton, Alignment.TOP_LEFT);
        layout.setComponentAlignment(closeButton, Alignment.TOP_RIGHT);

        content.addComponent(layout);
    }
}