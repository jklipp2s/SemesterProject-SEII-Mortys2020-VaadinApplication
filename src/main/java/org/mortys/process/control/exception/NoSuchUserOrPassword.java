package org.mortys.process.control.exception;

public class NoSuchUserOrPassword extends Exception {
    private String reason = null;
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public NoSuchUserOrPassword(String reason){
        this.reason = reason;
    }
}
