package org.mortys.process.control;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.UserDTO;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.NoSuchUserOrPassword;

import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class SearchControlTest {

    private UserDTO student;
    @Mock
    private UI myUi;
    @Mock
    private VaadinSession session;

    @Before
    public void setup() {
        //Test-Daten für den Student


        UI.setCurrent(myUi);
        when(myUi.getSession()).thenReturn(session);
    }

    @Test
    public void getInstance() {
        assertNotNull(SearchControl.getInstance());
    }


    @Test
    public void searchStudenten() throws DatabaseException, NoSuchUserOrPassword {

        List<Student> studentenList = SearchControl.getInstance().searchStellenStudenten("", "Hamburg", "", "");
        assertNotEquals(0, studentenList.size());
    }


    @Test
    public void searchStellenAnzeigen() throws DatabaseException, NoSuchUserOrPassword {

        List<StellenAnzeige> stellenAnzeigeList = SearchControl.getInstance().searchStellenAnzeigen("", "Bitterfeld");
        assertNotEquals(0, stellenAnzeigeList.size());
    }


}
