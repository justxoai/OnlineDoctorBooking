package vn.edu.usth.doconcall.Doctor.HealthCheck.List_Patient;

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

import vn.edu.usth.doconcall.Doctor.HealthCheck.Fragment.Patient_Information_Fragment;
import vn.edu.usth.doconcall.R;

public class Patient_Adapter extends RecyclerView.Adapter<Patient_ViewHolder> {

    Context context;
    List<Patient_Item> items;

    public Patient_Adapter(Context context, List<Patient_Item> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Patient_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Patient_ViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_list_patient,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Patient_ViewHolder holder, int position) {
        Patient_Item item = items.get(position);

        holder.name.setText(items.get(position).getName());
        holder.gender.setText(items.get(position).getGender());
        holder.dob.setText(items.get(position).getDob());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the fragment
                Fragment selectedFragment = new Patient_Information_Fragment();

                // Pass data using Bundle
                Bundle bundle = new Bundle();
                bundle.putInt("Patient Id", item.getId());
                bundle.putString("Patient Name", item.getName());
                bundle.putString("Patient Gender", item.getGender());
                bundle.putString("Patient Dob", item.getDob());

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
