package org.mortys.gui.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.mortys.gui.components.Footer;
import org.mortys.gui.components.GridCallbackValueProvider;
import org.mortys.gui.components.Header;
import org.mortys.gui.components.TextFieldWithIcon;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;
import org.mortys.process.control.SearchControl;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.util.BeschäftigungsArt;
import org.mortys.services.util.Roles;
import org.mortys.services.util.UserFetcher;
import org.mortys.services.util.Views;

import java.util.List;

public class SearchView extends VerticalLayout implements View {
    Student student = null;
    Unternehmer unternehmer = null;
    User user = null;
    private Label headerLabel;
    private TextFieldWithIcon searchLeft;
    private TextFieldWithIcon searchRight;
    private TextFieldWithIcon searchRightDown;
    private ComboBox searchLeftDown;
    private Button searchButton;
    private HorizontalLayout resultContainer;
    private Grid hitList;


    public SearchView(){
        this.user = UserFetcher.getUser();
        if(user instanceof Student){
            student= (Student)user;
        } else {
            unternehmer = (Unternehmer)user;
        }
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
        if(user instanceof Student){
            student= (Student)user;
        } else {
            unternehmer = (Unternehmer)user;
        }

        if(user == null){
            UI.getCurrent().getNavigator().navigateTo(Views.MAIN);
        } else {
            this.setUp();
        }

    }


    public void setUp() {

        boolean isUnternehmer = user instanceof Unternehmer;
        Header header = new Header(true);
        addComponent(header);
        this.setStyleName("layout_search");


        headerLabel = new Label(isUnternehmer ? "FINDE PASSENDE BEWERBER" : "FINDE DEIN UNTERNEHMEN");
        headerLabel.setStyleName("overLabelStudent");
        this.addComponent(headerLabel);



        searchButton = new Button("Search");
        searchButton.setStyleName("landing_loginButton searchButton");



        searchLeft = getFirst(isUnternehmer);
        searchRight = getSecond();


        HorizontalLayout innerCenterWrapper = isUnternehmer ? null : new HorizontalLayout(searchLeft, searchRight);
        VerticalLayout innerVerticalLayout = isUnternehmer ? null : new VerticalLayout(innerCenterWrapper);
        VerticalLayout centerWrapper = new VerticalLayout();
        centerWrapper.setStyleName("innerCenterWrapper");


        if (!isUnternehmer) {
            innerCenterWrapper.addComponent(searchButton);
            searchRight.setStyleName("searchRigth");
            searchLeft.setStyleName("searchLeft");
            innerCenterWrapper.setStyleName("innerCenterWrapper");
            innerVerticalLayout.setStyleName("innerVerticalLayout");
            innerVerticalLayout.setStyleName("marginlabel");
            centerWrapper.addComponent(innerVerticalLayout);

        }


        if (isUnternehmer) {
            Label left = new Label("Student");
            left.setStyleName("leftLabel");
            Label leftd = new Label("Beschäftigungsart");
            leftd.setStyleName("leftLabel");
            Label right = new Label("Ort");
            right.setStyleName("rightLabel");
            Label rightd = new Label("Fähigkeiten");
            rightd.setStyleName("rightLabel");



            VerticalLayout vertLeft = new VerticalLayout(left, getFirst(isUnternehmer));
            VerticalLayout vertright = new VerticalLayout(right, getSecond());
            VerticalLayout vertLeftd = new VerticalLayout(leftd, getThird());
            VerticalLayout vertrightd = new VerticalLayout(rightd, getFourth());
            vertLeft.setStyleName("searchLeft");
            vertLeftd.setStyleName("searchLeft");
            vertright.setStyleName("searchRigth");
            vertrightd.setStyleName("searchRigth");


            innerCenterWrapper = new HorizontalLayout(vertLeft, vertright);
            innerCenterWrapper.setStyleName("innerCenterWrapper");
            HorizontalLayout innerCenterWrapperDown = new HorizontalLayout(vertLeftd, vertrightd, searchButton);
            innerCenterWrapperDown.setStyleName("innerCenterWrapper");

            innerVerticalLayout = new VerticalLayout(innerCenterWrapper, innerCenterWrapperDown);
            innerVerticalLayout.setStyleName("innerVerticalLayout");
            innerVerticalLayout.setStyleName("marginlabel");


            searchButton.addStyleName("down");


            centerWrapper.addComponent(innerVerticalLayout);
        }





        HorizontalLayout searchWrapper = new HorizontalLayout(centerWrapper);
        searchWrapper.setStyleName("searchWrapper");

        this.addComponents(searchWrapper);
        this.addComponent(new Footer());

        //Logic

        searchButton.addClickListener(e -> {

            if (isUnternehmer) {
                setUpStudentenSearch(isUnternehmer, searchWrapper);
            } else {
                setUpStellenanzeigenSearch(isUnternehmer, searchWrapper);
            }

        });





    }

