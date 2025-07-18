package vn.edu.usth.doconcall.Doctor.Dashboard.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class Appointment_Adapter extends RecyclerView.Adapter<Appointment_ViewHolder> {

    private Context context;
    private List<Appointment_Item> appointmentList;

    public Appointment_Adapter(Context context, List<Appointment_Item> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public Appointment_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.frame_appointment_doctor_db, parent, false);
        return new Appointment_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Appointment_ViewHolder holder, int position) {
        holder.bind(appointmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }
}
