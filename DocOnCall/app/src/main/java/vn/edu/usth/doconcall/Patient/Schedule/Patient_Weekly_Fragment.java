package vn.edu.usth.doconcall.Patient.Schedule;

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

import vn.edu.usth.doconcall.Patient.Schedule.Calendar.Patient_Calendar_Adapter;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;
import vn.edu.usth.doconcall.Patient.Schedule.Week_Event.Patient_Event;
import vn.edu.usth.doconcall.Patient.Schedule.Week_Event.Patient_Event_Adapter;
import vn.edu.usth.doconcall.R;

public class Patient_Weekly_Fragment extends Fragment implements Patient_Calendar_Adapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_weekly_, container, false);

        calendarRecyclerView = v.findViewById(R.id.calendarRecyclerView);
        monthYearText = v.findViewById(R.id.monthYearTV);
        eventListView = v.findViewById(R.id.eventListView);

        setWeekView();

        week_fragment_function(v);

        return v;
    }

    private void setWeekView()
    {
        monthYearText.setText(Calendar_Utils.dayMonthFromDate(Calendar_Utils.selectedDate));
        ArrayList<LocalDate> days = Calendar_Utils.daysInWeekArray(Calendar_Utils.selectedDate);

        Patient_Calendar_Adapter calendarAdapter = new Patient_Calendar_Adapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<Patient_Event> dailyEvents = Patient_Event.eventsForDate(Calendar_Utils.selectedDate);

        Collections.sort(dailyEvents, new Comparator<Patient_Event>() {
            @Override
            public int compare(Patient_Event e1, Patient_Event e2) {
                return e1.getTime().compareTo(e2.getTime());
            }
        });

        Patient_Event_Adapter eventAdapter = new Patient_Event_Adapter(requireContext(), dailyEvents);

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