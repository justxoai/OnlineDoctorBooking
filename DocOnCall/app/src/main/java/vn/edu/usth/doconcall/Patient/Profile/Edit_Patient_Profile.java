package vn.edu.usth.doconcall.Patient.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.R;

public class Edit_Patient_Profile extends AppCompatActivity {

    private TextInputEditText patient_name, patient_dob_day, patient_dob_month, patient_dob_year;

    private CheckBox male, female;

    private String gender = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_patient);

        // Checkbox function
        male = findViewById(R.id.checkbox_male);
        female = findViewById(R.id.checkbox_female);

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    female.setChecked(false);
                    gender = "MALE";
                }
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    male.setChecked(false);
                    gender = "FEMALE";
                }
            }
        });

        // Setup EditText
        patient_name = findViewById(R.id.edittext_patient_name);
        patient_dob_day = findViewById(R.id.edit_day);
        patient_dob_month = findViewById(R.id.edit_month);
        patient_dob_year = findViewById(R.id.edit_year);

        // Setup function
        edit_patient_profile_function();

    }

    private void updateProfile() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int userId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && userId != -1) {
            String authHeader = "Bearer " + token;

            UserDto updateUser = new UserDto();

            updateUser.setGender(gender);

            String name = patient_name.getText().toString();
            updateUser.setName(name);

            String day = patient_dob_day.getText().toString().trim();
            String month = patient_dob_month.getText().toString().trim();
            String year = patient_dob_year.getText().toString().trim();

            if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
                Toast.makeText(this, "Please enter day, month, and year", Toast.LENGTH_SHORT).show();
                return;
            }

            int dayInt = Integer.parseInt(day);
            int monthInt = Integer.parseInt(month);
            int yearInt = Integer.parseInt(year);

            if (dayInt < 1 || dayInt > 31 || monthInt < 1 || monthInt > 12 || yearInt < 1935 || yearInt > Calendar.getInstance().get(Calendar.YEAR)) {
                Toast.makeText(this, "Invalid date input", Toast.LENGTH_SHORT).show();
                return;
            }

            String dob = String.format("%04d-%02d-%02d", yearInt, monthInt, dayInt);

            updateUser.setBirthday(dob);

            UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
            Call<UserDto> call = userService.updateUser(authHeader, userId, updateUser);
            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(Edit_Patient_Profile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(Edit_Patient_Profile.this, Patient_Profile.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(Edit_Patient_Profile.this, "Response null", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("Edit Patient Profile", "Error: " + t.getMessage(), t);
                    Toast.makeText(Edit_Patient_Profile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    private void edit_patient_profile_function() {
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Edit_Patient_Profile.this, Patient_Profile.class);
                startActivity(i);
                finish();
            }
        });

        Button save_button = findViewById(R.id.save_patient_profile_button);
        save_button.setOnClickListener(view -> updateProfile());

    }

}