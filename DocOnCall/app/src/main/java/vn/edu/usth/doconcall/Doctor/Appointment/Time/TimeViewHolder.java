package vn.edu.usth.doconcall.Doctor.Appointment.Time;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class TimeViewHolder extends RecyclerView.ViewHolder {

    TextView time_text;

    public TimeViewHolder(@NonNull View itemView) {
        super(itemView);
        time_text = itemView.findViewById(R.id.time_text);
    }
}
