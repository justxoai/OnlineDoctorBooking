package vn.edu.usth.doconcall.Patient.List_Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.Patient.Appointment.PatientManageAppointment;
import vn.edu.usth.doconcall.Patient.Dashboard.Patient_Dashboard;
import vn.edu.usth.doconcall.Patient.HealthCheck.Patient_HealthCheck;
import vn.edu.usth.doconcall.Patient.List_Doctor.RecyclerView.Doctor_Adapter;
import vn.edu.usth.doconcall.Patient.List_Doctor.RecyclerView.Doctor_Items;
import vn.edu.usth.doconcall.Patient.Profile.Patient_Profile;
import vn.edu.usth.doconcall.Patient.Schedule.Patient_Schedule;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class Patient_List_Doctor extends AppCompatActivity {

    private List<Doctor_Items> filter_items;
    private SearchView doctor_search;
    private RecyclerView doctor_recycler;
    private Doctor_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_list_doctor);

        // Side navigate
        DrawerLayout mDrawLayout = findViewById(R.id.patient_list_doctor_activity);

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

        // Setup RecyclerView and SearchView
        doctor_recycler_and_search_view_function();

        // Sidebar function
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

    private void doctor_recycler_and_search_view_function() {
        doctor_search = findViewById(R.id.doctor_searchView);
        doctor_search.clearFocus();

        doctor_recycler = findViewById(R.id.recycler_view_doctor);

        filter_items = new ArrayList<Doctor_Items>();

        fetch_doctor_list(null);

        adapter = new Doctor_Adapter(this, filter_items);

        doctor_recycler.setLayoutManager(new LinearLayoutManager(this));
        doctor_recycler.setAdapter(adapter);

        doctor_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                fetch_doctor_list(s);
                return false;
            }
        });
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
                        filter_items.clear();
                        for (DoctorDto doctor : doctors) {
                            filter_items.add(new Doctor_Items(doctor.getId(), doctor.getName(), doctor.getSpecialization(), R.drawable.profile));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("PatientListDoctor", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<List<DoctorDto>> call, Throwable t) {
                    Log.e("PatientListDoctor", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("PatientListDoctor", "Missing token");
        }
    }

    private void side_bar_function() {
        // Dashboard
        LinearLayout dashboard_page = findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_List_Doctor.this, Patient_Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        // Schedule
        LinearLayout schedule_page = findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_List_Doctor.this, Patient_Schedule.class);
                startActivity(i);
                finish();
            }
        });

        // Health Check
        LinearLayout health_page = findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_List_Doctor.this, Patient_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });

        // Manage Appointment
        LinearLayout manage_page = findViewById(R.id.to_manage_appointment_page);
        manage_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Patient_List_Doctor.this, PatientManageAppointment.class);
                startActivity(i);
                finish();
            }
        });

        // Consult Doctor
        LinearLayout doctor_page = findViewById(R.id.to_doctor_list_page);
        doctor_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_List_Doctor.this, Patient_List_Doctor.class);
                startActivity(i);
                finish();
            }
        });

        // Profile
        LinearLayout profile_page = findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_List_Doctor.this, Patient_Profile.class);
                startActivity(i);
                finish();
            }
        });

        // Logout
        LinearLayout log_out = findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(Patient_List_Doctor.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}