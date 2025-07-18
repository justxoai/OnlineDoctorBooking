package vn.edu.usth.doconcall.Patient.HealthCheck.SelectDoctor;

public class Select_Doctor_Items {

    int id;
    String name;
    String specialization;
    int doctor_image;

    public Select_Doctor_Items(int id, String name, String specialization, int doctor_image) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.doctor_image = doctor_image;
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

    public String getSpecialization() {
        return specialization;
    }

    public int getDoctor_image() {
        return doctor_image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setDoctor_image(int doctor_image) {
        this.doctor_image = doctor_image;
    }
}
