package vn.edu.usth.doconcall.Doctor.HealthCheck.List_Patient;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class Patient_ViewHolder extends RecyclerView.ViewHolder {

    TextView name, gender, dob;

    public Patient_ViewHolder(@NonNull View view){
        super(view);
        name = view.findViewById(R.id.list_patient_name);
        gender = view.findViewById(R.id.list_patient_gender);
        dob = view.findViewById(R.id.list_patient_dob);
    }
}
