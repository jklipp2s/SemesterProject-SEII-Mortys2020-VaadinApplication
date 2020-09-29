package org.mortys.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.mortys.gui.components.Footer;
import org.mortys.gui.components.Header;
import org.mortys.services.util.Views;

public class MainView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.setUp();
    }

    public void setUp() {
        //STYLE
        addComponent(new Header(false));
        this.setStyleName("layout_main");
        this.setResponsive(false);
        VerticalLayout vertical = new VerticalLayout();
        Button registerButton = new Button("Registrieren");
        registerButton.setId("registerButton");
        Button loginButton = new Button("Login");
        loginButton.setId("loginButton");
        Label welcomeText = new Label("This is the Landingpage for the collaboration website Mortys from the HBRS");
        Image logo = new Image(null, new ThemeResource("img/logo_Mortys.png"));
        logo.setWidth( 600 ,Unit.PIXELS);


        HorizontalLayout inline = new HorizontalLayout();
        inline.addComponent(logo);


        VerticalLayout buttons= new VerticalLayout();
        buttons.addComponent(registerButton);
        buttons.addComponent(loginButton);
        registerButton.setStyleName("landing_registerButton");
        loginButton.setStyleName("landing_loginButton");
        buttons.setStyleName("landing_buttonBox");
        inline.addComponent(buttons);
        inline.setStyleName("landing_logoButtonBox");

        vertical.addComponent(inline);
        vertical.addComponent(welcomeText);
        addComponent(vertical);
        vertical.setStyleName("landing_main_Container");

        welcomeText.addStyleName("landing_welcomeText");

        this.addComponent(new Footer());

        //LOGIC

        registerButton.addClickListener(e -> {
            UI.getCurrent().getNavigator().navigateTo(Views.REGISTER);
        });


        loginButton.addClickListener(e -> {
            UI.getCurrent().getNavigator().navigateTo(Views.LOGIN);
        });

    }

}
