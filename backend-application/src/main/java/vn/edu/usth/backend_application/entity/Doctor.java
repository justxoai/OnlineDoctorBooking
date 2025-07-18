package vn.edu.usth.backend_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="doctor_table")
public class Doctor {

    @Id
    private int doctorId;

    @Column(name = "Specialization")
    private String specialization;

    @Column(name = "Year_experience")
    private String yearExperience;

    @Column(name = "Work_process")
    private String workProcess;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doctor_Availability> availabilities = new ArrayList<>();
}
