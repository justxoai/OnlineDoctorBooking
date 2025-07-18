package vn.edu.usth.doconcall.Doctor.Dashboard.Notification;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class Doctor_notification_viewholder extends RecyclerView.ViewHolder {

    TextView header, content;

    public Doctor_notification_viewholder(@NonNull View view){
        super(view);
        header = view.findViewById(R.id.notification_header);
        content = view.findViewById(R.id.notification_content);
    }
}
