package vn.edu.usth.backend_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="patient_table")
public class Patient {

    @Id
    private int patientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User userId;
}
