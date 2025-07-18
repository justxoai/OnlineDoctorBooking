package vn.edu.usth.doconcall.Doctor.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Appointment.DoctorManageAppointment;
import vn.edu.usth.doconcall.Doctor.Dashboard.Doctor_Dashboard;
import vn.edu.usth.doconcall.Doctor.HealthCheck.Doctor_HealthCheck;
import vn.edu.usth.doconcall.Doctor.Schedule.Doctor_Schedule;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class Doctor_Profile extends AppCompatActivity {

    private TextView doctor_name, doctor_specialization, year_experience, work_process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_profile);

        // Side navigate
        DrawerLayout mDrawLayout = findViewById(R.id.doctor_profile_activity);

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

        // SideBar Function
        side_bar_function();

        // Other Function
        profile_function();

        // Setup Information
        doctor_name = findViewById(R.id.doctor_name_information);
        doctor_specialization = findViewById(R.id.doctor_specialization_information);
        year_experience = findViewById(R.id.year_experience_information);
        work_process = findViewById(R.id.work_process_information);

        setup_information();

        // Fetch user information
        fetch_user_information();

        // Fetch doctor Profile
        fetch_doctor_profile();
    }

    private void fetch_doctor_profile() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int userId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && userId != -1) {
            String authHeader = "Bearer " + token;

            UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
            Call<UserDto> call = userService.getUserById(authHeader, userId);

            DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
            Call<DoctorDto> doctorCall = doctorService.getDoctorById(authHeader, userId);

            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDto user = response.body();
                        setUserProfile(user);
                    } else {
                        Log.e("DoctorProfile-User", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("DoctorProfile-User", "Error: " + t.getMessage(), t);
                }
            });

            doctorCall.enqueue(new Callback<DoctorDto>() {
                @Override
                public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                       DoctorDto doctor = response.body();
                       setDoctorProfile(doctor);
                    } else {
                        Log.e("DoctorProfile-Doctor", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<DoctorDto> call, Throwable t) {
                    Log.e("DoctorProfile-Doctor", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("DoctorProfile-Doctor", "Missing token or userId");
        }
    }

    private void setUserProfile(UserDto user) {
        TextView doctor_name = findViewById(R.id.doctor_name_information);
        doctor_name.setText("Name: " + user.getName());
    }

    private void setDoctorProfile(DoctorDto doctor){
        TextView doctor_specialization = findViewById(R.id.doctor_specialization_information);
        doctor_specialization.setText(doctor.getSpecialization());

        TextView year_experience = findViewById(R.id.year_experience_information);
        year_experience.setText(doctor.getYear_experience() + " years of experience");

        TextView work_process = findViewById(R.id.work_process_information);
        work_process.setText(doctor.getWork_experience().replace("; ","\n").trim());
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
                        Log.e("DoctorProfileSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("DoctorProfileSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("DoctorProfileSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user) {
        TextView text = findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }

    private void setup_information() {
        Intent i = getIntent();
        String name = i.getStringExtra("doctorName");
        String specialization = i.getStringExtra("doctorSpecialization");
        String year = i.getStringExtra("doctorYearExperience");
        String process = i.getStringExtra("doctorWorkProcess");

        if(name != null){
            doctor_name.setText("Doctor: " + name);
        }
        else{
            doctor_name.setText("Doctor: Doctor Name");
        }

        if(specialization != null){
            doctor_specialization.setText("Specialization: " + specialization);
        } else{
            doctor_specialization.setText("Specialization: Doctor Specialization");
        }

        if(year != null){
            year_experience.setText(year + " years of experience.");
        } else{
            year_experience.setText("Doctor's Years of Experience");
        }

        if(process != null){
            work_process.setText(process);
        } else{
            work_process.setText("Doctor's WorkProcess");
        }
    }

    private void profile_function() {
        Button edit_profile = findViewById(R.id.edit_doctor_profile_button);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Profile.this, Edit_Doctor_Profile.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void side_bar_function(){
        // Dashboard
        LinearLayout dashboard_page = findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Profile.this, Doctor_Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        // Schedule
        LinearLayout schedule_page = findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Profile.this, Doctor_Schedule.class);
                startActivity(i);
                finish();
            }
        });

        // Health Check
        LinearLayout health_page = findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Profile.this, Doctor_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });

        // Manage Appointment
        LinearLayout manage_appointment = findViewById(R.id.to_manage_appointment_page);
        manage_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Profile.this, DoctorManageAppointment.class);
                startActivity(i);
            }
        });

        // Profile
        LinearLayout profile_page = findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Doctor_Profile.this, Doctor_Profile.class);
                startActivity(i);
                finish();
            }
        });

        // Log out
        LinearLayout log_out = findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(Doctor_Profile.this);
            }
        });
    }
}