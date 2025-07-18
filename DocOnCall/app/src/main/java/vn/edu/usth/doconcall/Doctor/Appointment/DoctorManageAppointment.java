package vn.edu.usth.doconcall.Doctor.Appointment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Appointment.Adapter.ManageAppointmentAdapter;
import vn.edu.usth.doconcall.Doctor.Dashboard.Adapter.Doctor_Dashboard_Adapter;
import vn.edu.usth.doconcall.Doctor.Dashboard.Doctor_Dashboard;
import vn.edu.usth.doconcall.Doctor.HealthCheck.Doctor_HealthCheck;
import vn.edu.usth.doconcall.Doctor.Profile.Doctor_Profile;
import vn.edu.usth.doconcall.Doctor.Schedule.Doctor_Schedule;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class DoctorManageAppointment extends AppCompatActivity {

    private ViewPager2 mViewPager;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_manage_appointment);

        // ViewPager: Change Dashboard and Notification Fragment
        mViewPager = findViewById(R.id.doctor_create_manage_appointment_view_pager);

        // Bottom Navigator
        bottomNavigationView = findViewById(R.id.doctor_create_manage_appointment_bottom_navigation);

        ManageAppointmentAdapter adapter = new ManageAppointmentAdapter(getSupportFragmentManager(), getLifecycle());
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
                        bottomNavigationView.getMenu().findItem(R.id.create_appointment_page).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.manage_appointment_page).setChecked(true);
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
                if (item.getItemId() == R.id.create_appointment_page) {
                    mViewPager.setCurrentItem(0, true);
                    return true;
                }
                if (item.getItemId() == R.id.manage_appointment_page) {
                    mViewPager.setCurrentItem(1, true);
                    return true;
                }
                return false;
            }
        });

        // Side navigate
        mDrawLayout = findViewById(R.id.create_manage_appointment_activity);

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

        // Fetch user information
        fetch_user_information();

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
                        Log.e("DoctorDashboardSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("DoctorDashboardSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("DoctorDashboardSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user) {
        TextView text = findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }

    private boolean isTokenExpired(String token) {
        try {
            String[] split = token.split("\\.");
            String payload = split[1];
            String json = new String(android.util.Base64.decode(payload, android.util.Base64.URL_SAFE));
            JSONObject jsonObject = new JSONObject(json);
            long exp = jsonObject.getLong("exp");
            long currentTime = System.currentTimeMillis() / 1000;
            return currentTime > exp;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Treat as expired if error occurs
        }
    }

    private void side_bar_function() {
        // Dashboard
        LinearLayout dashboard_page = findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorManageAppointment.this, Doctor_Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        // Schedule
        LinearLayout schedule_page = findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorManageAppointment.this, Doctor_Schedule.class);
                startActivity(i);
                finish();
            }
        });

        // Health Check
        LinearLayout health_check = findViewById(R.id.to_health_check_page);
        health_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorManageAppointment.this, Doctor_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });

        // Manage Appointment
        LinearLayout manage_appointment = findViewById(R.id.to_manage_appointment_page);
        manage_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorManageAppointment.this, DoctorManageAppointment.class);
                startActivity(i);
            }
        });

        // Profile
        LinearLayout profile_page = findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DoctorManageAppointment.this, Doctor_Profile.class);
                startActivity(i);
                finish();
            }
        });

        // Log out
        LinearLayout log_out = findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(DoctorManageAppointment.this);
            }
        });
    }
}