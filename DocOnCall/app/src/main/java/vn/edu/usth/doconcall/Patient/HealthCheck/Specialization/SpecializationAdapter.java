package vn.edu.usth.doconcall.Patient.HealthCheck.Specialization;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.doconcall.Patient.HealthCheck.Fragment.Doctor_Select_Fragment;
import vn.edu.usth.doconcall.R;

public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationViewHolder>{

    Context context;

    List<SpecializationItems> items;

    public SpecializationAdapter(Context context, List<SpecializationItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SpecializationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SpecializationViewHolder(LayoutInflater.from(context).inflate(R.layout.frame_specialization, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpecializationViewHolder holder, int position) {
        SpecializationItems item = items.get(position);

        holder.doctor_specialization.setText(item.getName());

        holder.itemView.setOnClickListener(view -> {
            Fragment selectedFragment = new Doctor_Select_Fragment();

            // Pass data using Bundle
            Bundle bundle = new Bundle();
            bundle.putString("Doctor Specialization", item.getName());

            selectedFragment.setArguments(bundle);

            // Now do fragment transaction
            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(android.R.id.content, selectedFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
