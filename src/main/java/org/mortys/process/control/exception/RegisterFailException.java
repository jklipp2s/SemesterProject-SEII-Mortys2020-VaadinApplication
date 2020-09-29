package org.mortys.process.control.exception;

public class RegisterFailException extends Exception {
    private String reason;

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }
}
