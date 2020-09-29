package org.mortys.process.control.proxy;

import org.mortys.model.dao.StellenAnzeigeDAO;
import org.mortys.model.dao.StudentDAO;
import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;

import java.util.List;

public class SearchProxy implements  SearchInstance {
private static  SearchProxy searchProxy;

    public static SearchProxy getInstance() {
        if(searchProxy == null) searchProxy = new SearchProxy();
        return searchProxy;
    }

    @Override
    public  List<StellenAnzeige> searchStellenAnzeigen(String stellenanzeige , String ort) throws DatabaseException {
        return StellenAnzeigeDAO.getInstance().fetchSpecificStellenAnzeigen(stellenanzeige , ort);
    }


    @Override
    public  List<Student> searchStellenStudenten(String student , String ort, String beschaeftigung, String faehigkteiten) throws DatabaseException {
        return StudentDAO.getInstance().fetchSpecificStudents(student, ort, beschaeftigung, faehigkteiten);
    }
}
