package vn.edu.usth.doconcall.Patient.HealthCheck.Specialization;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class SpecializationViewHolder extends RecyclerView.ViewHolder {

    TextView doctor_specialization;

    public SpecializationViewHolder(@NonNull View itemView) {
        super(itemView);
        doctor_specialization = itemView.findViewById(R.id.doctor_specialization);
    }

}
