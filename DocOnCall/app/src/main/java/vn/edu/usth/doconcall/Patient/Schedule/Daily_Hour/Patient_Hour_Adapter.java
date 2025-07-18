package vn.edu.usth.doconcall.Patient.Schedule.Daily_Hour;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.doconcall.Utils.Calendar_Utils;
import vn.edu.usth.doconcall.Patient.Schedule.Week_Event.Patient_Event;
import vn.edu.usth.doconcall.R;

public class Patient_Hour_Adapter extends ArrayAdapter<Patient_HourEvent> {

    public Patient_Hour_Adapter(@NonNull Context context, List<Patient_HourEvent> hourEvents)
    {
        super(context, 0, hourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Patient_HourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.frame_hour, parent, false);

        setHour(convertView, event.time);
        setEvents(convertView, event.events);

        return convertView;
    }

    private void setHour(View convertView, LocalTime time)
    {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(Calendar_Utils.formattedShortTime(time));
    }

    private void setEvents(View convertView, ArrayList<Patient_Event> events)
    {
        LinearLayout eventLayout = convertView.findViewById(R.id.event_daily);

        TextView doctor_event_name = convertView.findViewById(R.id.doctor_name_daily_event);
        TextView appointment_type_event = convertView.findViewById(R.id.appointment_type_daily_event);

        if (events == null || events.isEmpty()) {
            eventLayout.setVisibility(GONE);
        } else {
            eventLayout.setVisibility(VISIBLE);

            // For simplicity, display the first event only (or loop if you want all)
            Patient_Event e = events.get(0);

            doctor_event_name.setText("Doctor: " + e.getDoctor_name());
            appointment_type_event.setText("Type: " + e.getAppointment_type());
        }
    }

}
