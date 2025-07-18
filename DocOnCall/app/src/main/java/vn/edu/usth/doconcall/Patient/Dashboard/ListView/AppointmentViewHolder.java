    package vn.edu.usth.doconcall.Patient.Dashboard.ListView;

    import android.view.View;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import vn.edu.usth.doconcall.R;

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        private TextView appointmentTime;
        private TextView doctorName;
        private TextView appointmentType;
        private TextView appointmentStatus;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentTime = itemView.findViewById(R.id.time_frame_patient_db);
            doctorName = itemView.findViewById(R.id.doctor_name_frame_patient_db);
            appointmentType = itemView.findViewById(R.id.appointment_type_frame_patient_db);
            appointmentStatus = itemView.findViewById(R.id.appointment_status_frame_patient_db);
        }

        public void bind(AppointmentItem item) {
            appointmentTime.setText(item.getTime());
            doctorName.setText(item.getDoctor_name());
            appointmentType.setText(item.getAppointment_type());
            appointmentStatus.setText(item.getAppointment_status());
        }
    }
