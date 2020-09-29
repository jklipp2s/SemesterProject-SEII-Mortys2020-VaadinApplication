package org.mortys.process.control.proxy;

import org.mortys.model.objects.dto.StellenAnzeige;
import org.mortys.model.objects.dto.Student;
import org.mortys.process.control.exception.DatabaseException;

import java.util.List;

public interface SearchInstance {
      List<StellenAnzeige> searchStellenAnzeigen(String stellenanzeige , String ort) throws DatabaseException;

      List<Student> searchStellenStudenten(String student , String ort, String beschaeftigung, String faehigkteiten) throws DatabaseException;

}
