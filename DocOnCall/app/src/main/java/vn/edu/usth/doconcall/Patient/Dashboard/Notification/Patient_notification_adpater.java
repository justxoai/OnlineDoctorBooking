package vn.edu.usth.doconcall.Patient.Dashboard.Notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.R;

public class Patient_notification_adpater extends RecyclerView.Adapter<Patient_notification_viewholder> {

    Context context;
    List<Patient_notification_item> items;

    public Patient_notification_adpater(Context context, List<Patient_notification_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Patient_notification_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Patient_notification_viewholder(LayoutInflater.from(context).inflate(R.layout.frame_notificaton, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Patient_notification_viewholder holder, int position) {
        Patient_notification_item item = items.get(position);

        holder.header.setText(items.get(position).getHeader());
        holder.content.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}