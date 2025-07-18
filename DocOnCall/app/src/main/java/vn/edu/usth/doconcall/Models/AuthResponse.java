package vn.edu.usth.doconcall.Models;

public class AuthResponse {

    private String token;
    private String role;

    private int userId;

    private String userName;

    public String getToken() {
        return token;
    }

    public String getRole(){
        return role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
