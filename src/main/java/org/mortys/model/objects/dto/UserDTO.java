package org.mortys.model.objects.dto;

public class UserDTO {
    private String username;
    private String email;
    String password;
    private String matrnr;
    private String firma;
    boolean isUnternehmer;


    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getMatrnr() { return matrnr; }

    public void setMatrnr(String matrnr) { this.matrnr = matrnr; }

    public String getFirma() { return firma; }

    public void setFirma(String firma) { this.firma = firma; }

    public boolean isUnternehmer() { return isUnternehmer; }

    public void setUnternehmer(boolean unternehmer) { isUnternehmer = unternehmer; }

}
