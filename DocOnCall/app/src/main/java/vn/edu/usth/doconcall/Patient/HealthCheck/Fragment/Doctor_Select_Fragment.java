package vn.edu.usth.doconcall.Patient.HealthCheck.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Patient.HealthCheck.SelectDoctor.Select_Doctor_Adapter;
import vn.edu.usth.doconcall.Patient.HealthCheck.SelectDoctor.Select_Doctor_Items;
import vn.edu.usth.doconcall.R;

public class Doctor_Select_Fragment extends Fragment {

    private List<Select_Doctor_Items> filteredItems;
    private Select_Doctor_Adapter adapter;
    private SearchView doctorSearch;
    private RecyclerView doctorRecycler;
    private String selectedSpecialization = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor__select_, container, false);

        Bundle args = getArguments();
        if (args != null) {
            selectedSpecialization = args.getString("Doctor Specialization").split(" - ")[0];
        }

        // Doctor RecyclerView Function
        setupRecyclerView(v);

        // Button Function
        setupBackButton(v);

        return v;
    }

    private void setupRecyclerView(View v) {
        doctorSearch = v.findViewById(R.id.doctor_searchView_fragment);
        doctorSearch.clearFocus();

        doctorRecycler = v.findViewById(R.id.recycler_view_doctor_fragment);

        filteredItems = new ArrayList<>();

        fetch_doctor_list(null);

        adapter = new Select_Doctor_Adapter(requireContext(), filteredItems);

        doctorRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        doctorRecycler.setAdapter(adapter);

        doctorSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetch_doctor_list(newText);
                return true;
            }
        });
    }

    private void fetch_doctor_list(String query) {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
            Call<List<DoctorDto>> call = doctorService.getAllDoctors(authHeader, query);
            call.enqueue(new Callback<List<DoctorDto>>() {
                @Override
                public void onResponse(Call<List<DoctorDto>> call, Response<List<DoctorDto>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        List<DoctorDto> doctors = response.body();
                        filteredItems.clear();
                        for (DoctorDto doctor : doctors) {
                            if (selectedSpecialization == null || doctor.getSpecialization().equalsIgnoreCase(selectedSpecialization)) {
                                filteredItems.add(new Select_Doctor_Items(doctor.getId(), doctor.getName(), "", R.drawable.profile));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("DoctorSelectFragment", "No response");
                    }
                }

                @Override
                public void onFailure(Call<List<DoctorDto>> call, Throwable t) {
                    Log.e("DoctorSelectFragment", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("DoctorSelectFragment", "Missing Token");
        }
    }

    private void setupBackButton(View v) {
        ImageButton backButton = v.findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());
    }
}
