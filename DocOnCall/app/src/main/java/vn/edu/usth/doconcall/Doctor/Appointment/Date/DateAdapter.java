package vn.edu.usth.doconcall.Doctor.Appointment.Date;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class DateAdapter extends RecyclerView.Adapter<DateViewHolder>{

    Context context;

    List<DateItems> items;

    public DateAdapter(Context context, List<DateItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DateViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_availability_date, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateItems item = items.get(position);

        holder.date_text.setText(item.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Time_Availability.class);

                i.putExtra("Doctor Id", item.getDoctor_id());
                i.putExtra("Availability Id", item.getAvail_id());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
