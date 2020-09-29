package org.mortys.process.control.exception;

public class UserNotFoundException extends NoSuchUserOrPassword {

    public UserNotFoundException(String reason) {
        super(reason);
    }
}
