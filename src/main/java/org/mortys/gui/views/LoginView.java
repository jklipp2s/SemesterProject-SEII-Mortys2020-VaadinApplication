package org.mortys.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.mortys.gui.components.Footer;
import org.mortys.gui.components.Header;
import org.mortys.gui.components.TextFieldWithIcon;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.LoginControl;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.NoSuchUserOrPassword;
import org.mortys.services.util.Roles;
import org.mortys.services.util.Views;

public class LoginView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        User user = (User) UI.getCurrent().getSession().getAttribute(Roles.CURRENT_USER);

        if(user == null) {
            this.setUp();
        } else {
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        };

    }

    public LoginView(){};

    public void setUp() {
        addComponent(new Header(false));

        // this is a unique build of the login view. It should not be excluded within its own file!
        this.setStyleName("layout_loginRegister");
        HorizontalLayout body = new HorizontalLayout();
        body.addStyleName("login_container");
        // this is a unique build of the login view. It should not be excluded within its own file!

        VerticalLayout leftPage = new VerticalLayout();
        leftPage.setStyleName("login_leftPane");
        Label pageLogo = new Label();
        pageLogo.setStyleName("login_pageLogo");
        leftPage.addComponent(pageLogo);
        Label title = new Label("Login");
        title.setStyleName("login_title");
        leftPage.addComponent(title);
        // this is a unique build of the login view. It should not be excluded within its own file!

        Label icon = new Label("");
        icon.setIcon(VaadinIcons.SIGN_IN);
        icon.setStyleName("login_icon");
        leftPage.addComponent(icon);


        VerticalLayout rightPane = new VerticalLayout();
        rightPane.addStyleName("login_rightPane");

        TextField emailField = new TextField();
        emailField.setId("emailField");
        Label emailFieldLabel = new Label();
        emailFieldLabel.setIcon(VaadinIcons.USER);
        TextFieldWithIcon emailTextField = new TextFieldWithIcon(emailField, "Email", emailFieldLabel);

        PasswordField passwortField = new PasswordField("");
        passwortField.setId("passwordField");
        Label passwortFieldLabel = new Label();
        passwortFieldLabel.setIcon(VaadinIcons.PASSWORD);
        TextFieldWithIcon passwortTextField = new TextFieldWithIcon(passwortField, "Passwort", passwortFieldLabel);

        Button passwordVergessenLink = new Button("Passwort vergessen?");
        passwordVergessenLink.setStyleName("login_passwortVergessen");

        Button loginButton = new Button("Login");
        loginButton.setStyleName("login_button");
        loginButton.setId("loginButton");
        rightPane.addComponent(emailTextField);
        rightPane.addComponent(passwortTextField);
        rightPane.addComponent(passwordVergessenLink);
        rightPane.addComponent(loginButton);

        Button sendCodeButton = new Button("Send Code");
        sendCodeButton.setStyleName("login_sendCode");

        Button checkCodeButton = new Button("Check code");
        checkCodeButton.setStyleName("login_checkCode");




        body.addComponent(leftPage);
        body.addComponent(rightPane);

        this.addComponent(body);
        this.addComponent(new Footer());

        //Component Logic

        loginButton.addClickListener(e ->{
            String email = emailField.getValue();
            String password = passwortField.getValue();

            try{
                LoginControl.checkAuthentication(email,password);
            }catch(NoSuchUserOrPassword ex1){
                Notification.show("Error", "Wrong Email or Password. Please try again.", Notification.Type.ERROR_MESSAGE);
                emailField.setValue("");
                passwortField.setValue("");            }
            catch (DatabaseException ex2) {
                Notification.show("DB-Fehler", ex2.getReason(), Notification.Type.ERROR_MESSAGE);
                emailField.setValue("");
                passwortField.setValue("");        }
        });

        passwordVergessenLink.addClickListener(e->{
           //this.removeComponent(body);
          // body.removeComponent(rightPane);
           rightPane.removeComponent(passwortTextField);
           rightPane.removeComponent(loginButton);
           rightPane.removeComponent(passwordVergessenLink);
           rightPane.addComponent(sendCodeButton);
          // body.addComponent(rightPane);
          // this.addComponent(body);
        });

        sendCodeButton.addClickListener(e->{
           rightPane.removeComponent(sendCodeButton);
        });

    }

}
