package vn.edu.usth.doconcall.Patient.HealthCheck;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import vn.edu.usth.doconcall.Patient.HealthCheck.Fragment.HealthCheck_Fragment;
import vn.edu.usth.doconcall.R;

public class Patient_HealthCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_patient_health_check);

        // Health Check Function
        health_check_function();
    }

    private void health_check_function() {
        Fragment health_check_fragment = new HealthCheck_Fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, health_check_fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}