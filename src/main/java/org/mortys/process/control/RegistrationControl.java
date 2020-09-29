package org.mortys.process.control;

import org.mortys.model.dao.StudentDAO;
import org.mortys.model.dao.UnternehmerDAO;
import org.mortys.model.dao.UserDAO;
import org.mortys.model.objects.dto.Student;
import org.mortys.model.objects.dto.Unternehmer;
import org.mortys.model.objects.dto.UserDTO;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.process.control.exception.RegisterFailException;

public class RegistrationControl {



    public static boolean register(UserDTO userDTO) throws DatabaseException, RegisterFailException {

        String exception = "";

        if (UserDAO.getInstance().emailIsAlreadyInUse(userDTO.getEmail())) exception += "Email wurde schon verwendet\n";        if (userDTO.getPassword().length() > 30) exception += "Passwort ist zu lang\n";
        if (userDTO.getEmail().length() > 300) exception += "Emailadresse ist zu lang\n\n";
        if (userDTO.getUsername().length() > 30) exception += "Username ist zu lang\n\n";
        if (!userDTO.isUnternehmer() && userDTO.getMatrnr().length() > 22) exception += "Matrikelnummer ist zu lang\n\n";
        if (userDTO.isUnternehmer() && userDTO.getFirma().length() > 60) exception += "Firmenname ist zu lang\n\n";




        if ( userDTO.getEmail().isEmpty() || userDTO.getPassword().isEmpty()
        ||  userDTO.isUnternehmer() && userDTO.getFirma().isEmpty() ||  !userDTO.isUnternehmer() && userDTO.getMatrnr().isEmpty() ){
            exception += "\nBitte fülle alle Felder aus\n\n";
        }

        if (userDTO.getPassword().length() < 8 && !userDTO.getPassword().isEmpty()) exception += "Dein Passwort muss mindestens 8 Zeichen lang sein\n\n";

        if (exception.isEmpty()) {

         if(userDTO.isUnternehmer()) {
             Unternehmer unternehmer = new Unternehmer();
             unternehmer.setEmail(userDTO.getEmail());
             unternehmer.setUsername(userDTO.getUsername());
             unternehmer.setFirmenname(userDTO.getFirma());
             UnternehmerDAO.getInstance().registerUnternehmer(unternehmer,userDTO.getPassword());
         } else {
             Student student = new Student();
             student.setEmail(userDTO.getEmail());
             student.setUsername(userDTO.getUsername());
             student.setMatrikelnr(userDTO.getMatrnr());
             StudentDAO.getInstance().registerStudent(student,userDTO.getPassword());
         }
            return true;
        } else {

            RegisterFailException ex = new RegisterFailException();
            ex.setReason(exception);
            throw ex;
        }

    }



}
