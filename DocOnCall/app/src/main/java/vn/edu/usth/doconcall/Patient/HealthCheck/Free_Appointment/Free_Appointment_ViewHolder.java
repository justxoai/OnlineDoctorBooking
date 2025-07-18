package vn.edu.usth.doconcall.Patient.HealthCheck.Free_Appointment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class Free_Appointment_ViewHolder extends RecyclerView.ViewHolder {

    public TextView timeTextView;

    public Free_Appointment_ViewHolder(View itemView) {
        super(itemView);
        timeTextView = itemView.findViewById(R.id.free_appointment);
    }
}