package vn.edu.usth.doconcall.Patient.Appointment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.Patient.Dashboard.Patient_Dashboard;
import vn.edu.usth.doconcall.Patient.HealthCheck.Patient_HealthCheck;
import vn.edu.usth.doconcall.Patient.List_Doctor.Patient_List_Doctor;
import vn.edu.usth.doconcall.Patient.Profile.Patient_Profile;
import vn.edu.usth.doconcall.Patient.Schedule.Patient_Schedule;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class PatientManageAppointment extends AppCompatActivity {

    private RecyclerView appointment_recycler;
    private Appointment_Adapter adapter;
    private List<Appointment_Items> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_appointment);

        DrawerLayout mDrawLayout = findViewById(R.id.list_appointment);

        ImageButton mImageView = findViewById(R.id.menu_button);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawLayout != null && !mDrawLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        fetch_user_information();

        // Recyclerview setup
        appointment_manage_setup();

        // SideBar function
        side_bar_function();
    }

    private void appointment_manage_setup() {
        appointment_recycler = findViewById(R.id.recycler_view_list_appointment);

        appointmentList = new ArrayList<>();

        fetch_appointment_list();

        adapter = new Appointment_Adapter(this, appointmentList);

        appointment_recycler.setAdapter(adapter);
        appointment_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetch_appointment_list() {
        SessionManager sessionManager = SessionManager.getInstance();
        String token = sessionManager.getToken();
        int patientId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && patientId != -1) {
            String authHeader = "Bearer " + token;
            ScheduleEventAPI scheduleEventService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);

            Call<List<ScheduleEventDto>> call = scheduleEventService.getByPatientId(authHeader, patientId);
            call.enqueue(new Callback<List<ScheduleEventDto>>() {
                @Override
                public void onResponse(Call<List<ScheduleEventDto>> call, Response<List<ScheduleEventDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ScheduleEventDto> schedule_response = response.body();

                        appointmentList.clear();
                        if (schedule_response.isEmpty()) {
                            TextView no_appointment = findViewById(R.id.no_appointments_text);
                            no_appointment.setVisibility(View.VISIBLE);
                            appointment_recycler.setVisibility(View.GONE);
                            return;
                        }

                        for (ScheduleEventDto dto : schedule_response) {
                            int doctor = dto.getDoctor_id();
                            int availability_id = dto.getDoctor_availability_id();
                            int slot_id = dto.getTime_slot_id();
                            int schedule_id = dto.getSchedule_id();

                            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
                            Call<Doctor_AvailabilityDto> doctorAvailabilityCall = doctorAvailabilityService.getAvailabilityById(authHeader, availability_id);

                            doctorAvailabilityCall.enqueue(new Callback<Doctor_AvailabilityDto>() {
                                @Override
                                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Doctor_AvailabilityDto doc_avail_response = response.body();
                                        String date = (doc_avail_response.getAvailability_date());

                                        for (AvailabilitySlotDto slot : doc_avail_response.getSlots()) {
                                            if (slot.getSlot_id() == slot_id) {
                                                String time = slot.getSlot_time();

                                                DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
                                                Call<DoctorDto> doctorCall = doctorService.getDoctorById(authHeader, doctor);
                                                doctorCall.enqueue(new Callback<DoctorDto>() {
                                                    @Override
                                                    public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                                                        if (response.isSuccessful() && response.body() != null) {
                                                            DoctorDto doctorDto = response.body();
                                                            String doctor_name = doctorDto.getName();

                                                            appointmentList.add(new Appointment_Items(
                                                                    date,
                                                                    "Time: " + time,
                                                                    "Doctor: " + doctor_name,
                                                                    doctor,
                                                                    availability_id,
                                                                    slot_id,
                                                                    schedule_id
                                                            ));
                                                            sortAppointmentList();

                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<DoctorDto> call, Throwable t) {
                                                        Log.e("GetDoctorName", "Error: " + t.getMessage(), t);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                    Log.e("GetAvailabilityDateTime", "Error: " + t.getMessage(), t);
                                }
                            });
                        }

                    } else {
                        Log.e("GetAppointmentList", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                    Log.e("GetAppointmentList", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("GetAppointmentList", "Missing token or userId");
        }
    }

    private void fetch_user_information() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int userId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && userId != -1) {
            String authHeader = "Bearer " + token;

            UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
            Call<UserDto> call = userService.getUserById(authHeader, userId);

            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDto user = response.body();
                        setTextWelcome(user);
                    } else {
                        Log.e("PatientListDoctorSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("PatientListDoctorSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("PatientListDoctorSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user) {
        TextView text = findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }
    private void side_bar_function() {
        // Dashboard
        LinearLayout dashboard_page = findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientManageAppointment.this, Patient_Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        // Schedule
        LinearLayout schedule_page = findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientManageAppointment.this, Patient_Schedule.class);
                startActivity(i);
                finish();
            }
        });

        // Health Check
        LinearLayout health_page = findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientManageAppointment.this, Patient_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });

        // Manage Appointment
        LinearLayout manage_page = findViewById(R.id.to_manage_appointment_page);
        manage_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PatientManageAppointment.this, PatientManageAppointment.class);
                startActivity(i);
                finish();
            }
        });

        // List Doctor
        LinearLayout doctor_page = findViewById(R.id.to_doctor_list_page);
        doctor_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientManageAppointment.this, Patient_List_Doctor.class);
                startActivity(i);
                finish();
            }
        });

        // Profile
        LinearLayout profile_page = findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PatientManageAppointment.this, Patient_Profile.class);
                startActivity(i);
                finish();
            }
        });

        // Log out
        LinearLayout log_out = findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(PatientManageAppointment.this);
            }
        });
    }

    private void sortAppointmentList() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        appointmentList.sort((a1, a2) -> {
            LocalDate d1 = LocalDate.parse(a1.getDate(), dateFormat);
            LocalDate d2 = LocalDate.parse(a2.getDate(), dateFormat);

            int dateCompare = d1.compareTo(d2);
            if (dateCompare == 0) {
                String t1Str = a1.getTime().replace("Time: ", "").trim();
                String t2Str = a2.getTime().replace("Time: ", "").trim();

                LocalTime t1 = LocalTime.parse(t1Str, timeFormat);
                LocalTime t2 = LocalTime.parse(t2Str, timeFormat);

                return t1.compareTo(t2);
            }
            return dateCompare;
        });
    }
}