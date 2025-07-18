package vn.edu.usth.doconcall.Doctor.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import vn.edu.usth.doconcall.Doctor.Schedule.Week_Event.Doctor_Event;
import vn.edu.usth.doconcall.Doctor.Schedule.Daily_Hour.Doctor_HourEvent;
import vn.edu.usth.doconcall.Doctor.Schedule.Daily_Hour.Doctor_Hour_Adapter;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;
import vn.edu.usth.doconcall.R;

public class Doctor_Daily_Fragment extends Fragment {

    private ListView hourListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_daily_, container, false);

        hourListView = v.findViewById(R.id.hourListView);

        TextView test_function = v.findViewById(R.id.testFunction);
        test_function.setText(Calendar_Utils.weekdayDateMonthFromDate(Calendar_Utils.selectedDate));

        return v;
    }

    private void setHourAdapter()
    {
        Doctor_Hour_Adapter hourAdapter = new Doctor_Hour_Adapter(requireContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<Doctor_HourEvent> hourEventList()
    {
        ArrayList<Doctor_HourEvent> list = new ArrayList<>();

        for(int hour = 7; hour < 18; hour++)
        {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Doctor_Event> events = Doctor_Event.eventsForDateAndTime(LocalDate.now(), time);

            list.add(new Doctor_HourEvent(time, events));
        }

        return list;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setHourAdapter();
    }

}