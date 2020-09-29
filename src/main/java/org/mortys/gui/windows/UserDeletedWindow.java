package org.mortys.gui.windows;

import com.vaadin.ui.*;

public class UserDeletedWindow extends Window {

    public UserDeletedWindow(String text){
        super("Confirmation");
        center();

        VerticalLayout content = new VerticalLayout();
        content.addComponent(new Label(text));
        content.setMargin(true);
        setContent(content);

        Button confirmButton = new Button("OK");
        confirmButton.setStyleName("landing_loginButton");

        confirmButton.addClickListener(e -> { close(); });

        content.addComponent(confirmButton);
        content.setComponentAlignment(confirmButton, Alignment.TOP_LEFT);
    }
}
