package vn.edu.usth.doconcall.Doctor.Appointment.Date;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class DateViewHolder extends RecyclerView.ViewHolder {

    TextView date_text;

    public DateViewHolder(@NonNull View itemView) {
        super(itemView);
        date_text = itemView.findViewById(R.id.date_text);
    }
}
