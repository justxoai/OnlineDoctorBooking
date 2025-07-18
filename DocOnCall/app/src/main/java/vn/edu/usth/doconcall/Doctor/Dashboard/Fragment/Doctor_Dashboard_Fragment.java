package vn.edu.usth.doconcall.Doctor.Dashboard.Fragment;

import static vn.edu.usth.doconcall.Utils.Calendar_Utils.weekdayDateMonthFromDate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Dashboard.ListView.Appointment_Adapter;
import vn.edu.usth.doconcall.Doctor.Dashboard.ListView.Appointment_Item;
import vn.edu.usth.doconcall.Doctor.HealthCheck.Doctor_HealthCheck;
import vn.edu.usth.doconcall.Doctor.HealthCheck.List_Patient.Patient_Adapter;
import vn.edu.usth.doconcall.Doctor.HealthCheck.List_Patient.Patient_Item;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;

public class Doctor_Dashboard_Fragment extends Fragment {

    private List<Patient_Item> items;
    private List<Appointment_Item> appointment_item;

    private String event_patient_name, event_appointment_type, event_appointment_status;
    private String event_time;

    private RecyclerView eventRecyclerView;
    private LinearLayout textEventList;
    private Appointment_Adapter appointment_adapter;
    private Patient_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout
        View v = inflater.inflate(R.layout.fragment_doctor__dashboard_, container, false);

        // Dashboard Fragment Function
        dash_board_function(v);

        // RecyclerView List Patient in HealthCheck
        patient_recycle_view(v);

        // Setup Calendar
        calendar_setup_function(v);

        // Setup ListView
        RecyclerView_setup_function(v);

        return v;
    }

    private void RecyclerView_setup_function(View v) {

        eventRecyclerView = v.findViewById(R.id.eventRecyclerView_dashboard);
        textEventList = v.findViewById(R.id.text_eventList);

        textEventList.setVisibility(View.VISIBLE);
        eventRecyclerView.setVisibility(View.GONE);

        appointment_item = new ArrayList<>();

        fetch_schedule_event();

        appointment_adapter = new Appointment_Adapter(v.getContext(), appointment_item);

        eventRecyclerView.setAdapter(appointment_adapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
    }

    private void fetch_schedule_event() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && doctorId != -1) {
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
                            } else {
                                event_appointment_type = "In-Person Meeting";
                            }

                            // Set appointment status
                            event_appointment_status = schedule.getAppointment_status();

                            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
                            Call<Doctor_AvailabilityDto> doctorAvailabilityCall = doctorAvailabilityService.getAvailabilityById(authHeader, schedule.getDoctor_availability_id());
                            doctorAvailabilityCall.enqueue(new Callback<Doctor_AvailabilityDto>() {
                                @Override
                                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Doctor_AvailabilityDto doc_avail_response = response.body();

                                        String date = doc_avail_response.getAvailability_date();

                                        LocalDate parsedDate = LocalDate.parse(date);
                                        LocalDate today = LocalDate.now();
                                        LocalDate tomorrow = today.plusDays(1);
                                        LocalDate yesterday = today.minusDays(1);

                                        if (parsedDate.isAfter(yesterday) && parsedDate.isBefore(tomorrow)) {
                                            for (AvailabilitySlotDto slot : doc_avail_response.getSlots()) {
                                                if (slot.getSlot_id() == schedule.getTime_slot_id()) {
                                                    event_time = slot.getSlot_time();

                                                    UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
                                                    Call<UserDto> userCall = userService.getUserById(authHeader, schedule.getPatient_id());
                                                    userCall.enqueue(new Callback<UserDto>() {
                                                        @Override
                                                        public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                                                            if (response.isSuccessful() && response.body() != null) {
                                                                UserDto userDto = response.body();
                                                                event_patient_name = userDto.getName();

                                                                textEventList.setVisibility(View.GONE);
                                                                eventRecyclerView.setVisibility(View.VISIBLE);

                                                                appointment_item.add(new Appointment_Item(event_time, event_patient_name, event_appointment_type, event_appointment_status));
                                                            } else {
                                                                Log.e("SetDoctorName", "No response body");
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<UserDto> call, Throwable t) {
                                                            Log.e("PatientSchedule", "Error: " + t.getMessage(), t);
                                                        }
                                                    });
                                                }
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

                        appointment_adapter.notifyDataSetChanged();

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
            Log.e("PatientDashBoardSchedule", "Missing token or userId");

        }
    }

    private void calendar_setup_function(View v) {
        // Month and year
        TextView month_year = v.findViewById(R.id.monthYearTV_doctor_dashboard);
        month_year.setText(weekdayDateMonthFromDate(LocalDate.now()));
    }

    private void patient_recycle_view(View v) {
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_patient_dashboard);

        items = new ArrayList<Patient_Item>();

        fetch_patient_list();

        adapter = new Patient_Adapter(requireContext(), items);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

    }

    // Sửa lại ở đây:
    // Get all schedule, sau đó store id của patient, lọc trùng, show 3 người trong danh sách
    private void fetch_patient_list() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            ScheduleEventAPI scheduleEventService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<List<ScheduleEventDto>> schdedule_call = scheduleEventService.getByDoctorId(authHeader, doctorId);
            schdedule_call.enqueue(new Callback<List<ScheduleEventDto>>() {
                @Override
                public void onResponse(Call<List<ScheduleEventDto>> call, Response<List<ScheduleEventDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        List<ScheduleEventDto> scheduleEventDtos = response.body();

                        int count = 0;

                        for (ScheduleEventDto scheduleEventDto : scheduleEventDtos) {

                            if (count >= 3) break;

                            int patientId = scheduleEventDto.getPatient_id();

                            UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
                            Call<UserDto> user_call = userService.getUserById(authHeader, patientId);
                            user_call.enqueue(new Callback<UserDto>() {
                                @Override
                                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        UserDto user_response = response.body();

                                        String patient_gender;
                                        if (user_response.getGender() == "MALE") {
                                            patient_gender = "Male";
                                        } else {
                                            patient_gender = "Female";
                                        }

                                        items.add(new Patient_Item(patientId, user_response.getName(), patient_gender, formatDateString(user_response.getBirthday())));


                                        adapter.notifyDataSetChanged();
                                    } else {
                                        Log.e("ListPatient", "Missing response or body");
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserDto> call, Throwable t) {
                                    Log.e("ListPatient", "Error: " + t.getMessage(), t);
                                }
                            });
                        }
                    } else {
                        Log.e("ListPatient", "Missing response or body");
                    }
                }

                @Override
                public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                    Log.e("ListPatient", "Error: " + t.getMessage(), t);
                }
            });


        } else {
            Log.e("DoctorDashboardFragment", "Missing token or userId");
        }
    }

    private void dash_board_function(View v) {
        RelativeLayout see_all_patient = v.findViewById(R.id.see_all_patient);
        see_all_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Doctor_HealthCheck.class);
                startActivity(i);
            }
        });
    }

    private static String formatDateString(String date) {
        LocalDate parsed = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return parsed.format(formatter);
    }
}