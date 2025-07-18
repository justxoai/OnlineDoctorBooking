package vn.edu.usth.doconcall.Patient.Appointment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class Appointment_ViewHolder extends RecyclerView.ViewHolder {

    TextView dateTextView, timeTextView, doctorNameTextView;

    public Appointment_ViewHolder(@NonNull View itemView) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.appointment_date);
        timeTextView = itemView.findViewById(R.id.appointment_time);
        doctorNameTextView = itemView.findViewById(R.id.doctor_name);

    }
}
