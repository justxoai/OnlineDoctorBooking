package vn.edu.usth.doconcall.Patient.HealthCheck.Free_Appointment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class Free_Appointment_Adapter extends RecyclerView.Adapter<Free_Appointment_ViewHolder> {

    Context context;
    private List<Free_Appointment_Items> appointmentList;
    private OnItemClickListener listener;

    private int selectedPosition = -1;

    public Free_Appointment_Adapter(Context context, List<Free_Appointment_Items> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public Free_Appointment_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Free_Appointment_ViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_free_appointment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Free_Appointment_ViewHolder holder, int position) {
        Free_Appointment_Items item = appointmentList.get(position);

        holder.timeTextView.setText(item.getTime());

        if (position == selectedPosition) {
            holder.timeTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_navy));
            holder.timeTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.timeTextView.setBackgroundColor(Color.parseColor("#E2FFFC")); // Default color
            holder.timeTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                notifyDataSetChanged(); // Refresh list

                if (listener != null) {
                    listener.onItemClick(item.getTime());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String selectedTime);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}