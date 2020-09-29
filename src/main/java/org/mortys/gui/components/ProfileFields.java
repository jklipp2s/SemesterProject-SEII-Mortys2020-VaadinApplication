package org.mortys.gui.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;

import java.time.LocalDate;

public class ProfileFields extends HorizontalLayout {
    NativeSelect<String> anredeSelect;
    TextField vornameFieldbasic, nachnameFieldbasic,matrikelnummerFieldBasic=null,firmaFieldBasic=null,strasseFieldBasic,hausnummerFieldBasic,plzFieldBasic,ortFieldBasic,telefonnummerFieldBasic,ustIdFieldBasic=null,ibanFieldBasic=null;
    DateField dateField;
    Label vornameFieldLabel,nachnameFieldLabel,addresseLabel,matrikelnummerFieldLabel=null,firmaFieldLabel=null,strasseFieldlLabel,hausnummerFieldLabel,plzFieldLabel,ortFieldLabel,telefonnummerFieldLabel,ustIdFieldlabel,ibanFieldLabel;
    TextFieldWithIcon vornameField,nachnameField,matrikelnummerField=null,firmaField=null,strasseField,hausnummerField,plzField,ortField,telefonnummerField,ustIdField=null,ibanField=null;

    public ProfileFields(Unternehmer user){
        this.setStyleName("profile_fieldContainer");
        firmaFieldLabel = new Label();
        firmaFieldBasic = new TextField();
        firmaFieldLabel.setIcon(VaadinIcons.FACTORY);
        firmaField = new TextFieldWithIcon(firmaFieldBasic, user.getFirmenname(),firmaFieldLabel);

        ustIdFieldlabel = new Label();
        ustIdFieldBasic = new TextField();
        ustIdFieldlabel.setIcon(VaadinIcons.USER_CARD);
        ustIdField = new TextFieldWithIcon(ustIdFieldBasic,user.getUstId(),ustIdFieldlabel);

        ibanFieldLabel = new Label();
        ibanFieldBasic = new TextField();
        ibanFieldLabel.setIcon(VaadinIcons.CREDIT_CARD);
        ibanField = new TextFieldWithIcon(ibanFieldBasic, user.getIban(),ibanFieldLabel);

        HorizontalLayout body = this.setUp(user);
        this.addComponent(body);
    }

