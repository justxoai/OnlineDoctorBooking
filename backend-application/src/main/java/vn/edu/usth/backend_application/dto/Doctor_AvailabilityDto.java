package vn.edu.usth.backend_application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor_AvailabilityDto {

    private int availability_id;
    private int doctor_id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate availability_date;

    private List<AvailabilitySlotDto> slots;
}
