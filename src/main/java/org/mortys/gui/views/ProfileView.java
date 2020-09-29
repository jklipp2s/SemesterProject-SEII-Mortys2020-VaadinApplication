package org.mortys.gui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.mortys.gui.components.Footer;
import org.mortys.gui.components.Header;
import org.mortys.gui.components.ProfileFields;
import org.mortys.gui.components.ProfileLabel;
import org.mortys.gui.windows.DeleteUserConfirmationWindow;
import org.mortys.model.dao.StudentDAO;
import org.mortys.model.dao.UnternehmerDAO;
import org.mortys.model.dao.UserDAO;
import org.mortys.model.objects.dto.Address;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.ToggleControl;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.util.Roles;
import org.mortys.services.util.UserFetcher;
import org.mortys.services.util.Views;

import java.time.LocalDate;

import static org.mortys.process.control.UserControl.loadUserToSession;

public class ProfileView extends VerticalLayout implements View {
    Student student = null;
    Unternehmer unternehmer = null;
    User user = null;
    boolean mode = false;
    String persistFirma = "";
    String persistUst ="";
    String persistIban = "";
    String persistMatrikelnummer="";
    ProfileLabel labels;
    ProfileFields fields;
    Header header = new Header(true);
    Footer footer = new Footer();

    public ProfileView(){
        this.user = UserFetcher.getUser();
    }

