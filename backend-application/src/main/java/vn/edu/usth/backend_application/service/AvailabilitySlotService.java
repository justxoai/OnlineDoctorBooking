package vn.edu.usth.backend_application.service;

import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.AvailabilitySlotDto;

import java.util.List;

@Service
public interface AvailabilitySlotService {

    // Create AvailabilitySlot
    AvailabilitySlotDto createAvailabilitySlot(AvailabilitySlotDto availabilitySlotDto);

    // Get Id of Slot
    AvailabilitySlotDto getAvailabilitySlotById(Integer slot_id);

    // Get all Slot
    List<AvailabilitySlotDto> getAllAvailabilitySlots();

    // Update Slot
    AvailabilitySlotDto updateAvailabilitySlot(Integer slot_id, AvailabilitySlotDto availabilitySlotDto);

    // Delete Slot
    void deleteAvailabilitySlot(Integer slot_id);

}
