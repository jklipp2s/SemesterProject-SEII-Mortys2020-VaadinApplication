package org.mortys.process.control;

import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.proxy.SearchInstance;
import org.mortys.process.control.proxy.SearchProxy;

import java.util.List;
public class SearchControl implements SearchInstance {
    private static SearchControl searchControl;

    public static SearchControl getInstance(){
        if(searchControl == null) searchControl = new SearchControl();
        return searchControl;
    }

    public  List<StellenAnzeige> searchStellenAnzeigen(String stellenanzeige , String ort) throws DatabaseException {
        return SearchProxy.getInstance().searchStellenAnzeigen(stellenanzeige , ort);
    }

    public  List<Student> searchStellenStudenten(String student , String ort, String beschaeftigung, String faehigkteiten) throws DatabaseException {
        return SearchProxy.getInstance().searchStellenStudenten(student, ort, beschaeftigung, faehigkteiten);
    }



}
