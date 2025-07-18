package vn.edu.usth.doconcall.Doctor.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.R;

public class Edit_Doctor_Profile extends AppCompatActivity {

    private TextInputEditText doctor_name, doctor_specialization, doctor_year_experience, doctor_work_process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_edit_doctor);

        // Edit Profile Function
        doctor_specialization = findViewById(R.id.edittext_doctor_specialization);
        doctor_year_experience = findViewById(R.id.edittext_doctor_year_experience);
        doctor_work_process = findViewById(R.id.edittext_doctor_work_process);

        edit_profile_function();

    }

    private void updateProfile() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int userId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && userId != -1) {
            String authHeader = "Bearer " + token;

            DoctorDto updatedDoctor = new DoctorDto();

            String specialization = doctor_specialization.getText().toString();
            String year_experience = doctor_year_experience.getText().toString();
            String work_process = doctor_work_process.getText().toString();

            updatedDoctor.setSpecialization(specialization);
            updatedDoctor.setYear_experience(year_experience);
            updatedDoctor.setWork_experience(work_process);

            DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
            Call<DoctorDto> call = doctorService.updateDoctor(authHeader, userId, updatedDoctor);
            call.enqueue(new Callback<DoctorDto>() {
                @Override
                public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(Edit_Doctor_Profile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Edit_Doctor_Profile.this, Doctor_Profile.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(Edit_Doctor_Profile.this, "Response null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DoctorDto> call, Throwable t) {
                    Log.e("Edit Doctor Profile", "Error: " + t.getMessage(), t);
                    Toast.makeText(Edit_Doctor_Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void edit_profile_function() {
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Edit_Doctor_Profile.this, Doctor_Profile.class);
                startActivity(i);
                finish();
            }
        });

        Button save_button = findViewById(R.id.save_doctor_profile_button);
        save_button.setOnClickListener(view -> updateProfile());
    }

}