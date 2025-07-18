package vn.edu.usth.doconcall.Doctor.Appointment.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Appointment.Date.DateAdapter;
import vn.edu.usth.doconcall.Doctor.Appointment.Date.DateItems;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.R;

public class ManageAppointment extends Fragment {

    private RecyclerView date_recycler;
    private DateAdapter adapter;
    private List<DateItems> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_appointment, container, false);

        // Available Date RecyclerView
        date_recyclerview(v);

        return v;
    }

    private void date_recyclerview(View v) {
        date_recycler = v.findViewById(R.id.recycler_availability);

        items = new ArrayList<DateItems>();

        fetch_date_list();

        adapter = new DateAdapter(v.getContext(), items);

        date_recycler.setAdapter(adapter);
        date_recycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
    }

    private void fetch_date_list() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<List<Doctor_AvailabilityDto>> doctorAvailabilityCall = doctorAvailabilityService.getAllByDoctorId(authHeader, doctorId);
            doctorAvailabilityCall.enqueue(new Callback<List<Doctor_AvailabilityDto>>() {
                @Override
                public void onResponse(Call<List<Doctor_AvailabilityDto>> call, Response<List<Doctor_AvailabilityDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Doctor_AvailabilityDto> availabilities = response.body();

                        sortAvailabilitiesByDate(availabilities);

                        items.clear();

                        for (Doctor_AvailabilityDto availability : availabilities) {
//
                            items.add(new DateItems(availability.getAvailability_date(), doctorId, availability.getAvailability_id()));
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e("GetAvailability", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<List<Doctor_AvailabilityDto>> call, Throwable t) {
                    Log.e("GetAvailability", "Error: " + t.getMessage(), t);
                }
            });


        } else {
            Log.e("GetAvailability", "Missing token or userId");
        }
    }

    private void sortAvailabilitiesByDate(List<Doctor_AvailabilityDto> availabilities) {
        if (availabilities == null || availabilities.isEmpty()) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        availabilities.sort(Comparator.comparing(dto ->
                LocalDate.parse(dto.getAvailability_date(), formatter)
        ));
    }
}