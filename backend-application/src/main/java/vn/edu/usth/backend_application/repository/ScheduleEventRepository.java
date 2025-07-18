package vn.edu.usth.backend_application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.usth.backend_application.enums.Appointment_Status;
import vn.edu.usth.backend_application.entity.Doctor;
import vn.edu.usth.backend_application.entity.Doctor_Availability;
import vn.edu.usth.backend_application.entity.Patient;
import vn.edu.usth.backend_application.entity.Schedule_Event;

import java.util.List;
import java.util.Optional;

public interface ScheduleEventRepository extends JpaRepository<Schedule_Event, Integer> {

    List<Schedule_Event> findByPatientId(Patient patientId);

    List<Schedule_Event> findByDoctorId(Doctor doctorId);

    Optional<Schedule_Event> findById(int Id);

    List<Schedule_Event> findByAppointmentStatus(Appointment_Status appointment_status);

    List<Schedule_Event> findByDoctorAvailabilityId(Doctor_Availability doctorAvailabilityId);

}
