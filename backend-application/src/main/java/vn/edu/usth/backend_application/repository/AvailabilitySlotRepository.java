package vn.edu.usth.backend_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.usth.backend_application.enums.SlotStatus;
import vn.edu.usth.backend_application.entity.AvailabilitySlot;
import vn.edu.usth.backend_application.entity.Doctor_Availability;

import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Integer> {

    List<AvailabilitySlot> findByAvailabilityId(Doctor_Availability availabilityId);

    List<AvailabilitySlot> findBySlotStatus(SlotStatus slotStatus);


}
