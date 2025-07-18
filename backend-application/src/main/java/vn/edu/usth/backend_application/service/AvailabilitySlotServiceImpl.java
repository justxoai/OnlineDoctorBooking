package vn.edu.usth.backend_application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.usth.backend_application.dto.AvailabilitySlotDto;
import vn.edu.usth.backend_application.entity.AvailabilitySlot;
import vn.edu.usth.backend_application.entity.Doctor_Availability;
import vn.edu.usth.backend_application.mapper.AvailabilitySlotMapper;
import vn.edu.usth.backend_application.repository.AvailabilitySlotRepository;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AvailabilitySlotServiceImpl implements AvailabilitySlotService{

    private AvailabilitySlotRepository availabilitySlotRepository;

    @Override
    public AvailabilitySlotDto createAvailabilitySlot(AvailabilitySlotDto availabilitySlotDto) {

        AvailabilitySlot availabilitySlot = AvailabilitySlotMapper.mapToAvailabilitySlot(availabilitySlotDto);

        AvailabilitySlot savedAvailabilitySlot = availabilitySlotRepository.save(availabilitySlot);

        return AvailabilitySlotMapper.mapToAvailabilitySlotDto(savedAvailabilitySlot);
    }

    @Override
    public AvailabilitySlotDto getAvailabilitySlotById(Integer slot_id) {
        AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(slot_id)
                .orElseThrow(
                        () -> new ResolutionException("Slot not found with id " + slot_id));

        return AvailabilitySlotMapper.mapToAvailabilitySlotDto(availabilitySlot);
    }

    @Override
    public List<AvailabilitySlotDto> getAllAvailabilitySlots() {
        List<AvailabilitySlot> availabilitySlots = availabilitySlotRepository.findAll();
        return availabilitySlots.stream().map((availabilitySlot) -> AvailabilitySlotMapper.mapToAvailabilitySlotDto(availabilitySlot)).collect(Collectors.toList());
    }

    @Override
    public AvailabilitySlotDto updateAvailabilitySlot(Integer slot_id, AvailabilitySlotDto availabilitySlotDto) {
        AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(slot_id)
                .orElseThrow(
                        () -> new ResolutionException("Slot not found with id " + slot_id));

        availabilitySlot.setSlotTime(availabilitySlotDto.getSlot_time());
        availabilitySlot.setSlotStatus(availabilitySlotDto.getSlot_status());

        AvailabilitySlot savedaAvailabilitySlot = availabilitySlotRepository.save(availabilitySlot);

        return AvailabilitySlotMapper.mapToAvailabilitySlotDto(savedaAvailabilitySlot);
    }

    @Override
    public void deleteAvailabilitySlot(Integer slot_id) {
        AvailabilitySlot availabilitySlot = availabilitySlotRepository.findById(slot_id)
                .orElseThrow(
                        () -> new ResolutionException("Slot not found with id " + slot_id));

        availabilitySlotRepository.delete(availabilitySlot);
    }
}
