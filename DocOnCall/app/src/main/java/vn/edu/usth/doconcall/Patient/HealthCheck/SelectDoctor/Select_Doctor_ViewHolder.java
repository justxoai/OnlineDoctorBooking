package vn.edu.usth.doconcall.Patient.HealthCheck.SelectDoctor;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.usth.doconcall.R;

public class Select_Doctor_ViewHolder extends RecyclerView.ViewHolder {

    TextView doctor_name, doctor_specialization;

    ImageView doctor_image;

    public  Select_Doctor_ViewHolder(@NonNull View view){
        super(view);
        doctor_name = view.findViewById(R.id.doctor_name);
        doctor_specialization = view.findViewById(R.id.doctor_specialization);
        doctor_image = view.findViewById(R.id.doctor_image);
    }

}
