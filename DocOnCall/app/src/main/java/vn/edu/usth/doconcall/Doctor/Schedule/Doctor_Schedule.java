package vn.edu.usth.doconcall.Doctor.Schedule;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Appointment.DoctorManageAppointment;
import vn.edu.usth.doconcall.Doctor.Dashboard.Doctor_Dashboard;
import vn.edu.usth.doconcall.Doctor.HealthCheck.Doctor_HealthCheck;
import vn.edu.usth.doconcall.Doctor.Profile.Doctor_Profile;
import vn.edu.usth.doconcall.Doctor.Schedule.Week_Event.Doctor_Event;
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
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class Doctor_Schedule extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_schedule);

        // ViewPager: Change Dashboard and Notification Fragment
        mViewPager = findViewById(R.id.doctor_schedule_view_pager);
        mViewPager.setVisibility(View.GONE);

        // Bottom Navigator
        bottomNavigationView = findViewById(R.id.schedule_bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);

        // Floating Button
        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

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
                fab.setVisibility(View.VISIBLE);
            }
        }, 2000);


        Doctor_Schedule_Adapter adapter = new Doctor_Schedule_Adapter(getSupportFragmentManager(), getLifecycle());
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
        mDrawLayout = findViewById(R.id.doctor_schedule_activity);

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

        // Sidebar Function
        side_bar_function();

        // Schedule Function
        schedule_function();

        // Fetch user information
        fetch_user_information();

        // Fetch Schedule
        fetch_doctor_schedule();

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
                        Log.e("DoctorScheduleSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("DoctorScheduleSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("DoctorScheduleSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user) {
        TextView text = findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }

    private void schedule_function() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Schedule.this, DoctorManageAppointment.class);
                startActivity(i);
            }
        });
    }

    private void fetch_doctor_schedule() {
        Doctor_Event.eventsList.clear();

        SessionManager sessionManager = SessionManager.getInstance();
        String token = sessionManager.getToken();
        int doctorId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            ScheduleEventAPI scheduleService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<List<ScheduleEventDto>> call = scheduleService.getByDoctorId(authHeader, doctorId);

            call.enqueue(new Callback<List<ScheduleEventDto>>() {
                @Override
                public void onResponse(Call<List<ScheduleEventDto>> call, Response<List<ScheduleEventDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ScheduleEventDto> schedules = response.body();

                        for (ScheduleEventDto schedule : schedules) {
                            String eventType = schedule.getAppointment_style().equals("VIRTUAL_MEETING")
                                    ? "Online Meeting" : "In-Person Meeting";

                            // Get patient name
                            UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
                            Call<UserDto> userCall = userService.getUserById(authHeader, schedule.getPatient_id());

                            userCall.enqueue(new Callback<UserDto>() {
                                @Override
                                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        String patientName = response.body().getName();

                                        // Get availability info
                                        DoctorAvailabilityAPI availService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
                                        Call<Doctor_AvailabilityDto> availCall = availService.getAvailabilityById(authHeader, schedule.getDoctor_availability_id());

                                        availCall.enqueue(new Callback<Doctor_AvailabilityDto>() {
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
                                                        Log.e("DoctorSchedule", "Slot not found for schedule");
                                                        return;
                                                    }

                                                    // Determine status
                                                    LocalDate today = LocalDate.now();
                                                    LocalTime nowTime = LocalTime.now();
                                                    String status;

                                                    if (schedule.getAppointment_status().equalsIgnoreCase("CANCELLED")) {
                                                        status = "Cancel";
                                                    } else if (eventDate.isAfter(today)) {
                                                        status = "Upcoming";
                                                    } else if (eventDate.isBefore(today)) {
                                                        status = "Complete";
                                                    } else {
                                                        status = (eventTime.isAfter(nowTime)) ? "Upcoming" : "Complete";
                                                    }

                                                    Doctor_Event event = new Doctor_Event(
                                                            eventDate,
                                                            eventTime,
                                                            patientName,
                                                            eventType,
                                                            status
                                                    );

                                                    Doctor_Event.eventsList.add(event);
                                                } else {
                                                    Log.e("DoctorAvail", "Availability data missing");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                                Log.e("DoctorAvail", "Error: " + t.getMessage(), t);
                                            }
                                        });

                                    } else {
                                        Log.e("PatientInfo", "Patient info not found");
                                    }
                                }

                                @Override
                                public void onFailure(Call<UserDto> call, Throwable t) {
                                    Log.e("PatientInfo", "Error: " + t.getMessage(), t);
                                }
                            });
                        }
                    } else {
                        Log.e("DoctorSchedule", "No schedules found");
                    }
                }

                @Override
                public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                    Log.e("DoctorSchedule", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("DoctorSchedule", "Missing token or doctor ID");
        }
    }

    private void side_bar_function() {
        // Dashboard
        LinearLayout dashboard_page = findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Schedule.this, Doctor_Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        // Schedule
        LinearLayout schedule_page = findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Schedule.this, Doctor_Schedule.class);
                startActivity(i);
                finish();
            }
        });

        // Health Check
        LinearLayout health_page = findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Schedule.this, Doctor_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });

        // Manage Appointment
        LinearLayout manage_appointment = findViewById(R.id.to_manage_appointment_page);
        manage_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Schedule.this, DoctorManageAppointment.class);
                startActivity(i);
            }
        });

        // Profile
        LinearLayout profile_page = findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Schedule.this, Doctor_Profile.class);
                startActivity(i);
                finish();
            }
        });

        // Log out
        LinearLayout log_out = findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(Doctor_Schedule.this);
            }
        });
    }

}