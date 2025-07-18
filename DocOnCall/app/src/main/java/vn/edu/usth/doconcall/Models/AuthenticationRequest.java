package vn.edu.usth.doconcall.Models;

public class AuthenticationRequest {

    private String phoneNumber;

    private String password;

    public AuthenticationRequest(){}

    public AuthenticationRequest(String phoneNumber, String password){
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
