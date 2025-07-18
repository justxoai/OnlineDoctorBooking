package vn.edu.usth.doconcall.Patient.Appointment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class Appointment_Adapter extends RecyclerView.Adapter<Appointment_ViewHolder> {

    Context context;

    List<Appointment_Items> items;

    public Appointment_Adapter(Context context, List<Appointment_Items> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Appointment_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Appointment_ViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_patient_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Appointment_ViewHolder holder, int position) {
        Appointment_Items item = items.get(position);

        holder.dateTextView.setText(items.get(position).getDate());
        holder.timeTextView.setText(items.get(position).getTime());
        holder.doctorNameTextView.setText(items.get(position).getDoctorName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Appointment_Details.class);

                i.putExtra("Doctor Id", item.getDoctor_id());
                i.putExtra("Availability Id", item.getAvailability_id());
                i.putExtra("Slot Id", item.getSlot_id());
                i.putExtra("Schedule Id", item.getSchedule_id());

                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