    private void setUpStudentenSearch(boolean isUnternehmer, HorizontalLayout searchWrapper) {
        if (getFirst(isUnternehmer).getValue().isEmpty() && getSecond().getValue().isEmpty() && getFourth().getValue().isEmpty()) {
            Notification.show("Fehler", "Bitte wählen Sie mindestens einen Suchbegriff", Notification.Type.WARNING_MESSAGE);
            return;
        }




        if(hitList == null) hitList = getHitlistStudent();

        headerLabel.setVisible(false);

        List<Student> list = null;

        try {
            list = SearchControl.getInstance().searchStellenStudenten(getFirst(isUnternehmer).getValue(), getSecond().getValue(),
                    getThird().getValue().toString(), getFourth().getValue());
        } catch (DatabaseException ex2) {
            Notification.show("DB-Fehler", ex2.getReason(), Notification.Type.ERROR_MESSAGE);
        }

        hitList.setItems(list);
        hitList.setHeightByRows(list.size() > 0 ? list.size() : 1);
        if (list.size() == 0) {
            Notification.show("Die Suche war leider ergebnislos", Notification.Type.HUMANIZED_MESSAGE);
        }


        if(resultContainer == null) {
            this.removeComponent(searchWrapper);
            this.searchButton.removeStyleName("searchButton");
            Button closeButton = new Button(VaadinIcons.CLOSE);
            closeButton.setStyleName("closeButton");
            VerticalLayout collapsedSearchBar = new VerticalLayout(closeButton, getFirst(isUnternehmer), getSecond(),
                    getThird(), getFourth(), searchButton);
            collapsedSearchBar.setStyleName("collapsedSearchBar");
            collapsedSearchBar.setComponentAlignment(searchButton, Alignment.MIDDLE_CENTER);
            collapsedSearchBar.setComponentAlignment(closeButton, Alignment.TOP_RIGHT);
            resultContainer = new HorizontalLayout(collapsedSearchBar, hitList);
            resultContainer.setStyleName("resultContainer");
            this.addComponent(resultContainer, getComponentCount()-1);

            closeButton.addClickListener(e -> {
                hitList.addStyleName("size-full");
                collapsedSearchBar.setStyleName("collapse");
                Button recollapseSearchBarButton = new Button(VaadinIcons.CHEVRON_CIRCLE_RIGHT);
                recollapseSearchBarButton.setStyleName("openButton");
                this.addComponent(recollapseSearchBarButton);
                recollapseSearchBarButton.addClickListener(b -> {
                    collapsedSearchBar.setStyleName("collapsedSearchBar");
                    hitList.setStyleName("hitList hitListStudent");
                    removeComponent(recollapseSearchBarButton);
                });


            });

        }

    }


    public void setUpStellenanzeigenSearch(boolean isUnternehmer, HorizontalLayout searchWrapper) {
        if (getFirst(isUnternehmer).getValue().isEmpty() && getSecond().getValue().isEmpty()) {
            Notification.show("Fehler", "Bitte tippen sie etwas in die Beiden Felder ein", Notification.Type.WARNING_MESSAGE);
            return;
        }

        searchWrapper.removeStyleName("searchWrapper");
        searchWrapper.addStyleName("searchWrapper_collapsed");
        headerLabel.setVisible(false);

        List<StellenAnzeige> list = null;

        try {
            list = SearchControl.getInstance().searchStellenAnzeigen(getFirst(isUnternehmer).getValue(), getSecond().getValue());
        } catch (DatabaseException ex2) {
            Notification.show("DB-Fehler", ex2.getReason(), Notification.Type.ERROR_MESSAGE);
        }

        if(hitList == null) hitList = getHitlistStellenAnzeige();

        hitList.setItems(list);
        hitList.setHeightByRows(list.size() > 0 ? list.size() : 1);
        if (list.size() == 0) {
            Notification.show("Die Suche war leider ergebnislos", Notification.Type.HUMANIZED_MESSAGE);
        }

        if(!hitList.isAttached()) this.addComponent(hitList, getComponentCount()-1);
    }

