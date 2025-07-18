package vn.edu.usth.backend_application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.usth.backend_application.enums.SlotStatus;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySlotDto {

    private int slot_id;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime slot_time;
    private SlotStatus slot_status;

}
