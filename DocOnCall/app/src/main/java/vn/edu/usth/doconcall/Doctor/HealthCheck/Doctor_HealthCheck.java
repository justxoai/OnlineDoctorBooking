package vn.edu.usth.doconcall.Doctor.HealthCheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import vn.edu.usth.doconcall.Doctor.Dashboard.Doctor_Dashboard;
import vn.edu.usth.doconcall.Doctor.HealthCheck.Fragment.Patient_Select_Fragment;
import vn.edu.usth.doconcall.Doctor.Profile.Doctor_Profile;
import vn.edu.usth.doconcall.Auth.Login_Activity;
import vn.edu.usth.doconcall.Doctor.Schedule.Doctor_Schedule;

import vn.edu.usth.doconcall.R;

public class Doctor_HealthCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_health_check);

        // Health Check Function
        health_check_function();
    }

    private void health_check_function() {
        Fragment patient_select = new Patient_Select_Fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, patient_select);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}