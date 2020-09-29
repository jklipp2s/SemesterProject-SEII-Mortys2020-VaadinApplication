package org.mortys.gui.components;


import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.mortys.gui.windows.StellenAnzeigeSelectWindow;
import org.mortys.gui.windows.StudentSelectWindow;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;

public  class GridCallbackValueProvider<T>
        implements ValueProvider<T, Layout> {


    private final Grid<T> grid;


    public GridCallbackValueProvider(Grid<T> grid) {
        this.grid = grid;
    }

    @Override
    public Layout apply(T obj) {
        HorizontalLayout al = new HorizontalLayout();
        Button button = new Button();

        al.addComponent(button);
        button.setIcon(obj instanceof Student ? VaadinIcons.CLIPBOARD_USER : VaadinIcons.USER_CARD);
        button.setStyleName("selectButton");
        button.addClickListener( clickEvent -> {


         if(obj instanceof Student) {
             StudentSelectWindow window = new StudentSelectWindow((Student) obj);
             UI.getCurrent().addWindow(window);
         }


            if(obj instanceof StellenAnzeige) {
                StellenAnzeigeSelectWindow window = new StellenAnzeigeSelectWindow((StellenAnzeige) obj);
                UI.getCurrent().addWindow(window);
            }




            grid.deselectAll();



        });
        return al;
    }

}