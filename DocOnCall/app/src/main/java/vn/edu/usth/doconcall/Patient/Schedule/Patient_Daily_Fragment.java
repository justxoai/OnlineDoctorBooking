package vn.edu.usth.doconcall.Patient.Schedule;

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
import java.util.Random;

import vn.edu.usth.doconcall.Utils.Calendar_Utils;
import vn.edu.usth.doconcall.Patient.Schedule.Week_Event.Patient_Event;
import vn.edu.usth.doconcall.Patient.Schedule.Daily_Hour.Patient_HourEvent;
import vn.edu.usth.doconcall.Patient.Schedule.Daily_Hour.Patient_Hour_Adapter;
import vn.edu.usth.doconcall.R;

public class Patient_Daily_Fragment extends Fragment {

    private ListView hourListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Random random = new Random();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_daily_, container, false);

        hourListView = v.findViewById(R.id.hourListView);

        TextView test_function = v.findViewById(R.id.testFunction);
        test_function.setText(Calendar_Utils.weekdayDateMonthFromDate(LocalDate.now()));

        return v;
    }

    private void setHourAdapter() {
        Patient_Hour_Adapter hourAdapter = new Patient_Hour_Adapter(requireContext(), hourEventList());
        hourListView.setAdapter(hourAdapter);
    }

    private ArrayList<Patient_HourEvent> hourEventList() {
        ArrayList<Patient_HourEvent> list = new ArrayList<>();

        for (int hour = 7; hour < 18; hour++) {
            LocalTime time = LocalTime.of(hour, 0);
            ArrayList<Patient_Event> events = Patient_Event.eventsForDateAndTime(LocalDate.now(), time);

            list.add(new Patient_HourEvent(time, events));
        }

        return list;
    }


    @Override
    public void onResume() {
        super.onResume();
        setHourAdapter();
    }
}