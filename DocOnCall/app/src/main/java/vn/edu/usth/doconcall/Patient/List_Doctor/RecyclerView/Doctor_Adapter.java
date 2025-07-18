package vn.edu.usth.doconcall.Patient.List_Doctor.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.Patient.List_Doctor.Doctor_Information;
import vn.edu.usth.doconcall.R;

public class Doctor_Adapter extends RecyclerView.Adapter<Doctor_ViewHolder> {

    Context context;
    List<Doctor_Items> items;

    public Doctor_Adapter(Context context, List<Doctor_Items> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Doctor_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Doctor_ViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_list_doctor,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Doctor_ViewHolder holder, int position) {
        Doctor_Items item = items.get(position);

        holder.doctor_name.setText(items.get(position).getName());
        holder.doctor_specialization.setText(items.get(position).getSpecialization());
        holder.doctor_image.setImageResource(items.get(position).getDoctor_image());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Doctor_Information.class);

                i.putExtra("Doctor Id", item.getId());
                i.putExtra("Doctor Name", item.getName());
                i.putExtra("Doctor Specialization", item.getSpecialization());
                i.putExtra("Doctor Image", item.getDoctor_image());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}