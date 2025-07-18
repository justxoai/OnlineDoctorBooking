package vn.edu.usth.doconcall.Patient.List_Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Patient.HealthCheck.Patient_HealthCheck;
import vn.edu.usth.doconcall.R;

public class Doctor_Information extends AppCompatActivity {

    private TextView doctor_name, doctor_name_header, doctor_speci, doctor_work, doctor_year;
    private ImageView doctor_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_information3);

        doctor_name = findViewById(R.id.doctor_name_information);
        doctor_name_header = findViewById(R.id.doctor_name_header_information);
        doctor_speci = findViewById(R.id.doctor_specialization_information);
        doctor_work = findViewById(R.id.work_process_information);
        doctor_year = findViewById(R.id.year_experience_information);

        doctor_image = findViewById(R.id.doctor_image_information);

        // Setup Text
        fetch_doctor_information();

        // Doctor Information Function
        doctor_information_function();


    }

    private void fetch_doctor_information() {
        String doctor_set_name = getIntent().getStringExtra("Doctor Name");
        String doctor_set_specialization = getIntent().getStringExtra("Doctor Specialization");
        int doctor_set_image = getIntent().getIntExtra("Doctor Image", R.drawable.doctor_list);

        doctor_name.setText(doctor_set_name);
        doctor_name_header.setText(doctor_set_name);
        doctor_speci.setText(doctor_set_specialization);
        doctor_image.setImageResource(doctor_set_image);

        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int userId = getIntent().getIntExtra("Doctor Id", 0);

        if (token != null && !token.isEmpty() && userId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
            Call<DoctorDto> call = doctorService.getDoctorById(authHeader, userId);
            call.enqueue(new Callback<DoctorDto>() {
                @Override
                public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        DoctorDto doctor = response.body();
                        doctor_year.setText(doctor.getYear_experience() + " years of experience");
                        doctor_work.setText(doctor.getWork_experience().replace("; ", "\n").trim());
                    } else {
                        Log.e("DoctorInformation", "Failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<DoctorDto> call, Throwable t) {
                    Log.e("DoctorInformation", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("DoctorInformation", "Missing token or userId");
        }


    }

    private void doctor_information_function() {
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> {
            onBackPressed();
        });

        Button schedule_button = findViewById(R.id.schedule_button);
        schedule_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Doctor_Information.this, Patient_HealthCheck.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}