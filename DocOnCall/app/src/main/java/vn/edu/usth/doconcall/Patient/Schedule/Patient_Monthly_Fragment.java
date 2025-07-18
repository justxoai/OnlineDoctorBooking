package vn.edu.usth.doconcall.Patient.Schedule;

import static vn.edu.usth.doconcall.Utils.Calendar_Utils.daysInMonthArray;
import static vn.edu.usth.doconcall.Utils.Calendar_Utils.monthYearFromDate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import vn.edu.usth.doconcall.Patient.Schedule.Calendar.Patient_Calendar_Adapter;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;
import vn.edu.usth.doconcall.R;

public class Patient_Monthly_Fragment extends Fragment implements Patient_Calendar_Adapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_monthly_, container, false);

        calendarRecyclerView = v.findViewById(R.id.calendarRecyclerView);
        monthYearText = v.findViewById(R.id.monthYearTV);

        Calendar_Utils.selectedDate = LocalDate.now();
        setMonthView();

        month_fragment_function(v);

        return v;
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(Calendar_Utils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        Patient_Calendar_Adapter calendarAdapter = new Patient_Calendar_Adapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            Calendar_Utils.selectedDate = date;
            setMonthView();
        }
    }

    private void month_fragment_function(View v) {
        Button back_month_button = v.findViewById(R.id.back_month_button);
        back_month_button.setOnClickListener(view -> {
            Calendar_Utils.selectedDate = Calendar_Utils.selectedDate.minusMonths(1);
            setMonthView();
        });

        Button next_month_button = v.findViewById(R.id.next_month_button);
        next_month_button.setOnClickListener(view -> {
            Calendar_Utils.selectedDate = Calendar_Utils.selectedDate.plusMonths(1);
            setMonthView();
        });
    }

}