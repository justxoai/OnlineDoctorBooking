package vn.edu.usth.backend_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.usth.backend_application.enums.Appointment_Status;
import vn.edu.usth.backend_application.enums.Appointment_Style;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "new_schedule_event")
public class Schedule_Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scheduleId;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patientId;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctorId;

    @ManyToOne
    @JoinColumn(name = "availability_id")
    private Doctor_Availability doctorAvailabilityId;

    @ManyToOne
    @JoinColumn(name = "Slot_id")
    private AvailabilitySlot slotId;

    @Column(name = "Appointment_style")
    private Appointment_Style appointmentStyle;

    @Column(name = "Appoinment_status")
    private Appointment_Status appointmentStatus;

}
