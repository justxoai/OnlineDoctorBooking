package vn.edu.usth.doconcall.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.AuthResponse;
import vn.edu.usth.doconcall.Models.RegisterRequest;
import vn.edu.usth.doconcall.Network.AuthAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.R;

public class SignUp_Activity extends AppCompatActivity {

    private EditText full_name, phone_num, password, confirm_password;
    private Button sign_up_button;
    private CheckBox male, female;
    private TextView no_name, no_phone, phone_exist, phone_invalid, phone_length, no_password, short_password, upper_password, lower_password, number_password, no_confirm_password, match_password;

    boolean valid = true;
    boolean checked = true;
    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_sign_up);

        // Setup ID
        full_name = findViewById(R.id.input_name_sign_up);
        phone_num = findViewById(R.id.input_phone_sign_up);
        password = findViewById(R.id.input_password_sign_up);
        confirm_password = findViewById(R.id.input_confirm_pass_sign_up);

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

        sign_up_button = findViewById(R.id.sign_up_button);

        // Setup UI for Error
        no_name = findViewById(R.id.error_no_full_name);
        no_phone = findViewById(R.id.error_no_phone);
        phone_exist = findViewById(R.id.error_phone_exist);
        phone_invalid = findViewById(R.id.error_phone_invalid);
        phone_length = findViewById(R.id.error_phone_length);
        no_password = findViewById(R.id.error_no_password);
        short_password = findViewById(R.id.error_short_password);
        upper_password = findViewById(R.id.error_uppercase_password);
        lower_password = findViewById(R.id.error_lowercase_password);
        number_password = findViewById(R.id.error_number_password);
        no_confirm_password = findViewById(R.id.error_no_confirm_password);
        match_password = findViewById(R.id.error_match_password);

        no_name.setVisibility(View.GONE);
        no_phone.setVisibility(View.GONE);
        phone_exist.setVisibility(View.GONE);
        phone_invalid.setVisibility(View.GONE);
        phone_length.setVisibility(View.GONE);
        no_password.setVisibility(View.GONE);
        short_password.setVisibility(View.GONE);
        upper_password.setVisibility(View.GONE);
        lower_password.setVisibility(View.GONE);
        number_password.setVisibility(View.GONE);
        no_confirm_password.setVisibility(View.GONE);
        match_password.setVisibility(View.GONE);

        // Sign up function
        sign_up_function();
    }

    private void sign_up_function() {
        // Sign up
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_full_name();

                check_exist_phone_number(phone_num.getText().toString().trim());

                check_password();

                if (valid) {
                    String name = full_name.getText().toString().trim();
                    String phoneNumber = phone_num.getText().toString().trim();
                    String pass = password.getText().toString().trim();

                    AuthAPI authAPI = RetrofitClient.getInstance().create(AuthAPI.class);

                    RegisterRequest registerRequest = new RegisterRequest(name, pass, phoneNumber, "", gender, "PATIENT");

                    Call<AuthResponse> call = authAPI.register(registerRequest);
                    call.enqueue(new Callback<AuthResponse>() {
                        @Override
                        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(SignUp_Activity.this, "Signup successful! Please login.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignUp_Activity.this, Login_Activity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(SignUp_Activity.this, "Signup failed: " + response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable t) {
                            Toast.makeText(SignUp_Activity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        TextView log_in_page = findViewById(R.id.log_in_link);
        log_in_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp_Activity.this, Login_Activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public boolean check_full_name() {
        if (full_name.getText().toString().trim().isEmpty()) {
            no_name.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            no_name.setVisibility(View.GONE);
            valid = true;
        }

        return valid;
    }

    private void check_exist_phone_number(String phone) {

        if (phone.isEmpty()) {
            no_phone.setVisibility(View.VISIBLE);

            phone_exist.setVisibility(View.GONE);
            phone_invalid.setVisibility(View.GONE);
            phone_length.setVisibility(View.GONE);

            valid = false;
        } else if (phone.length() < 10) {
            phone_length.setVisibility(View.VISIBLE);

            no_phone.setVisibility(View.GONE);
            phone_exist.setVisibility(View.GONE);
            phone_invalid.setVisibility(View.GONE);

            valid = false;
        } else if (!phone.matches("^0\\d{9}$")) {
            phone_invalid.setVisibility(View.VISIBLE);

            no_phone.setVisibility(View.GONE);
            phone_exist.setVisibility(View.GONE);
            phone_length.setVisibility(View.GONE);

            valid = false;
        } else {
            UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
            Call<Boolean> call = userService.checkPhoneNumber(phone);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        boolean exists_phoneNumber = response.body();

                        if (exists_phoneNumber) {
                            valid = false;

                            phone_exist.setVisibility(View.VISIBLE);

                            no_phone.setVisibility(View.GONE);
                            phone_invalid.setVisibility(View.GONE);
                            phone_length.setVisibility(View.GONE);

                        } else {
                            no_phone.setVisibility(View.GONE);
                            phone_exist.setVisibility(View.GONE);
                            phone_invalid.setVisibility(View.GONE);
                            phone_length.setVisibility(View.GONE);

                            valid = true;
                        }
                    } else {
                        valid = false;
                        Log.e("CheckExistsPhoneNumber", "Missing response body");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    valid = false;
                    Log.e("CheckExistsPhoneNumber", "Error: " + t.getMessage(), t);
                }
            });
        }
    }

    public boolean check_password() {
        no_password.setVisibility(View.GONE);
        short_password.setVisibility(View.GONE);
        upper_password.setVisibility(View.GONE);
        lower_password.setVisibility(View.GONE);
        number_password.setVisibility(View.GONE);
        match_password.setVisibility(View.GONE);
        no_confirm_password.setVisibility(View.GONE);

        valid = true;

        String pass = password.getText().toString().trim();
        String confirm_pass = confirm_password.getText().toString().trim();

        if (pass.isEmpty()) {
            no_password.setVisibility(View.VISIBLE);
            valid = false;
            return valid;
        }

        if (pass.length() < 8) {
            short_password.setVisibility(View.VISIBLE);
            valid = false;
            return valid;
        }

        if (!pass.matches(".*[A-Z].*")) {
            upper_password.setVisibility(View.VISIBLE);
            valid = false;
            return valid;
        }

        if (!pass.matches(".*[a-z].*")) {
            lower_password.setVisibility(View.VISIBLE);
            valid = false;
            return valid;
        }

        if (!pass.matches(".*\\d.*")) {
            number_password.setVisibility(View.VISIBLE);
            valid = false;
            return valid;
        }

        if (confirm_pass.isEmpty()) {
            no_confirm_password.setVisibility(View.VISIBLE);
            valid = false;
            return valid;
        }

        if (!confirm_pass.equals(pass)) {
            match_password.setVisibility(View.VISIBLE);
            valid = false;
            return valid;
        }

        return valid;
    }

}