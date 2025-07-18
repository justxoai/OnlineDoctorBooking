package vn.edu.usth.doconcall.Doctor.HealthCheck.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Appointment.DoctorManageAppointment;
import vn.edu.usth.doconcall.Doctor.Dashboard.Doctor_Dashboard;
import vn.edu.usth.doconcall.Doctor.HealthCheck.List_Patient.Patient_Adapter;
import vn.edu.usth.doconcall.Doctor.HealthCheck.List_Patient.Patient_Item;
import vn.edu.usth.doconcall.Doctor.Profile.Doctor_Profile;
import vn.edu.usth.doconcall.Doctor.Schedule.Doctor_Schedule;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.LogoutUtils;

public class Patient_Select_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Patient_Item> items;
    private Patient_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout
        View v = inflater.inflate(R.layout.fragment_patient__select_, container, false);

        // Side navigate
        DrawerLayout mDrawLayout = v.findViewById(R.id.patient_select_fragment);

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

        // List of Patient Recycler
        list_patient_recycler_view(v);

        // Side bar function
        side_bar_function(v);

        // Patient Select Fragment Function
        patient_select_fragment_function(v);

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
                        Log.e("PatientSelectSideBar", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("PatientSelectSideBar", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("PatientSelectSideBar", "Missing token or userId");
        }
    }

    private void setTextWelcome(UserDto user, View v) {
        TextView text = v.findViewById(R.id.welcome_message);
        text.setText("Hi, " + user.getName());
    }

    private void patient_select_fragment_function(View v) {
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireContext(), DoctorManageAppointment.class);
                startActivity(i);
            }
        });
    }

    private void list_patient_recycler_view(View v) {
        recyclerView = v.findViewById(R.id.list_patient_recycler_view);

        items = new ArrayList<Patient_Item>();

        fetch_patient_select();

        adapter = new Patient_Adapter(requireContext(), items);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fetch_patient_select() {
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

                        if (scheduleEventDtos.isEmpty()){
                            TextView no_patients_text = getView().findViewById(R.id.no_patients_text);
                            no_patients_text.setVisibility(View.VISIBLE);

                            recyclerView.setVisibility(View.GONE);
                        } else {
                            for (ScheduleEventDto scheduleEventDto : scheduleEventDtos) {
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
                                            Log.e("PatientSelectFragment-GetUser", "Missing response or body");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<UserDto> call, Throwable t) {
                                        Log.e("PatientSelectFragment-GetUser", "Error: " + t.getMessage(), t);
                                    }
                                });
                            }

                        }

                    } else {
                        Log.e("PatientSelectFragment", "Missing response or body");
                    }
                }

                @Override
                public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                    Log.e("PatientSelectFragment", "Error: " + t.getMessage(), t);
                }
            });


        } else {
            Log.e("PatientSelectFragment", "Missing token or userId");
        }
    }

    private void side_bar_function(View v) {
        // Dashboard
        LinearLayout dashboard_page = v.findViewById(R.id.to_dashboard_page);
        dashboard_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Doctor_Dashboard.class);
                startActivity(i);
            }
        });

        // Schedule
        LinearLayout schedule_page = v.findViewById(R.id.to_schedule_page);
        schedule_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Doctor_Schedule.class);
                startActivity(i);
            }
        });

        // Health Check
        LinearLayout health_page = v.findViewById(R.id.to_health_check_page);
        health_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Doctor_Schedule.class);
                startActivity(i);
            }
        });

        // Manage Appointment
        LinearLayout manage_appointment = v.findViewById(R.id.to_manage_appointment_page);
        manage_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), DoctorManageAppointment.class);
                startActivity(i);
            }
        });

        // Profile
        LinearLayout profile_page = v.findViewById(R.id.to_profile_page);
        profile_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(requireContext(), Doctor_Profile.class);
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

    private static String formatDateString(String date) {
        LocalDate parsed = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return parsed.format(formatter);
    }

}