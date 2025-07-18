package vn.edu.usth.doconcall.Patient.HealthCheck.SelectDoctor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.Patient.HealthCheck.Fragment.Appointment_Select_Fragment;
import vn.edu.usth.doconcall.R;

public class Select_Doctor_Adapter extends RecyclerView.Adapter<Select_Doctor_ViewHolder> {

    Context context;
    List<Select_Doctor_Items> items;

    public Select_Doctor_Adapter(Context context, List<Select_Doctor_Items> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Select_Doctor_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Select_Doctor_ViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_list_doctor,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Select_Doctor_ViewHolder holder, int position) {
        Select_Doctor_Items item = items.get(position);

        holder.doctor_name.setText(items.get(position).getName());
        holder.doctor_specialization.setText(items.get(position).getSpecialization());
        holder.doctor_image.setImageResource(items.get(position).getDoctor_image());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the fragment
                Fragment selectedFragment = new Appointment_Select_Fragment();

                // Pass data using Bundle
                Bundle bundle = new Bundle();
                bundle.putInt("Doctor Id", item.getId());
                bundle.putString("Doctor Name", item.getName());
                bundle.putString("Doctor Specialization", item.getSpecialization());
                bundle.putInt("Doctor Image", item.getDoctor_image());


                selectedFragment.setArguments(bundle);

                // Now do fragment transaction
                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(android.R.id.content, selectedFragment); // make sure this ID is correct
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
