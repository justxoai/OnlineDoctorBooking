package vn.edu.usth.doconcall.Patient.HealthCheck.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Auth.Login_Activity;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.Patient.Appointment.PatientManageAppointment;
import vn.edu.usth.doconcall.Patient.Dashboard.Patient_Dashboard;
import vn.edu.usth.doconcall.Patient.HealthCheck.Patient_HealthCheck;
import vn.edu.usth.doconcall.Patient.List_Doctor.Patient_List_Doctor;
import vn.edu.usth.doconcall.Patient.Profile.Patient_Profile;
import vn.edu.usth.doconcall.Patient.Schedule.Patient_Schedule;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class HealthCheck_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout
        View v = inflater.inflate(R.layout.fragment_health_check_, container, false);

        // Side navigate
        DrawerLayout mDrawLayout = v.findViewById(R.id.health_check_fragment);

        // Function to open Side-menu
        ImageButton mImageView = v.findViewById(R.id.menu_button);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawLayout != null && !mDrawLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Side Bar Function
        side_bar_function(v);

        // Health Check Fragment Function
        health_check_function(v);

        // Fetch user information
        fetch_user_information(v);

        return v;
    }

    private void fetch_user_information(View v) {
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
                        setTextWelcome(user, v);
                    } else {
                        Log.e("PatientHealthCheckSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("PatientHealthCheckSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("PatientHealthCheckSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user, View v) {
        TextView text = v.findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }

    private void health_check_function(View v) {
        Button start_survey = v.findViewById(R.id.start_health_check_button);
        start_survey.setOnClickListener(view -> {
            Fragment doctor_select = new Specialization_Select_Fragment();
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(android.R.id.content, doctor_select);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

    }

    private void side_bar_function(View v) {
        // Dashboard
        LinearLayout dashboard_page = v.findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Patient_Dashboard.class);
                startActivity(i);
            }
        });

        // Schedule
        LinearLayout schedule_page = v.findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Patient_Schedule.class);
                startActivity(i);
            }
        });

        // Health Check
        LinearLayout health_page = v.findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Patient_HealthCheck.class);
                startActivity(i);
            }
        });

        // Manage Appointment
        LinearLayout manage_page = v.findViewById(R.id.to_manage_appointment_page);
        manage_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), PatientManageAppointment.class);
                startActivity(i);
            }
        });

        // List Doctor
        LinearLayout doctor_page = v.findViewById(R.id.to_doctor_list_page);
        doctor_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Patient_List_Doctor.class);
                startActivity(i);
            }
        });

        // Profile
        LinearLayout profile_page = v.findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Patient_Profile.class);
                startActivity(i);
            }
        });

        // Log out
        LinearLayout log_out = v.findViewById(R.id.to_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUtils.getInstance().logoutUser(requireContext());
            }
        });
    }

}