package vn.edu.usth.doconcall.Patient.Schedule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDate;
import java.time.LocalTime;
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
import vn.edu.usth.doconcall.Patient.Appointment.PatientManageAppointment;
import vn.edu.usth.doconcall.Patient.Dashboard.Patient_Dashboard;
import vn.edu.usth.doconcall.Patient.HealthCheck.Patient_HealthCheck;
import vn.edu.usth.doconcall.Patient.List_Doctor.Patient_List_Doctor;
import vn.edu.usth.doconcall.Patient.Profile.Patient_Profile;
import vn.edu.usth.doconcall.Patient.Schedule.Week_Event.Patient_Event;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class Patient_Schedule extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_schedule);

        // ViewPager: Change Dashboard and Notification Fragment
        mViewPager = findViewById(R.id.patient_schedule_view_pager);
        mViewPager.setVisibility(View.GONE);

        // Bottom Navigator
        bottomNavigationView = findViewById(R.id.schedule_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        // Loading
        RelativeLayout loading_layout = findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading_layout.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        }, 2000);

        Patient_Schedule_Adapter adapter = new Patient_Schedule_Adapter(getSupportFragmentManager(), getLifecycle());
        mViewPager.setAdapter(adapter);
        mViewPager.setUserInputEnabled(false);

        // ViewPager2 setup Function
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.month_calendar).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.week_calendar).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.daily_calendar).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        // BottomNavigation setup Function
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.month_calendar) {
                    mViewPager.setCurrentItem(0, true);
                    return true;
                }
                if (item.getItemId() == R.id.week_calendar) {
                    mViewPager.setCurrentItem(1, true);
                    return true;
                }
                if (item.getItemId() == R.id.daily_calendar) {
                    mViewPager.setCurrentItem(2, true);
                    return true;
                }
                return false;
            }
        });

        // Side navigate
        mDrawLayout = findViewById(R.id.patient_schedule_activity);

        // Function to open Side-menu
        ImageButton mImageView = findViewById(R.id.menu_button);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawLayout != null && !mDrawLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Sidebar function
        side_bar_function();

        // Fetch user information
        fetch_user_information();

        fetch_patient_schedule();

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
                        Log.e("PatientScheduleSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("PatientScheduleSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("PatientScheduleSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user) {
        TextView text = findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }

    private void fetch_patient_schedule() {
        Patient_Event.eventsList.clear();

        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int patientId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && patientId != -1) {
            String authHeader = "Bearer " + token;

            ScheduleEventAPI scheduleService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<List<ScheduleEventDto>> call = scheduleService.getByPatientId(authHeader, patientId);
            call.enqueue(new Callback<List<ScheduleEventDto>>() {
                @Override
                public void onResponse(Call<List<ScheduleEventDto>> call, Response<List<ScheduleEventDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ScheduleEventDto> schedules = response.body();

                        for (ScheduleEventDto schedule : schedules) {

                            String eventType = schedule.getAppointment_style().equals("VIRTUAL_MEETING")
                                    ? "Online Meeting" : "In-Person Meeting";

                            DoctorAvailabilityAPI availabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
                            Call<Doctor_AvailabilityDto> availabilityCall = availabilityService.getAvailabilityById(authHeader, schedule.getDoctor_availability_id());
                            availabilityCall.enqueue(new Callback<Doctor_AvailabilityDto>() {
                                @Override
                                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Doctor_AvailabilityDto availability = response.body();
                                        LocalDate eventDate = LocalDate.parse(availability.getAvailability_date().toString());
                                        LocalTime eventTime = null;

                                        for (AvailabilitySlotDto slot : availability.getSlots()) {
                                            if (slot.getSlot_id() == schedule.getTime_slot_id()) {
                                                eventTime = LocalTime.parse(slot.getSlot_time().toString());
                                                break;
                                            }
                                        }

                                        if (eventTime == null) {
                                            Log.e("FetchSchedule", "Time slot not found");
                                            return;
                                        }

                                        LocalDate today = LocalDate.now();
                                        LocalTime nowTime = LocalTime.now();

                                        String status;

                                        if (schedule.getAppointment_status().equals("CANCELLED")) {
                                            status = "Cancel";
                                        } else if (schedule.getAppointment_status().equals("SCHEDULED")) {
                                            status = "Upcoming";
                                        } else if (schedule.getAppointment_status().equals("COMPLETED")) {
                                            status = "Complete";
                                        } else {
                                            if (eventDate.isAfter(today)) {
                                                status = "Upcoming";
                                                fetch_update_schedule(schedule.getSchedule_id(), schedule.getAppointment_style(), "SCHEDULED");
                                            } else if (eventDate.isBefore(today)) {
                                                status = "Complete";
                                                fetch_update_schedule(schedule.getSchedule_id(), schedule.getAppointment_style(), "COMPLETED");
                                            } else {
                                                if (eventTime.isAfter(nowTime)) {
                                                    status = "Upcoming";
                                                    fetch_update_schedule(schedule.getSchedule_id(), schedule.getAppointment_style(), "SCHEDULED");
                                                } else {
                                                    status = "Complete";
                                                    fetch_update_schedule(schedule.getSchedule_id(), schedule.getAppointment_style(), "COMPLETED");
                                                }
                                            }
                                        }

                                        // Fetch Doctor Info (final nested call)
                                        DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
                                        Call<DoctorDto> doctorCall = doctorService.getDoctorById(authHeader, schedule.getDoctor_id());

                                        // Local copies inside
                                        LocalDate finalEventDate = eventDate;
                                        LocalTime finalEventTime = eventTime;

                                        doctorCall.enqueue(new Callback<DoctorDto>() {
                                            @Override
                                            public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    String doctorName = response.body().getName();

                                                    // All data is ready
                                                    Patient_Event event = new Patient_Event(
                                                            finalEventDate,
                                                            finalEventTime,
                                                            doctorName,
                                                            eventType,
                                                            status
                                                    );

                                                    Patient_Event.eventsList.add(event);

                                                } else {
                                                    Log.e("DoctorInfo", "Doctor info missing");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<DoctorDto> call, Throwable t) {
                                                Log.e("DoctorFetch", "Error: " + t.getMessage(), t);
                                            }
                                        });
                                    } else {
                                        Log.e("AvailabilityInfo", "No response body");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                    Log.e("AvailabilityFetch", "Error: " + t.getMessage(), t);
                                }
                            });
                        }

                    } else {
                        Log.e("ScheduleFetch", "No response body or failure");
                    }
                }

                @Override
                public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                    Log.e("ScheduleFetch", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("FetchSchedule", "Missing token or user ID");
        }
    }

    private void fetch_update_schedule(int schedule_id, String new_appointment_type, String new_appointment_status) {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            ScheduleEventDto scheduleEventDto = new ScheduleEventDto();
            scheduleEventDto.setSchedule_id(0);
            scheduleEventDto.setPatient_id(0);
            scheduleEventDto.setDoctor_id(0);
            scheduleEventDto.setDoctor_availability_id(0);
            scheduleEventDto.setTime_slot_id(0);
            scheduleEventDto.setAppointment_style(new_appointment_type);
            scheduleEventDto.setAppointment_status(new_appointment_status);

            ScheduleEventAPI scheduleService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<ScheduleEventDto> call = scheduleService.updateScheduleEvent(authHeader, schedule_id, scheduleEventDto);
            call.enqueue(new Callback<ScheduleEventDto>() {
                @Override
                public void onResponse(Call<ScheduleEventDto> call, Response<ScheduleEventDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("UpdateSchedule", "Success");
                    } else {
                        Log.e("UpdateSchedule", "Missing response body");
                    }
                }

                @Override
                public void onFailure(Call<ScheduleEventDto> call, Throwable t) {
                    Log.e("UpdateSchedule", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("UpdateSchedule", "Missing token");
        }
    }

    private void side_bar_function() {
        // Dashboard
        LinearLayout dashboard_page = findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Schedule.this, Patient_Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        // Schedule
        LinearLayout schedule_page = findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Schedule.this, Patient_Schedule.class);
                startActivity(i);
                finish();
            }
        });

        // Health Check
        LinearLayout health_page = findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Schedule.this, Patient_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });

        // Manage Appointment
        LinearLayout manage_page = findViewById(R.id.to_manage_appointment_page);
        manage_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Patient_Schedule.this, PatientManageAppointment.class);
                startActivity(i);
                finish();
            }
        });

        // Consult Doctor
        LinearLayout doctor_page = findViewById(R.id.to_doctor_list_page);
        doctor_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Schedule.this, Patient_List_Doctor.class);
                startActivity(i);
                finish();
            }
        });

        // Profile
        LinearLayout profile_page = findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Schedule.this, Patient_Profile.class);
                startActivity(i);
                finish();
            }
        });

        // Logout
        LinearLayout log_out = findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(Patient_Schedule.this);
            }
        });
    }

}