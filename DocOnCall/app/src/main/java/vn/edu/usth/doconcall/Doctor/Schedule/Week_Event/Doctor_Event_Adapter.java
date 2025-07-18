package vn.edu.usth.doconcall.Doctor.Schedule.Week_Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

import vn.edu.usth.doconcall.Utils.Calendar_Utils;
import vn.edu.usth.doconcall.R;

public class Doctor_Event_Adapter extends ArrayAdapter<Doctor_Event> {

    private Context context;
    private List<Doctor_Event> events;

    public Doctor_Event_Adapter(@NonNull Context context, List<Doctor_Event> events)
    {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Doctor_Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.frame_event_2, parent, false);

        TextView time_event = convertView.findViewById(R.id.time_event_frame);
        TextView patient_name_event = convertView.findViewById(R.id.patient_name_daily_event);
        TextView appointment_type_event = convertView.findViewById(R.id.appointment_type_daily_event);
        TextView appointment_status_event = convertView.findViewById(R.id.appointment_status_daily_event);

        String eventTitle = Calendar_Utils.formattedTime(event.getTime());

        time_event.setText("Time: " + eventTitle );
        patient_name_event.setText("Patient: " + event.getPatient_name());
        appointment_type_event.setText("Type: " + event.getAppointment_type());

        String status = event.getAppointment_status();
        appointment_status_event.setText("Status: " + status);

        switch (status) {
            case "Upcoming":
                appointment_status_event.setTextColor(ContextCompat.getColor(context, R.color.positive)); // Green
                break;
            case "Complete":
                appointment_status_event.setTextColor(ContextCompat.getColor(context, R.color.primary_color)); // Blue-gray
                break;
            case "Cancel":
                appointment_status_event.setTextColor(ContextCompat.getColor(context, R.color.error)); // Red
                break;
            default:
                appointment_status_event.setTextColor(ContextCompat.getColor(context, R.color.positive));
                break;
        }

        return convertView;
    }
}
