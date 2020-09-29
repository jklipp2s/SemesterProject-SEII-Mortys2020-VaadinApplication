package org.mortys.services.util;

public class PasswordResetData {

    final public static int LIMIT_VALID_VALUE = 10;
    final public static String LIMIT_VALID_UNIT = LimitUnit.MINUTES.toString();

    public enum LimitUnit {
        SECONDS, MINUTES, HOURS
    }
}
