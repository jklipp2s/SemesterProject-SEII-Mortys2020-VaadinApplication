package org.mortys.gui.components;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;

public class ProfileLabel extends HorizontalLayout {
    Label ibanLabel=null,ustIdLabel=null,telefonnummerLabel,ortLabel,plzLabel,hausnummerLabel,strasseLabel,nachnameLabel,vornameLabel,anredeLabel,firmaLabel=null,geburtsdatumLabel,matrikelnummerLabel=null;

    public ProfileLabel(Unternehmer user){
        this.setStyleName("profile_labelContainer");
        firmaLabel = new Label(user.getFirmenname());
        ustIdLabel = new Label(user.getUstId());
        ibanLabel = new Label(user.getIban());
        HorizontalLayout body = this.setUp(user);
        this.addComponent(body);
    }

    public ProfileLabel(Student user) {
        this.setStyleName("profile_labelContainer");
        matrikelnummerLabel = new Label(user.getMatrikelnr());
        HorizontalLayout body = this.setUp(user);
        this.addComponent(body);
    }


    public HorizontalLayout setUp(User user){
        HorizontalLayout body = new HorizontalLayout();
        body.addStyleName("profile_container");

        VerticalLayout leftPane = new VerticalLayout();
        leftPane.setStyleName("profile_leftPane");

        VerticalLayout rightPane = new VerticalLayout();
        rightPane.addStyleName("profile_rightPane");

        HorizontalLayout addressContainerOne = new HorizontalLayout();
        addressContainerOne.setStyleName("profile_addressContrainerOne");

        HorizontalLayout addressContainerTwo = new HorizontalLayout();
        addressContainerTwo.setStyleName("profile_addressContrainerTwo");

        HorizontalLayout ibanUstidContainer = new HorizontalLayout();
        ibanUstidContainer.setStyleName("profile_ibanUstidContainer");

        VerticalLayout textContainerLeft = new VerticalLayout();
        textContainerLeft.setStyleName("profile_textContainerLeft");

        VerticalLayout textContainerRight = new VerticalLayout();
        textContainerRight.setStyleName("profile_textContainerRight");

        Label spacing = new Label("&nbsp", ContentMode.HTML);
        Label spacing2 = new Label("&nbsp", ContentMode.HTML);
        Label spacing3 = new Label("&nbsp", ContentMode.HTML);
        Label spacing4 = new Label("&nbsp", ContentMode.HTML);
        Label spacing5 = new Label("&nbsp", ContentMode.HTML);

        leftPane.addComponent(new ImageUpload(user));

        anredeLabel = new Label(user.getAnrede());
        vornameLabel = new Label(user.getVorname());
        nachnameLabel = new Label(user.getNachname());
        if(user.getGeburtsdatum() != null) {geburtsdatumLabel = new Label("Geb.: "+ user.getGeburtsdatum().toString());}
        strasseLabel = new Label(user.getAddress() == null ? "" : user.getAddress().getStreet());
        hausnummerLabel = new Label(user.getAddress() == null ? "" : "" + user.getAddress().getHausnummer());
        plzLabel = new Label(user.getAddress() == null ? "" : user.getAddress().getPlz());
        ortLabel = new Label(user.getAddress() == null ? "" : user.getAddress().getOrt());
        telefonnummerLabel = new Label(user.getTelefon());

        textContainerLeft.addComponent(anredeLabel);
        textContainerLeft.addComponent(vornameLabel);
        textContainerLeft.addComponent(nachnameLabel);
            if(geburtsdatumLabel != null) textContainerLeft.addComponent(geburtsdatumLabel);
        textContainerLeft.addComponent(user instanceof Student ? matrikelnummerLabel : firmaLabel);

        leftPane.addComponent(textContainerLeft);

            addressContainerOne.addComponent(strasseLabel);
            addressContainerOne.addComponent(spacing);
            addressContainerOne.addComponent(hausnummerLabel);
            addressContainerTwo.addComponent(plzLabel);
            addressContainerTwo.addComponent(spacing2);
            addressContainerTwo.addComponent(ortLabel);
        textContainerRight.addComponent(addressContainerOne);
        textContainerRight.addComponent(addressContainerTwo);
            if(!(user instanceof Student)) ibanUstidContainer.addComponent(ustIdLabel);
            if(!(user instanceof Student)) ibanUstidContainer.addComponent(spacing3);
            if(!(user instanceof Student)) ibanUstidContainer.addComponent(ibanLabel);
            if(!(user instanceof Student)) textContainerRight.addComponent(ibanUstidContainer);
        textContainerRight.addComponent(telefonnummerLabel);

        rightPane.addComponent(textContainerRight);

            body.addComponent(leftPane);
            body.addComponent(rightPane);
            return body;
    }
/*
    public void setLogo(VerticalLayout pane){
        Label usrBg = new Label();
        usrBg.setStyleName("profile_usrbg");
        usrBg.setIcon(VaadinIcons.USER);
        pane.addComponent(usrBg);

        Label kindOfUser = new Label(/*student != null ? "Student" : "Unternehmer");
        kindOfUser.setStyleName("profile_kindOfUser");
        pane.addComponent(kindOfUser);
    } */

}
