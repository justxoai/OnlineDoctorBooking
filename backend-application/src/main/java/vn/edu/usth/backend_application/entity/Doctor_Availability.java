package vn.edu.usth.backend_application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_availability_table")
public class Doctor_Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorAvailabilityId;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "Availability_date")
    private LocalDate availabilityDate;

    @OneToMany(mappedBy = "availabilityId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailabilitySlot> slots = new ArrayList<>();

}
