package vn.edu.usth.doconcall.Models;

public class DoctorDto {

    private int id;
    private String name;
    private String phoneNumber;
    private String birthday;
    private String gender;

    private String specialization;
    private String year_experience;
    private String work_experience;

    public DoctorDto(){
    }

    public DoctorDto(int id, String name, String phoneNumber, String birthday, String gender, String specialization, String year_experience, String work_experience){
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.gender = gender;
        this.specialization = specialization;
        this.year_experience = year_experience;
        this.work_experience = work_experience;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getWork_experience() {
        return work_experience;
    }

    public void setWork_experience(String work_experience) {
        this.work_experience = work_experience;
    }
}
