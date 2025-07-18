package vn.edu.usth.doconcall.Doctor.HealthCheck.List_Patient;

public class Patient_Item {

    int id;
    String name;
    String gender;
    String dob;

    public Patient_Item(int id, String name, String gender, String dob){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }
}
