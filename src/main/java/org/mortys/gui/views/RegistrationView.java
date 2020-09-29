package org.mortys.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import org.mortys.gui.components.Footer;
import org.mortys.gui.components.Header;
import org.mortys.gui.components.TextFieldWithIcon;
import org.mortys.gui.windows.RegistrationSuccessWindow;
import org.mortys.model.dao.UserDAO;
import org.mortys.model.objects.dto.User;
import org.mortys.model.objects.dto.UserDTO;
import org.mortys.process.control.RegistrationControl;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.RegisterFailException;
import org.mortys.services.util.Roles;

import java.util.regex.Pattern;

public class RegistrationView extends VerticalLayout implements View {

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

        User user = (User) UI.getCurrent().getSession().getAttribute(Roles.CURRENT_USER);

        if (user == null) this.setUp();
    }

    public RegistrationView() {
    }

    ;

    public void setUp() {

        //Style setup

        addComponent(new Header(false));

        this.setStyleName("layout_loginRegister");
        HorizontalLayout body = new HorizontalLayout();
        body.addStyleName("login_container");

        VerticalLayout leftPage = new VerticalLayout();
        leftPage.setStyleName("login_leftPane");
        Label pageLogo = new Label();
        pageLogo.setStyleName("login_pageLogo");
        leftPage.addComponent(pageLogo);
        Label title = new Label("Registration");
        title.setStyleName("registration_title");
        leftPage.addComponent(title);

        Label icon = new Label("");
        icon.setIcon(VaadinIcons.WRENCH);
        icon.setStyleName("register_icon");
        leftPage.addComponent(icon);


        VerticalLayout rightPane = new VerticalLayout();
        rightPane.addStyleName("register_rightPane");

        TextField emailField = new TextField();
        emailField.setId("emailField");
        Label emailFieldLabel = new Label();
        emailFieldLabel.setIcon(VaadinIcons.ENVELOPE_O);
        TextFieldWithIcon email = new TextFieldWithIcon(emailField, "email", emailFieldLabel);


        TextField usernameField = new TextField();
        usernameField.setId("usernameField");
        Label usernameFieldLabel = new Label();
        usernameFieldLabel.setIcon(VaadinIcons.USER);
        TextFieldWithIcon username = new TextFieldWithIcon(usernameField, "username", usernameFieldLabel);


        PasswordField passwordField = new PasswordField();
        passwordField.setId("passwordField");
        Label passwordFieldLabel = new Label();
        passwordFieldLabel.setIcon(VaadinIcons.PASSWORD);
        TextFieldWithIcon password = new TextFieldWithIcon(passwordField, "password", passwordFieldLabel);

        PasswordField validatePasswordField = new PasswordField();
        validatePasswordField.setId("validateField");
        Label validatepasswordFieldLabel = new Label();
        validatepasswordFieldLabel.setIcon(VaadinIcons.PASSWORD);
        TextFieldWithIcon validatePassword = new TextFieldWithIcon(validatePasswordField, " repeat password", validatepasswordFieldLabel);


        CheckBox isStudent = new CheckBox();
        isStudent.setCaption("Student");
        isStudent.setId("studentCheck");
        CheckBox isUnternehmer = new CheckBox();
        isUnternehmer.setCaption("Unternehmer");
        isUnternehmer.setId("unternehmerCheck");
        HorizontalLayout checkBoxWrapper = new HorizontalLayout(isStudent, isUnternehmer);
        checkBoxWrapper.setStyleName("register_rightPane_checkBox");
        checkBoxWrapper.setWidth("250px");
        checkBoxWrapper.setComponentAlignment(isUnternehmer, Alignment.MIDDLE_RIGHT);
        checkBoxWrapper.setComponentAlignment(isStudent, Alignment.MIDDLE_LEFT);


        Button registerButton = new Button("Register");
        registerButton.setStyleName("login_button");
        registerButton.setId("registerButton");
        rightPane.addComponents(email, username, password, validatePassword, checkBoxWrapper);

        body.addComponent(leftPage);

        body.addComponent(rightPane);

        this.addComponent(body);
        this.addComponent(new Footer());

        TextField matrField = new TextField();
        matrField.setId("matrField");
        Label matrFieldLabel = new Label();
        matrFieldLabel.setIcon(VaadinIcons.CHECK_CIRCLE_O);
        TextFieldWithIcon matrnr = new TextFieldWithIcon(matrField, "Matrikelnummer", matrFieldLabel);

        TextField firmaField = new TextField();
        firmaField.setId("firmaField");
        Label firmaFieldLabel = new Label();
        firmaFieldLabel.setIcon(VaadinIcons.CHECK_CIRCLE_O);
        TextFieldWithIcon firma = new TextFieldWithIcon(firmaField, "Firma", firmaFieldLabel);


        //Component Logic

        isStudent.addValueChangeListener(e -> {


            if (isStudent.getValue()) {

                isUnternehmer.setValue(false);
                rightPane.addComponent(matrnr);
                rightPane.addComponent(registerButton);
            } else {
                rightPane.removeComponent(matrnr);
                rightPane.removeComponent(registerButton);
            }


        });


        isUnternehmer.addValueChangeListener(e -> {


            if (isUnternehmer.getValue()) {

                isStudent.setValue(false);
                rightPane.addComponents(firma);
                rightPane.addComponent(registerButton);

            } else {
                rightPane.removeComponent(firma);
                rightPane.removeComponent(registerButton);

            }


        });

        registerButton.addClickListener(e -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(emailField.getValue().toLowerCase());
            userDTO.setUsername(usernameField.getValue());
            userDTO.setPassword(passwordField.getValue());
            userDTO.setUnternehmer(isUnternehmer.getValue());

            if (userDTO.isUnternehmer()) {
                userDTO.setFirma(firmaField.getValue());
            } else {
                userDTO.setMatrnr(matrField.getValue());
            }

            //
            try {
                boolean erfolg = RegistrationControl.register(userDTO);
                if (erfolg) {
                    RegistrationSuccessWindow registrationSuccessWindow = new RegistrationSuccessWindow(usernameField.getValue());
                    UI.getCurrent().addWindow(registrationSuccessWindow);
                }
            } catch (DatabaseException databaseException) {
                Notification.show(null, databaseException.getReason(), Notification.Type.ERROR_MESSAGE);
            } catch (RegisterFailException registerFailException) {
                Notification.show(null, registerFailException.getReason(), Notification.Type.ERROR_MESSAGE);
            }


        });

        // Built in ERROR's

        Label errorWarningEmail = new Label("Bitte geben sie eine korrekte Email ein.");
        errorWarningEmail.setStyleName("errorwarning einzeiler");

        Label errorWarningUsername = new Label("Der Username ist schon vergeben, bitte wählen Sie einen anderen");
        errorWarningUsername.setStyleName("errorwarning zweizeiler");

        Label errorWarningPassword = new Label("Ihre Eingaben stimmen nicht überein.");
        errorWarningPassword.setStyleName("errorwarning einzeiler");


        //TEXTFIELDLISTENERS

        emailField.addBlurListener(e -> {
            if (!isValid(emailField.getValue()) && !emailField.getValue().isEmpty()) {
                rightPane.addComponent(errorWarningEmail, rightPane.getComponentIndex(email));
                registerButton.setEnabled(false);
            } else if (errorWarningEmail.isAttached()) {
                rightPane.removeComponent(errorWarningEmail);
            }
        });


        usernameField.addValueChangeListener(e -> {

            try {
                boolean usernameAlreadyUsed = UserDAO.getInstance().usernameIsAlreadyInUse(usernameField.getValue());
                if (usernameAlreadyUsed) {
                    rightPane.addComponent(errorWarningUsername, rightPane.getComponentIndex(username));
                    registerButton.setEnabled(false);
                } else if (errorWarningUsername.isAttached()) {
                    rightPane.removeComponent(errorWarningUsername);
                }
            } catch (DatabaseException databaseException) {
                Notification.show(null, databaseException.getReason(), Notification.Type.ERROR_MESSAGE);
            }
        });

        usernameField.setValueChangeMode(ValueChangeMode.LAZY);


        validatePasswordField.addBlurListener(e -> {
            if (!passwordField.getValue().equals(validatePasswordField.getValue())) {
                rightPane.addComponent(errorWarningPassword, rightPane.getComponentIndex(validatePassword));
                registerButton.setEnabled(false);
            } else if (errorWarningPassword.isAttached()) {
                rightPane.removeComponent(errorWarningPassword);

            }
        });
        validatePasswordField.setValueChangeMode(ValueChangeMode.LAZY);


        // Enable RegisterButton Just if there are no errors
        rightPane.addComponentDetachListener(e -> {
            if (rightPane.getComponentIndex(errorWarningEmail) == -1 && rightPane.getComponentIndex(errorWarningUsername) == -1
                    && rightPane.getComponentIndex(errorWarningPassword) == -1) {
                registerButton.setEnabled(true);
            }
        });


    }


    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
