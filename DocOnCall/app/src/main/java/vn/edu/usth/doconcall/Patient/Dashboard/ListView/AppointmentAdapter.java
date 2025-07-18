package vn.edu.usth.doconcall.Patient.Dashboard.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentViewHolder> {

    private Context context;
    private List<AppointmentItem> appointmentList;

    public AppointmentAdapter(Context context, List<AppointmentItem> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.frame_appointment_patient_db, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.bind(appointmentList.get(position));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }
}
