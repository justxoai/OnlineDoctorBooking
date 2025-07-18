package vn.edu.usth.backend_application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.usth.backend_application.enums.SlotStatus;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "availability_slot")
public class AvailabilitySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int slotId;

    @Column(name = "Availability_time")
    private LocalTime slotTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    @ManyToOne
    @JoinColumn(name = "Availability_id")
    private Doctor_Availability availabilityId;

}
