package org.mortys.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.LoginControl;
import org.mortys.process.control.ToggleControl;
import org.mortys.services.util.Roles;
import org.mortys.services.util.Views;

public class Header extends HorizontalLayout {

    //Style

    User user = null;
    public Header(boolean logo){
        this.setStyleName("header_main");
     Image headLogo = new Image(null, new ThemeResource("img/logo_Mortys.png"));
     headLogo.setWidth(150, Unit.PIXELS);
     headLogo.setStyleName("header_main_logo");
     if(logo) {
         this.addComponent(headLogo);
     }
    HorizontalLayout header_menuBox = new HorizontalLayout();
    header_menuBox.setStyleName("header_main_menuBox");

        // USER FETCHING
        User user = (User) UI.getCurrent().getSession().getAttribute(Roles.CURRENT_USER);

            Label headLabel = new Label("Logged in as: " + (user instanceof Student ? "Student: " + ((Student) user).getMatrikelnr() + " " + user.getVorname() + ", "+ user.getNachname() : user instanceof Unternehmer ? "Unternehmer: " + ((Unternehmer) user).getFirmenname() + " " + user.getVorname() + ", " + user.getNachname() : "Admin"));
            headLabel.addStyleName("header_main_menuBox_headLabel");
            CheckBoxFeature c = new CheckBoxFeature();

            c.setFeature("func-set-bewerbungen");
            c.setCaption("Bewerbungen deaktivieren");
            ToggleControl.addToggle(c);
            ToggleControl.setValue(c);

        headLogo.addClickListener(e ->{
           UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        });



    MenuBar bar = new MenuBar();
    MenuBar.MenuItem item1 = bar.addItem("", null);

    //Loggout
        item1.addItem("Logout", VaadinIcons.SIGN_OUT, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                LoginControl.logoutUser();
            }
        });


        item1.addItem("Profil", VaadinIcons.USER, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                UI.getCurrent().getNavigator().navigateTo(Views.PROFILE);
            }
        });


        item1.addItem("Suche", VaadinIcons.SEARCH, new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem menuItem) {
                UI.getCurrent().getNavigator().navigateTo(Views.SEARCH);
            }
        });




            if(user != null){
                header_menuBox.addComponent(headLabel);
                if (user.getEmail().equals("admin@admin.de")) {
                    header_menuBox.addComponent(c);
                }
                header_menuBox.addComponent(bar);
            }
        this.addComponent(header_menuBox);

    }

    }

