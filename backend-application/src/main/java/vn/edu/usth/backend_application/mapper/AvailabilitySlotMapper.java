package vn.edu.usth.backend_application.mapper;

import vn.edu.usth.backend_application.dto.AvailabilitySlotDto;
import vn.edu.usth.backend_application.entity.AvailabilitySlot;
import vn.edu.usth.backend_application.entity.Doctor_Availability;

public class AvailabilitySlotMapper {

    public static AvailabilitySlotDto mapToAvailabilitySlotDto(AvailabilitySlot availabilitySlot) {
        return new AvailabilitySlotDto(
                availabilitySlot.getSlotId(),
                availabilitySlot.getSlotTime(),
                availabilitySlot.getSlotStatus()
        );
    }

    public static AvailabilitySlot mapToAvailabilitySlot(AvailabilitySlotDto dto) {

        return new AvailabilitySlot(
                dto.getSlot_id(),
                dto.getSlot_time(),
                dto.getSlot_status(),
                null
        );
    }
}
