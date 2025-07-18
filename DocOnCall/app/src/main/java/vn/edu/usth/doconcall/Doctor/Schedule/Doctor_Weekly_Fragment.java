package vn.edu.usth.doconcall.Doctor.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import vn.edu.usth.doconcall.Doctor.Schedule.Calendar.Doctor_Calendar_Adapter;
import vn.edu.usth.doconcall.Doctor.Schedule.Week_Event.Doctor_Event;
import vn.edu.usth.doconcall.Doctor.Schedule.Week_Event.Doctor_Event_Adapter;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;

public class Doctor_Weekly_Fragment extends Fragment implements Doctor_Calendar_Adapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_weekly_, container, false);

        calendarRecyclerView = v.findViewById(R.id.calendarRecyclerView);
        monthYearText = v.findViewById(R.id.monthYearTV);
        eventListView = v.findViewById(R.id.eventListView);

        setWeekView();

        week_fragment_function(v);

        return v;
    }

    private void setWeekView()
    {
        monthYearText.setText(Calendar_Utils.monthYearFromDate(Calendar_Utils.selectedDate));
        ArrayList<LocalDate> days = Calendar_Utils.daysInWeekArray(Calendar_Utils.selectedDate);

        Doctor_Calendar_Adapter calendarAdapter = new Doctor_Calendar_Adapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<Doctor_Event> dailyEvents = Doctor_Event.eventsForDate(Calendar_Utils.selectedDate);

        Collections.sort(dailyEvents, new Comparator<Doctor_Event>() {
            @Override
            public int compare(Doctor_Event e1, Doctor_Event e2) {
                return e1.getTime().compareTo(e2.getTime());
            }
        });

        Doctor_Event_Adapter eventAdapter = new Doctor_Event_Adapter(requireContext(), dailyEvents);

        eventListView.setAdapter(eventAdapter);
    }

    @Override
    public void onItemClick(int position, LocalDate date)
    {
        Calendar_Utils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void week_fragment_function(View v) {
        Button back_week_button = v.findViewById(R.id.back_week_button);
        back_week_button.setOnClickListener(view -> {
            Calendar_Utils.selectedDate = Calendar_Utils.selectedDate.minusWeeks(1);
            setWeekView();
        });

        Button next_week_button = v.findViewById(R.id.next_week_button);
        next_week_button.setOnClickListener(view -> {
            Calendar_Utils.selectedDate = Calendar_Utils.selectedDate.plusWeeks(1);
            setWeekView();
        });
    }
}