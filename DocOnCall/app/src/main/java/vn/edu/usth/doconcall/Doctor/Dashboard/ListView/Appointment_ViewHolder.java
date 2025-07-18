package vn.edu.usth.doconcall.Doctor.Dashboard.ListView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class Appointment_ViewHolder extends RecyclerView.ViewHolder {

    private TextView appointmentTime;
    private TextView patientName;
    private TextView appointmentType;
    private TextView appointmentStatus;

    public Appointment_ViewHolder(@NonNull View itemView) {
        super(itemView);
        appointmentTime = itemView.findViewById(R.id.time_frame_doctor_db);
        patientName = itemView.findViewById(R.id.patient_name_frame_doctor_db);
        appointmentType = itemView.findViewById(R.id.appointment_type_frame_doctor_db);
        appointmentStatus = itemView.findViewById(R.id.appointment_status_frame_doctor_db);
    }

    public void bind(Appointment_Item item) {
        appointmentTime.setText(item.getTime());
        patientName.setText(item.getPatient_name());
        appointmentType.setText(item.getAppointment_type());
        appointmentStatus.setText(item.getAppointment_status());
    }
}
