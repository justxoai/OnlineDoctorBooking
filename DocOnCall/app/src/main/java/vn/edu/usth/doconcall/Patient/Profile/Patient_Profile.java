package vn.edu.usth.doconcall.Patient.Profile;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.Patient.Appointment.PatientManageAppointment;
import vn.edu.usth.doconcall.Patient.Dashboard.Patient_Dashboard;
import vn.edu.usth.doconcall.Patient.HealthCheck.Patient_HealthCheck;
import vn.edu.usth.doconcall.Patient.List_Doctor.Patient_List_Doctor;
import vn.edu.usth.doconcall.Patient.Schedule.Patient_Schedule;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class Patient_Profile extends AppCompatActivity {

    private TextView patient_name, patient_gender, patient_dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_profile);

        // Side navigate
        DrawerLayout mDrawLayout = findViewById(R.id.patient_profile_activity);

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

        // Profile Function
        profile_function();

        // Setup Information
        patient_name = findViewById(R.id.textview_patient_name);
        patient_gender = findViewById(R.id.textview_patient_gender);
        patient_dob = findViewById(R.id.textview_patient_dob);

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
                        setPatientProfile(user);
                    } else {
                        Log.e("PatientProfileSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("PatientProfileSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("PatientProfileSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user) {
        TextView text = findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }

    private void setPatientProfile(UserDto user) {
        TextView name = findViewById(R.id.textview_patient_name);
        name.setText("Name: " + user.getName());

        TextView gender = findViewById(R.id.textview_patient_gender);
        gender.setText("Gender: " + user.getGender());

        TextView dob = findViewById(R.id.textview_patient_dob);
        if (user.getBirthday() == null) {
            dob.setText("DoB: NaN");
        } else {
            String rawDate = user.getBirthday(); // e.g., "2004-12-28"
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date date = fromFormat.parse(rawDate);
                String formattedDate = toFormat.format(date);
                dob.setText("DoB: " + formattedDate); // Output: "DoB: 28-12-2004"
            } catch (ParseException e) {
                e.printStackTrace();
                dob.setText("DoB: " + user.getBirthday());
            }
        }

        TextView phone = findViewById(R.id.textview_patient_phone);
        phone.setText("Phone: " + user.getPhoneNumber());

    }

    private void profile_function() {
        Button edit_profile = findViewById(R.id.edit_patient_profile_button);
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Profile.this, Edit_Patient_Profile.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void side_bar_function() {
        // Dashboard
        LinearLayout dashboard_page = findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Profile.this, Patient_Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        // Schedule
        LinearLayout schedule_page = findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Profile.this, Patient_Schedule.class);
                startActivity(i);
                finish();
            }
        });

        // Health Check
        LinearLayout health_page = findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Profile.this, Patient_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });

        // Manage Appointment
        LinearLayout manage_page = findViewById(R.id.to_manage_appointment_page);
        manage_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Patient_Profile.this, PatientManageAppointment.class);
                startActivity(i);
                finish();
            }
        });

        // Consult Doctor
        LinearLayout doctor_page = findViewById(R.id.to_doctor_list_page);
        doctor_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Profile.this, Patient_List_Doctor.class);
                startActivity(i);
                finish();
            }
        });

        // Profile
        LinearLayout profile_page = findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Patient_Profile.this, Patient_Profile.class);
                startActivity(i);
                finish();
            }
        });

        // Logout
        LinearLayout log_out = findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(Patient_Profile.this);
            }
        });
    }

}