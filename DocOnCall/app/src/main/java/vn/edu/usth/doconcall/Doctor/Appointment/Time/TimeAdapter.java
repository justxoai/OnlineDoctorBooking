package vn.edu.usth.doconcall.Doctor.Appointment.Time;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class TimeAdapter extends RecyclerView.Adapter<TimeViewHolder>{

    Context context;

    List<TimeItems> items;

    public TimeAdapter(Context context, List<TimeItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_availability_time_slot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        TimeItems item = items.get(position);

        holder.time_text.setText(item.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Manage_Availability.class);

                i.putExtra("Current Time", item.getTime());
                i.putExtra("Doctor Id", item.getDoctor_id());
                i.putExtra("Availability Id", item.getAvailability_id());
                i.putExtra("Slot Id", item.getSlot_id());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