    private Grid<StellenAnzeige> getHitlistStellenAnzeige() {
        Grid<StellenAnzeige> hitList = new Grid<>();
        hitList.addColumn(StellenAnzeige::getId).setCaption("Id");
        hitList.addColumn(StellenAnzeige::getUnternehmen).setCaption("Firma");
        hitList.addColumn(StellenAnzeige::getErstellungsDatum).setCaption("Veröffentlichung");
        hitList.addColumn(StellenAnzeige::getTitel).setCaption("Titel");
        hitList.addColumn(StellenAnzeige::getOrt).setCaption("Ort");
        hitList.setStyleName("hitList");
        hitList.addComponentColumn(new GridCallbackValueProvider(hitList));


        return hitList;
    }

    private Grid<Student> getHitlistStudent() {

        Grid<Student> hitList = new Grid<>();
        hitList.addColumn(Student::getBeschäftigung).setCaption("Beschäftigung");
        hitList.addColumn(User::getOrt).setCaption("Ort");
        hitList.addColumn(User::getEmail).setCaption("Email");
        hitList.addColumn(Student::getSoftskills).setCaption("Fertigkeiten");
        hitList.setStyleName("hitList hitListStudent");
        hitList.addComponentColumn(new GridCallbackValueProvider(hitList));

        return hitList;
    }


    private TextFieldWithIcon getFirst(boolean isUnternehmer) {
        if (searchLeft == null) {
            TextField searchFieldLeft = new TextField();
            Label searchFieldLeftLabel = new Label();
            searchFieldLeftLabel.setIcon(isUnternehmer ? VaadinIcons.USER : VaadinIcons.ROCKET);
            TextFieldWithIcon searchLeft = new TextFieldWithIcon("0", searchFieldLeft, isUnternehmer ? "Studenten finden" : "Job finden", searchFieldLeftLabel);
            this.searchLeft = searchLeft;
        }
        return this.searchLeft;
    }


    private TextFieldWithIcon getSecond() {
        if(searchRight == null) {
            TextField searchFieldRight = new TextField();
            Label searchFieldRightLabel = new Label();
            searchFieldRightLabel.setIcon(VaadinIcons.LOCATION_ARROW_CIRCLE);
            TextFieldWithIcon searchRight = new TextFieldWithIcon("0", searchFieldRight, "Ort", searchFieldRightLabel);
            this.searchRight = searchRight;
        }
        return this.searchRight;
    }

    private ComboBox getThird() {
        if(searchLeftDown == null) {
            searchLeftDown = new ComboBox();
            searchLeftDown.setTextInputAllowed(false);
            searchLeftDown.setItems(BeschäftigungsArt.ALL, BeschäftigungsArt.VOLLZEIT, BeschäftigungsArt.WERK, BeschäftigungsArt.DUAL);
            searchLeftDown.setSelectedItem(BeschäftigungsArt.ALL);
            this.searchLeftDown = searchLeftDown;
        }
        return this.searchLeftDown;
    }

    private TextFieldWithIcon getFourth() {
        if(searchRightDown == null) {
            TextField searchFieldRightDown = new TextField();
            Label searchFieldRightLabelDown = new Label();
            searchFieldRightLabelDown.setIcon(VaadinIcons.USER_CARD);
            TextFieldWithIcon searchRightDown = new TextFieldWithIcon("0", searchFieldRightDown, "Qualifikationen", searchFieldRightLabelDown);
            this.searchRightDown = searchRightDown;
        }
        return this.searchRightDown;
    }


}
