package vn.edu.usth.doconcall.Doctor.Dashboard.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class Doctor_notification_adapter extends RecyclerView.Adapter<Doctor_notification_viewholder> {

    Context context;
    List<Doctor_notification_items> items;

    public Doctor_notification_adapter(Context context, List<Doctor_notification_items> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Doctor_notification_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Doctor_notification_viewholder(LayoutInflater.from(context).inflate(R.layout.frame_notificaton, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Doctor_notification_viewholder holder, int position) {
        Doctor_notification_items item = items.get(position);

        holder.header.setText(items.get(position).getHeader());
        holder.content.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}