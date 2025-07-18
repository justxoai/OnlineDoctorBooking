package vn.edu.usth.doconcall.Models;

public class RegisterRequest {

    private String username;

    private String password;

    private String phone;

    private String date;

    private String gender;

    private String role;

    private String specialization;

    private String year_experience;

    private String work_process;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password, String phone, String date, String gender, String role){
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.date = date;
        this.gender = gender;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getYear_experience() {
        return year_experience;
    }

    public void setYear_experience(String year_experience) {
        this.year_experience = year_experience;
    }

    public String getWork_process() {
        return work_process;
    }

    public void setWork_process(String work_process) {
        this.work_process = work_process;
    }
}
