package vn.edu.usth.doconcall.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Dashboard.Doctor_Dashboard;
import vn.edu.usth.doconcall.Models.AuthResponse;
import vn.edu.usth.doconcall.Models.AuthenticationRequest;
import vn.edu.usth.doconcall.Network.AuthAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Patient.Dashboard.Patient_Dashboard;
import vn.edu.usth.doconcall.R;

public class Login_Activity extends AppCompatActivity {

    private EditText phone_num, password;
    private Button login_button;
    private TextView no_phone, no_password, no_user, wrong_password;

    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setup ID
        phone_num = findViewById(R.id.input_phone_num_log_in);
        password = findViewById(R.id.input_pass_log_in);
        login_button = findViewById(R.id.login_button);

        // Setup UI for Error
        RelativeLayout loading_layout = findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);

        LinearLayout header_layout = findViewById(R.id.header_login);
        header_layout.setVisibility(View.GONE);

        LinearLayout login_layout = findViewById(R.id.login_layout);
        login_layout.setVisibility(View.GONE);

        no_phone = findViewById(R.id.error_no_phone);
        no_user = findViewById(R.id.error_no_user);
        no_password = findViewById(R.id.error_no_password);
        wrong_password = findViewById(R.id.error_wrong_password);

        no_phone.setVisibility(View.GONE);
        no_user.setVisibility(View.GONE);
        no_password.setVisibility(View.GONE);
        wrong_password.setVisibility(View.GONE);

        // Setup Delay Loading
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading_layout.setVisibility(View.GONE);
                header_layout.setVisibility(View.VISIBLE);
                login_layout.setVisibility(View.VISIBLE);
            }
        }, 3000);

        // Login Function
        login_function();
    }

    private void login_function() {
        // Change password
        TextView forgot_pass = findViewById(R.id.forgot_password);
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Activity.this, ChangePassword_Activity.class);
                startActivity(i);
                finish();
            }
        });

        // Sign up
        TextView sign_up = findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_Activity.this, SignUp_Activity.class);
                startActivity(i);
                finish();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_phone_number();
                check_password();

                if (valid) {
                    performLogin();
                }
            }
        });
    }

    private boolean check_phone_number() {
        if (phone_num.getText().toString().trim().isEmpty()) {
            no_phone.setVisibility(View.VISIBLE);

            valid = false;
        } else {
            no_phone.setVisibility(View.GONE);

            valid = true;
        }
        return valid;
    }

    private boolean check_password() {
        if (password.getText().toString().isEmpty()) {
            no_password.setVisibility(View.VISIBLE);

            valid = false;
        } else {
            no_password.setVisibility(View.GONE);

            valid = true;
        }

        return valid;
    }

    private void performLogin() {
        AuthAPI authAPI = RetrofitClient.getInstance().create(AuthAPI.class);

        String login_phoneNumber = phone_num.getText().toString().trim();
        String login_password = password.getText().toString();

        AuthenticationRequest loginRequest = new AuthenticationRequest(login_phoneNumber, login_password);

        authAPI.login(loginRequest).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().getToken();
                    String role = response.body().getRole();

                    int userId = response.body().getUserId();
                    String userName = response.body().getUserName();

                    // Save session using SharedPreferences
                    SessionManager.getInstance(Login_Activity.this).saveSession(token, userId, userName, role);

                    Toast.makeText(Login_Activity.this, "Login successful", Toast.LENGTH_SHORT).show();

                    if ("DOCTOR".equals(role)) {
                        Intent i = new Intent(Login_Activity.this, Doctor_Dashboard.class);
                        startActivity(i);
                        finish();
                    } else if ("PATIENT".equals(role)) {
                        Intent i = new Intent(Login_Activity.this, Patient_Dashboard.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(Login_Activity.this, "Unknown role", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    wrong_password.setVisibility(View.VISIBLE);
                    Toast.makeText(Login_Activity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(Login_Activity.this, "Login failed", Toast.LENGTH_SHORT).show();
                Log.e("Login","Login failed: " + t.getMessage(), t);
            }
        });
    }

}