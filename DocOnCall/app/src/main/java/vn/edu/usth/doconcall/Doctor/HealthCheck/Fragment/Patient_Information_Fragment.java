package vn.edu.usth.doconcall.Doctor.HealthCheck.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.R;

public class Patient_Information_Fragment extends Fragment {

    private TextView nameView, genderView, dob;

    private String patientName, patientGender, patientDob, event_appointment_type, event_appointment_status, date, time;
    private int patientId;
    private LocalTime scheduleTime;
    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout
        View v = inflater.inflate(R.layout.fragment_patient__information_, container, false);

        // Set information
        nameView = v.findViewById(R.id.patient_information_name);
        genderView = v.findViewById(R.id.patient_information_gender);
        dob = v.findViewById(R.id.patient_information_dob);

        bundle = getArguments();
        patientId = bundle.getInt("Patient Id");
        patientName = bundle.getString("Patient Name");
        patientGender = bundle.getString("Patient Gender");
        patientDob = bundle.getString("Patient Dob");

        nameView.setText("Name: " + patientName);
        genderView.setText("Gender: " + patientGender);
        dob.setText("Dob: " + patientDob);

        // Button Function
        patient_information_function(v);

        // Fetch Appointment Information
        fetch_appointment_information(v);

        return v;
    }

    private void fetch_appointment_information(View v) {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = sessionManager.getUserId();

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            ScheduleEventAPI scheduleService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<List<ScheduleEventDto>> schedule_call = scheduleService.getByDoctorId(authHeader, doctorId);
            schedule_call.enqueue(new Callback<List<ScheduleEventDto>>() {
                @Override
                public void onResponse(Call<List<ScheduleEventDto>> call, Response<List<ScheduleEventDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ScheduleEventDto> schedules = response.body();

                        for (ScheduleEventDto schedule : schedules) {

                            if (schedule.getAppointment_style().equals("VIRTUAL_MEETING")) {
                                event_appointment_type = "Online Meeting";
                                TextView appointment_type = v.findViewById(R.id.appointment_type);
                                appointment_type.setText("Appointment Type: " + event_appointment_type);
                            } else {
                                event_appointment_type = "In-Person Meeting";
                                TextView appointment_type = v.findViewById(R.id.appointment_type);
                                appointment_type.setText("Appointment Type: " + event_appointment_type);
                            }

                            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
                            Call<Doctor_AvailabilityDto> doctorAvailabilityCall = doctorAvailabilityService.getAvailabilityById(authHeader, schedule.getDoctor_availability_id());
                            doctorAvailabilityCall.enqueue(new Callback<Doctor_AvailabilityDto>() {
                                @Override
                                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Doctor_AvailabilityDto doc_avail_response = response.body();

                                        date = formatDateString(doc_avail_response.getAvailability_date());
                                        TextView appointment_date = v.findViewById(R.id.appointment_date_select);
                                        appointment_date.setText("Date: " + date);

                                        for (AvailabilitySlotDto slotDto : doc_avail_response.getSlots()) {
                                            if (schedule.getTime_slot_id() == slotDto.getSlot_id()) {
                                                time = slotDto.getSlot_time();

                                                scheduleTime = LocalTime.parse(time);

                                                TextView appointment_time = v.findViewById(R.id.appointment_time_select);
                                                appointment_time.setText("Time: " + time);
                                            }
                                        }

                                    } else {
                                        Log.e("SetDate", "No response body");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                    Log.e("SetDateAndTime", "Error: " + t.getMessage(), t);
                                }
                            });

                        }

                    } else {
                        Log.e("PatientDashBoardSchedule", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                    Log.e("PatientDashBoardSchedule", "Error: " + t.getMessage(), t);
                }
            });


        } else {
            Log.e("PatientInformationFragment", "Missing Token");
        }

    }

    private void patient_information_function(View v) {
        // Back
        ImageButton back_button = v.findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private static String formatDateString(String date) {
        LocalDate parsed = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return parsed.format(formatter);
    }
}