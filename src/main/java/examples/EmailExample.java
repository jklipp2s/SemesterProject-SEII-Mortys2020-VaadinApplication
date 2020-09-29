package examples;

import org.mortys.process.control.LoginControl;
import org.mortys.process.control.exception.UserNotFoundException;

public class EmailExample {

    public static void main (String[] args) {

        // generate Resetcodes Email muss in der DB registriert sein!
        String email = "vm8443393004031142v@vmail.inf.h-brs.de";

        try {
            System.out.println("Generate Resetcode for Email: " + email);
            for(int i = 0; i < 4; ++i) {
                LoginControl.sendResetCode(email);
            }
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }

    }
}
