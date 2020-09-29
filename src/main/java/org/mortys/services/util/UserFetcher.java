package org.mortys.services.util;

import com.vaadin.ui.UI;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.User;

public class UserFetcher {
    public static User getUser(){
        Student student = null;
        Unternehmer unternehmer = null;
       // User user = null;
        if ( UI.getCurrent().getSession().getAttribute(Roles.STUDENT) instanceof Student ) {
            student = (Student) UI.getCurrent().getSession().getAttribute(Roles.STUDENT);
            //user = student;
            return student;
        } else {
            unternehmer = (Unternehmer) UI.getCurrent().getSession().getAttribute(Roles.UNTERNEHMER);
            //user = unternehmer;
            return unternehmer;
        }
    }
}
