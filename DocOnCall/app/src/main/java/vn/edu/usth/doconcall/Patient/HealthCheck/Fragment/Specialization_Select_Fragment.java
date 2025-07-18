package vn.edu.usth.doconcall.Patient.HealthCheck.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.doconcall.Patient.HealthCheck.Specialization.SpecializationAdapter;
import vn.edu.usth.doconcall.Patient.HealthCheck.Specialization.SpecializationItems;
import vn.edu.usth.doconcall.R;

public class Specialization_Select_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_specialization__select_, container, false);

        // Specialization RecyclerView Function
        setupRecyclerView(v);

        // Button Function
        setupBackButton(v);

        return v;
    }

    private void setupBackButton(View v) {
        ImageButton backButton = v.findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void setupRecyclerView(View v) {
        RecyclerView specialization = v.findViewById(R.id.recycler_view_specialization_fragment);

        List<SpecializationItems> items = new ArrayList<>();

        items.add(new SpecializationItems("General Internal Medicine - Nội khoa tổng quát"));
        items.add(new SpecializationItems("Cardiology - Tim mạch"));
        items.add(new SpecializationItems("Pulmonology - Hô hấp"));
        items.add(new SpecializationItems("Endocrinology - Nội tiết"));
        items.add(new SpecializationItems("Gastroenterology - Tiêu hóa"));
        items.add(new SpecializationItems("Nephrology - Thận học"));
        items.add(new SpecializationItems("Hematology - Huyết học"));
        items.add(new SpecializationItems("Neurology - Thần kinh"));
        items.add(new SpecializationItems("Psychiatry - Tâm thần học"));
        items.add(new SpecializationItems("Dermatology - Da liễu"));
        items.add(new SpecializationItems("Obstetrics and Gynecology - Sản phụ khoa"));
        items.add(new SpecializationItems("Pediatrics - Nhi khoa"));
        items.add(new SpecializationItems("Urology - Tiết niệu"));
        items.add(new SpecializationItems("Ophthalmology - Mắt"));
        items.add(new SpecializationItems("Otolaryngology - Tai Mũi Họng"));
        items.add(new SpecializationItems("Allergy and Immunology - Dị ứng và miễn dịch"));



        SpecializationAdapter adapter = new SpecializationAdapter(requireContext(), items);

        specialization.setAdapter(adapter);
        specialization.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}