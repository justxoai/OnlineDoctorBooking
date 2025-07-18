package vn.edu.usth.doconcall.Patient.Dashboard.Fragment;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Patient.Dashboard.ListView.AppointmentAdapter;
import vn.edu.usth.doconcall.Patient.Dashboard.ListView.AppointmentItem;
import vn.edu.usth.doconcall.Patient.List_Doctor.Patient_List_Doctor;
import vn.edu.usth.doconcall.Patient.List_Doctor.RecyclerView.Doctor_Adapter;
import vn.edu.usth.doconcall.Patient.List_Doctor.RecyclerView.Doctor_Items;
import vn.edu.usth.doconcall.Patient.Schedule.Patient_Schedule;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;

public class Patient_Dashboard_Fragment extends Fragment {

    private List<Doctor_Items> filter_items;
    private List<AppointmentItem> appointment_item;
    private Doctor_Adapter adapter;
    private RecyclerView eventRecyclerView;
    private AppointmentAdapter appointment_adapter;
    private String event_doctor_name, event_appointment_type, event_appointment_status;
    private String event_time;
    private LinearLayout textEventList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient__dashboard_, container, false);

        // Dashboard Fragment Function
        dashboard_function(v);

        // Setup Doctor RecyclerView
        doctor_recycler_view(v);

        // Setup Calendar
        calendar_setup_function(v);

        // Setup ListView
        RecyclerView_setup_function(v);

        return v;
    }

    private void RecyclerView_setup_function(View v) {
        eventRecyclerView = v.findViewById(R.id.eventRecyclerView_patient_dashboard);
        textEventList = v.findViewById(R.id.text_eventList);

        textEventList.setVisibility(View.VISIBLE);
        eventRecyclerView.setVisibility(View.GONE);

        appointment_item = new ArrayList<>();

        fetch_schedule_event();

        appointment_adapter = new AppointmentAdapter(v.getContext(), appointment_item);

        eventRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        eventRecyclerView.setAdapter(appointment_adapter);
    }

    private void fetch_schedule_event() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int patientId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && patientId != -1) {
            String authHeader = "Bearer " + token;

            ScheduleEventAPI scheduleService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<List<ScheduleEventDto>> schedule_call = scheduleService.getByPatientId(authHeader, patientId);
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

                                        if (parsedDate.isAfter(yesterday) && parsedDate.isBefore(tomorrow)){
                                            for (AvailabilitySlotDto slot : doc_avail_response.getSlots()) {
                                                if (slot.getSlot_id() == schedule.getTime_slot_id()) {
                                                    event_time = slot.getSlot_time();

                                                    DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
                                                    Call<DoctorDto> doctorCall = doctorService.getDoctorById(authHeader, schedule.getDoctor_id());
                                                    doctorCall.enqueue(new Callback<DoctorDto>() {
                                                        @Override
                                                        public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                                                            if (response.isSuccessful() && response.body() != null) {
                                                                DoctorDto doctorDto = response.body();
                                                                event_doctor_name = doctorDto.getName();

                                                                textEventList.setVisibility(View.GONE);
                                                                eventRecyclerView.setVisibility(View.VISIBLE);

                                                                appointment_item.add(new AppointmentItem(event_time, event_doctor_name, event_appointment_type, event_appointment_status));
                                                            } else {
                                                                Log.e("SetDoctorName", "No response body");
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<DoctorDto> call, Throwable t) {
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
        TextView month_year = v.findViewById(R.id.monthYearTV_patient_dashboard);
        month_year.setText(weekdayDateMonthFromDate(LocalDate.now()));
    }

    private void doctor_recycler_view(View v) {
        RecyclerView doctor_recycler = v.findViewById(R.id.recycler_view_doctor_dashboard);

        filter_items = new ArrayList<Doctor_Items>();

        fetch_doctor_list(null);

        adapter = new Doctor_Adapter(requireContext(), filter_items);

        doctor_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        doctor_recycler.setAdapter(adapter);
    }

    private void fetch_doctor_list(String query) {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
            Call<List<DoctorDto>> call = doctorService.getAllDoctors(authHeader, query);
            call.enqueue(new Callback<List<DoctorDto>>() {
                @Override
                public void onResponse(Call<List<DoctorDto>> call, Response<List<DoctorDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<DoctorDto> doctors = response.body();

                        Collections.shuffle(doctors);

                        List<DoctorDto> randomThree = doctors.size() > 3
                                ? doctors.subList(0, 3)
                                : doctors; // if less than 3, use all

                        filter_items.clear();
                        for (DoctorDto doctor : randomThree) {
                            filter_items.add(new Doctor_Items(
                                    doctor.getId(),
                                    doctor.getName(),
                                    doctor.getSpecialization(),
                                    R.drawable.profile
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("PatientDashBoardListDoctor", "No Doctot Found");
                    }
                }

                @Override
                public void onFailure(Call<List<DoctorDto>> call, Throwable t) {
                    Log.e("PatientDashBoardListDoctor", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("PatientDashBoardListDoctor", "Missing token");
        }
    }

    private void dashboard_function(View v) {

        RelativeLayout see_all_doctor = v.findViewById(R.id.see_all_doctor);
        see_all_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Patient_List_Doctor.class);
                startActivity(i);
            }
        });
    }
}