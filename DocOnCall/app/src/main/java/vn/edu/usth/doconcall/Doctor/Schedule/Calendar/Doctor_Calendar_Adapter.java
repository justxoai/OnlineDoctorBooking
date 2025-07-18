package vn.edu.usth.doconcall.Doctor.Schedule.Calendar;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import vn.edu.usth.doconcall.Doctor.Schedule.Week_Event.Doctor_Event;
import vn.edu.usth.doconcall.R;
import vn.edu.usth.doconcall.Utils.Calendar_Utils;

public class Doctor_Calendar_Adapter extends RecyclerView.Adapter<Doctor_Calendar_ViewHolder> {

    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;

    public Doctor_Calendar_Adapter(ArrayList<LocalDate> days, OnItemListener onItemListener)
    {
        this.days = days;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public Doctor_Calendar_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.frame_monthly, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if(days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else // week view
            layoutParams.height = (int) parent.getHeight();

        return new Doctor_Calendar_ViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull Doctor_Calendar_ViewHolder holder, int position)
    {
        final LocalDate date = days.get(position);

        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));

        if(date.equals(Calendar_Utils.selectedDate)){
            holder.parentView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.parentView.setBackgroundColor(Color.TRANSPARENT); // reset background
        }

        if(date.getMonth().equals(Calendar_Utils.selectedDate.getMonth()))
            holder.dayOfMonth.setTextColor(Color.BLACK);
        else
            holder.dayOfMonth.setTextColor(Color.LTGRAY);

        if(!Doctor_Event.eventsForDate(date).isEmpty()){
            holder.dayOfMonth.setTextColor(Color.RED); // override color if event exists
        }
    }

    @Override
    public int getItemCount()
    {
        return days.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, LocalDate date);
    }
}