    /*
    public void fetchUser(){
        if ( UI.getCurrent().getSession().getAttribute(Roles.STUDENT) instanceof Student ) {
            student = (Student) UI.getCurrent().getSession().getAttribute(Roles.STUDENT);
            user = student;
        } else {
            unternehmer = (Unternehmer) UI.getCurrent().getSession().getAttribute(Roles.UNTERNEHMER);
            user = unternehmer;
        }
    }
    */


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.user = UserFetcher.getUser();
        if(user == null){
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        } else {
            this.setUp();
        }

    }

    private void buttonChange(HorizontalLayout buttonBar, Button editProfileButton, Button saveProfileButton, Button cancelProfileButton){
        if(mode){
            this.removeComponent(labels);
            this.removeComponent(buttonBar);
            if(user instanceof Student){
                fields = new ProfileFields((Student)user);
            } else {
                fields = new ProfileFields((Unternehmer)user);
            }
            this.removeComponent(footer);
            this.addComponent(fields);
            buttonBar.removeComponent(editProfileButton);
            buttonBar.addComponent(saveProfileButton);
            buttonBar.addComponent(cancelProfileButton);
            this.addComponent(buttonBar);
            this.addComponent(footer);
        } else {
            this.removeComponent(footer);
            this.removeComponent(fields);
            if(user instanceof Student){
                labels = new ProfileLabel((Student)user);
            } else {
                labels = new ProfileLabel((Unternehmer)user);
            }
            this.addComponent(labels);
            this.addComponent(buttonBar);
            this.addComponent(footer);
            buttonBar.addComponent(editProfileButton);
            buttonBar.removeComponent(saveProfileButton);
            buttonBar.removeComponent(cancelProfileButton);
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
        if(user instanceof Student){
            labels = new ProfileLabel((Student)user);
        } else {
            labels = new ProfileLabel((Unternehmer)user);
        }
        //LABELS - END

        // Button Group - START
        Button editProfileButton = new Button("Edit");
        editProfileButton.setStyleName("profile_editButton");
        editProfileButton.setId("editButton");

        Button deleteProfileButton = new Button("Delete Profile");
        deleteProfileButton.setStyleName("profile_deleteButton");
        deleteProfileButton.setId("deleteButton");

        Button saveProfileButton = new Button("Save");
        saveProfileButton.setStyleName("profile_saveButton");
        saveProfileButton.setId("saveButton");

        Button cancelProfileButton = new Button("Cancel");
        cancelProfileButton.setStyleName("profile_cancelButton");
        cancelProfileButton.setId("cancelButton");

        Button submitJobOfferButton = new Button("Submit Job Offer");
        submitJobOfferButton.setStyleName("profile_saveButton");
        submitJobOfferButton.setId("jobOfferButton");
        buttonBar.addComponent(deleteProfileButton);
        buttonBar.addComponent(editProfileButton);
        if( UI.getCurrent().getSession().getAttribute(Roles.CURRENT_USER) instanceof Unternehmer  && !ToggleControl.featureIsEnabled("func-set-bewerbungen")){
            buttonBar.addComponent(submitJobOfferButton);
        }
        // Button Group - END
        this.addComponent(labels);
        this.addComponent(buttonBar);
        this.addComponent(footer);

        //Button Logic

        editProfileButton.addClickListener(e -> {
            mode = !mode;

            this.user = UserFetcher.getUser();

            this.buttonChange(buttonBar, editProfileButton, saveProfileButton,cancelProfileButton);
        });




        saveProfileButton.addClickListener(e->{
            User persistUser;
            boolean studentUsr = false;
            if(user instanceof Student) {
                persistUser = new Student();
                studentUsr = true;
            } else {
                persistUser = new Unternehmer();
            }

            String persistAnrede = fields.getAnredeSelect().getValue() == null ? "" : fields.getAnredeSelect().getValue();
            String persistNachname =  fields.getNachnameFieldbasic().getValue().equals("") ? user.getNachname() == null ? "" : user.getNachname() : fields.getNachnameFieldbasic().getValue();
            String persistVorname = fields.getVornameFieldbasic().getValue().equals("") ? user.getVorname() == null ? "" : user.getVorname() : fields.getVornameFieldbasic().getValue();
            LocalDate persisteGebDatum = fields.getDateField().getValue();
            if(studentUsr) {persistMatrikelnummer = fields.getVornameFieldbasic().getValue().equals("") ? ((Student) user).getMatrikelnr() == null ? "" : ((Student)user).getMatrikelnr() : fields.getMatrikelnummerFieldBasic().getValue();}
            if(!studentUsr) { persistFirma = fields.getFirmaFieldBasic().getValue().equals("") ? ((Unternehmer)user).getFirmenname() == null ? "" : ((Unternehmer)user).getFirmenname() : fields.getFirmaFieldBasic().getValue();}

            String persistStrasse = fields.getStrasseFieldBasic().getValue().equals("") ? user.getAddress() == null ? "" : user.getAddress().getStreet() : fields.getStrasseFieldBasic().getValue();
            String persistHausnummer =  fields.getHausnummerFieldBasic().getValue().equals("") ? "" + user.getAddress().getHausnummer() : fields.getHausnummerFieldBasic().getValue();            String persistPlz = fields.getPlzFieldBasic().getValue().equals("") ? user.getAddress() == null ? "" : user.getAddress().getPlz() : fields.getPlzFieldBasic().getValue();
            String persistOrt = fields.getOrtFieldBasic().getValue().equals("") ? user.getAddress() == null ? "" : user.getAddress().getOrt() : fields.getOrtFieldBasic().getValue();

            Address persistAddress = new Address();
            persistAddress.setStreet(persistStrasse);
           if(!persistHausnummer.equals("")) { persistAddress.setHausnummer(Integer.parseInt(persistHausnummer));}
            persistAddress.setPlz(persistPlz);
            persistAddress.setOrt(persistOrt);

            if(!studentUsr) { persistUst = fields.getUstIdFieldBasic().getValue().equals("") ? ((Unternehmer)user).getUstId() == null ? "": ((Unternehmer)user).getUstId() : fields.getUstIdFieldBasic().getValue();}
            if(!studentUsr) { persistIban =  fields.getIbanFieldBasic().getValue().equals("") ? ((Unternehmer)user).getIban() == null ? "" : ((Unternehmer)user).getIban() : fields.getIbanFieldBasic().getValue();}

            String persistTelefon = fields.getTelefonnummerFieldBasic().getValue().equals("") ? user.getTelefon() == null ? "" : user.getTelefon() : fields.getTelefonnummerFieldBasic().getValue();

            try {
                UserDAO.getInstance().setAdresse(user, persistAddress);
                UserDAO.getInstance().setAnrede(user, persistAnrede);
                UserDAO.getInstance().setNachname(user, persistNachname);
                UserDAO.getInstance().setVorname(user, persistVorname);
                UserDAO.getInstance().setGeburtsDatum(user, persisteGebDatum);
                UserDAO.getInstance().setTelefon(user, persistTelefon);
               if(!studentUsr) UnternehmerDAO.getInstance().setFirmenname((Unternehmer)user, persistFirma);
               if(!studentUsr) UnternehmerDAO.getInstance().setUstid((Unternehmer)user, persistUst);
               if(!studentUsr) UnternehmerDAO.getInstance().setIban((Unternehmer)user, persistIban);

            } catch (DatabaseException databaseException) {
                databaseException.printStackTrace();
            }

            loadUserToSession(user.getEmail());

            this.user = UserFetcher.getUser();

            mode = !mode;
            this.buttonChange(buttonBar, editProfileButton, saveProfileButton,cancelProfileButton);
        });



        deleteProfileButton.addClickListener(e->{
           UI.getCurrent().addWindow(new DeleteUserConfirmationWindow("Sind Sie sicher!?"));
        });

        cancelProfileButton.addClickListener(e->{

            this.user = UserFetcher.getUser();

            mode = !mode;
            this.buttonChange(buttonBar, editProfileButton, saveProfileButton,cancelProfileButton);
        });

        submitJobOfferButton.addClickListener(e -> {
           UI.getCurrent().getNavigator().navigateTo(Views.APPLICATION);
        });


    }
}
