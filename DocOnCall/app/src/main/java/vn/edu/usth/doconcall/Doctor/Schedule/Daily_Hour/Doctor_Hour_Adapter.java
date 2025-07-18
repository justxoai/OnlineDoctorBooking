package vn.edu.usth.doconcall.Doctor.Schedule.Daily_Hour;

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

import vn.edu.usth.doconcall.Doctor.Schedule.Week_Event.Doctor_Event;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;

public class Doctor_Hour_Adapter extends ArrayAdapter<Doctor_HourEvent> {

    public Doctor_Hour_Adapter(@NonNull Context context, List<Doctor_HourEvent> hourEvents)
    {
        super(context, 0, hourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Doctor_HourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.frame_hour_2, parent, false);

        setHour(convertView, event.time);
        setEvents(convertView, event.events);

        return convertView;
    }

    private void setHour(View convertView, LocalTime time)
    {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(Calendar_Utils.formattedShortTime(time));
    }

    private void setEvents(View convertView, ArrayList<Doctor_Event> events)
    {
        LinearLayout event = convertView.findViewById(R.id.event_daily);

        TextView patient_name_event = convertView.findViewById(R.id.patient_name_daily_event);
        TextView appointment_type_event = convertView.findViewById(R.id.appointment_type_daily_event);

        if (events == null || events.isEmpty()) {
            event.setVisibility(GONE);
        } else {
            event.setVisibility(VISIBLE);

        // For simplicity, display the first event only (or loop if you want all)
        Doctor_Event e = events.get(0);

        patient_name_event.setText("Patient: " + e.getPatient_name());
        appointment_type_event.setText("Type: " + e.getAppointment_type());
        }
    }

}