    public ProfileFields(Student user) {
        this.setStyleName("profile_fieldContainer");
        matrikelnummerFieldLabel = new Label();
        matrikelnummerFieldBasic = new TextField();
        matrikelnummerFieldLabel.setIcon(VaadinIcons.DIPLOMA_SCROLL);
        matrikelnummerField = new TextFieldWithIcon(matrikelnummerFieldBasic, user.getMatrikelnr(),matrikelnummerFieldLabel);
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

        anredeSelect = new NativeSelect<>();
        anredeSelect.setItems("Herr", "Frau", "Diverse");
        anredeSelect.setValue(user.getAnrede());
        anredeSelect.setStyleName("profile_anrede");

        vornameFieldbasic = new TextField();
        vornameFieldLabel = new Label();
        vornameFieldLabel.setIcon(VaadinIcons.USER);
        vornameField = new TextFieldWithIcon(vornameFieldbasic, user.getVorname(), vornameFieldLabel);


        nachnameFieldbasic = new TextField();
        nachnameFieldLabel = new Label();
        nachnameFieldLabel.setIcon(VaadinIcons.USER);
        nachnameField = new TextFieldWithIcon(nachnameFieldbasic, user.getNachname(), nachnameFieldLabel);



        dateField = new DateField();
        dateField.setStyleName("profile_date");
        dateField.setValue(user.getGeburtsdatum());
        dateField.setValue(LocalDate.now());

        strasseFieldBasic = new TextField();
        strasseFieldlLabel = new Label();
        strasseFieldlLabel.setIcon(VaadinIcons.CAR);
        strasseField = new TextFieldWithIcon(strasseFieldBasic, user.getAddress() == null ? "" : user.getAddress().getStreet(), strasseFieldlLabel);

        hausnummerFieldBasic = new TextField();
        hausnummerFieldLabel = new Label();
        hausnummerFieldLabel.setIcon(VaadinIcons.HOME);
        hausnummerField = new TextFieldWithIcon(hausnummerFieldBasic,user.getAddress() == null ? "" : "" + user.getAddress().getHausnummer(), hausnummerFieldLabel);

        plzFieldBasic = new TextField();
        plzFieldLabel = new Label();
        plzFieldLabel.setIcon(VaadinIcons.TAG);
        plzField = new TextFieldWithIcon(plzFieldBasic,user.getAddress() == null ? "" : user.getAddress().getPlz(), plzFieldLabel);

        ortFieldBasic = new TextField();
        ortFieldLabel = new Label();
        ortFieldLabel.setIcon(VaadinIcons.BUILDING);
        ortField = new TextFieldWithIcon(ortFieldBasic,user.getAddress() == null ? "" : user.getAddress().getOrt(), ortFieldLabel);

        telefonnummerFieldBasic = new TextField();
        telefonnummerFieldLabel = new Label();
        telefonnummerFieldLabel.setIcon(VaadinIcons.PHONE);
        telefonnummerField = new TextFieldWithIcon(telefonnummerFieldBasic, user.getTelefon(), telefonnummerFieldLabel);

        textContainerLeft.addComponent(anredeSelect);
        textContainerLeft.addComponent(vornameField);
        textContainerLeft.addComponent(nachnameField);
        textContainerLeft.addComponent(dateField);
        textContainerLeft.addComponent(user instanceof Student ? matrikelnummerField : firmaField);
        addressContainerOne.addComponent(strasseField);
        addressContainerOne.addComponent(spacing);
        addressContainerOne.addComponent(hausnummerField);
        addressContainerTwo.addComponent(plzField);
        addressContainerTwo.addComponent(spacing2);
        addressContainerTwo.addComponent(ortField);
        textContainerRight.addComponent(addressContainerOne);
        textContainerRight.addComponent(addressContainerTwo);
        if(!(user instanceof Student)) ibanUstidContainer.addComponent(ustIdField);
        if(!(user instanceof Student)) ibanUstidContainer.addComponent(spacing3);
        if(!(user instanceof Student)) ibanUstidContainer.addComponent(ibanField);
        if(!(user instanceof Student)) textContainerRight.addComponent(ibanUstidContainer);
        textContainerRight.addComponent(telefonnummerField);

        leftPane.addComponent(textContainerLeft);
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

        Label kindOfUser = new Label(student != null ? "Student" : "Unternehmer");
        kindOfUser.setStyleName("profile_kindOfUser");
        pane.addComponent(kindOfUser);
    }*/

    public NativeSelect<String> getAnredeSelect() {
        return anredeSelect;
    }

    public TextField getVornameFieldbasic() {
        return vornameFieldbasic;
    }

    public TextField getNachnameFieldbasic() {
        return nachnameFieldbasic;
    }

    public TextField getMatrikelnummerFieldBasic() {
        return matrikelnummerFieldBasic;
    }

    public TextField getFirmaFieldBasic() {
        return firmaFieldBasic;
    }

    public TextField getStrasseFieldBasic() {
        return strasseFieldBasic;
    }

    public TextField getHausnummerFieldBasic() {
        return hausnummerFieldBasic;
    }

    public TextField getPlzFieldBasic() {
        return plzFieldBasic;
    }

    public TextField getOrtFieldBasic() {
        return ortFieldBasic;
    }

    public TextField getTelefonnummerFieldBasic() {
        return telefonnummerFieldBasic;
    }

    public TextField getUstIdFieldBasic() {
        return ustIdFieldBasic;
    }

    public TextField getIbanFieldBasic() {
        return ibanFieldBasic;
    }

    public DateField getDateField() {
        return dateField;
    }

    public Label getVornameFieldLabel() {
        return vornameFieldLabel;
    }

    public Label getNachnameFieldLabel() {
        return nachnameFieldLabel;
    }

    public Label getAddresseLabel() {
        return addresseLabel;
    }

    public Label getMatrikelnummerFieldLabel() {
        return matrikelnummerFieldLabel;
    }

    public Label getFirmaFieldLabel() {
        return firmaFieldLabel;
    }

    public Label getStrasseFieldlLabel() {
        return strasseFieldlLabel;
    }

    public Label getHausnummerFieldLabel() {
        return hausnummerFieldLabel;
    }

    public Label getPlzFieldLabel() {
        return plzFieldLabel;
    }

    public Label getOrtFieldLabel() {
        return ortFieldLabel;
    }

    public Label getTelefonnummerFieldLabel() {
        return telefonnummerFieldLabel;
    }

    public Label getUstIdFieldlabel() {
        return ustIdFieldlabel;
    }

    public Label getIbanFieldLabel() {
        return ibanFieldLabel;
    }
}
