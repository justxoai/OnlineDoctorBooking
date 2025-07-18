package vn.edu.usth.doconcall.Patient.HealthCheck.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Patient.HealthCheck.Free_Appointment.Free_Appointment_Adapter;
import vn.edu.usth.doconcall.Patient.HealthCheck.Free_Appointment.Free_Appointment_Items;
import vn.edu.usth.doconcall.R;

public class Appointment_Select_Fragment extends Fragment {

    private TextView doctor_name, doctor_spec, doctor_work, doctor_year;
    private ImageView doctor_image;
    private RecyclerView date_recyclerview, time_recyclerview;
    private Free_Appointment_Adapter time_adapter, date_adapter;
    private Bundle bundle, bundle_id;
    private String selectedType = "";
    private String selectedDate = null;

    private String doctorName, specialization;
    private CheckBox checkboxOnlineMeeting, checkboxInPersonMeeting;
    private List<Free_Appointment_Items> dateList, timeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Layout
        View v = inflater.inflate(R.layout.fragment_appointment__select_, container, false);

        bundle = new Bundle();

        bundle_id = getArguments();

        // Doctor Profile
        doctor_name = v.findViewById(R.id.doctor_name_information);
        doctor_spec = v.findViewById(R.id.doctor_specialization_information);
        doctor_work = v.findViewById(R.id.doctor_work_process);
        doctor_year = v.findViewById(R.id.doctor_year_experience);

        doctor_image = v.findViewById(R.id.doctor_image_information);

        // Checkbox
        checkboxOnlineMeeting = v.findViewById(R.id.checkbox_online_meeting);
        checkboxInPersonMeeting = v.findViewById(R.id.checkbox_in_person_meeting);

        // Check box conditional:
        checkboxOnlineMeeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxInPersonMeeting.setChecked(false);
                    selectedType = "Online Meeting";
                }
            }
        });

        checkboxInPersonMeeting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxOnlineMeeting.setChecked(false);
                    selectedType = "In Person Meeting";
                }
            }
        });

        // Set Doctor Information
        fetch_doctor_information(v);

        // Appointment Select Fragment Function
        appointment_select_function(v);

        // Date and Time RecyclerView
        free_date_recycler_view(v);

        return v;
    }

    private void free_date_recycler_view(View v) {
        date_recyclerview = v.findViewById(R.id.free_date_recycler_view);
        time_recyclerview = v.findViewById(R.id.free_time_recycler_view);
        time_recyclerview.setVisibility(View.GONE);

        dateList = new ArrayList<Free_Appointment_Items>();

        fetch_date_list(v);

        date_adapter = new Free_Appointment_Adapter(requireContext(), dateList);

        date_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        date_recyclerview.setAdapter(date_adapter);

        date_adapter.setOnItemClickListener(new Free_Appointment_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String clickedDate) {
                if (clickedDate.equals(selectedDate)) {

                    selectedDate = null;

                    time_recyclerview.setVisibility(View.GONE);
                    v.findViewById(R.id.select_time_text).setVisibility(View.GONE);

                    Toast.makeText(getContext(), "No date selected", Toast.LENGTH_SHORT).show();
                } else {
                    selectedDate = clickedDate;

                    free_time_recycler_view(v);

                    time_recyclerview.setVisibility(View.VISIBLE);
                    v.findViewById(R.id.select_time_text).setVisibility(View.VISIBLE);

                    Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
                    bundle.putString("Appointment Date", selectedDate);
                }


            }
        });
    }

    private void fetch_date_list(View v) {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = bundle_id.getInt("Doctor Id");

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<List<Doctor_AvailabilityDto>> call = doctorAvailabilityService.getAllByDoctorId(authHeader, doctorId);
            call.enqueue(new Callback<List<Doctor_AvailabilityDto>>() {
                @Override
                public void onResponse(Call<List<Doctor_AvailabilityDto>> call, Response<List<Doctor_AvailabilityDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Doctor_AvailabilityDto> availabilities = response.body();

                        sortAvailabilitiesByDate(availabilities);

                        dateList.clear();

                        for (Doctor_AvailabilityDto availability : availabilities) {
                            boolean hasAvailableSlot = false;

                            for (AvailabilitySlotDto slot : availability.getSlots()) {
                                if ("AVAILABLE".equalsIgnoreCase(slot.getSlot_status())) {
                                    hasAvailableSlot = true;
                                    break;
                                }
                            }

                            String formattedDate = filterAndFormatDate(availability.getAvailability_date());

                            if (hasAvailableSlot && formattedDate != null) {
                                dateList.add(new Free_Appointment_Items(formattedDate));
                            }

                        }

                        date_adapter.notifyDataSetChanged();

                        TextView noDateText = v.findViewById(R.id.no_date_text);
                        Button confirmButton = v.findViewById(R.id.confirm_button);

                        if (dateList.isEmpty()) {
                            noDateText.setVisibility(View.VISIBLE);
                            v.findViewById(R.id.free_date_recycler_view).setVisibility(View.GONE);
                            v.findViewById(R.id.free_time_recycler_view).setVisibility(View.GONE);
                            v.findViewById(R.id.appointment_type_card).setVisibility(View.GONE);
                            v.findViewById(R.id.select_date_text).setVisibility(View.GONE);
                            confirmButton.setVisibility(View.GONE);
                        } else {
                            noDateText.setVisibility(View.GONE);
                            confirmButton.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Log.e("AppointmentASelectFragment", "No response in Date");
                    }
                }

                @Override
                public void onFailure(Call<List<Doctor_AvailabilityDto>> call, Throwable t) {
                    Log.e("AppointmentASelectFragment", "Error: " + t.getMessage(), t);
                }
            });


        } else {
            Log.e("AppointmentASelectFragment", "Missing token or doctorId in Fetch Date");
        }

    }

    private void free_time_recycler_view(View v) {
        time_recyclerview = v.findViewById(R.id.free_time_recycler_view);

        timeList = new ArrayList<Free_Appointment_Items>();

        fetch_time_list();

        timeList.add(new Free_Appointment_Items(""));

        time_recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        time_adapter = new Free_Appointment_Adapter(requireContext(), timeList);
        time_recyclerview.setAdapter(time_adapter);

        time_adapter.setOnItemClickListener(new Free_Appointment_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String selectedTime) {
                Toast.makeText(getContext(), "Selected Time: " + selectedTime, Toast.LENGTH_SHORT).show();

                bundle.putString("Appointment Time", selectedTime);
            }
        });

    }

    private void fetch_time_list() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = bundle_id.getInt("Doctor Id");

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<List<Doctor_AvailabilityDto>> call = doctorAvailabilityService.getAllByDoctorId(authHeader, doctorId);

            call.enqueue(new Callback<List<Doctor_AvailabilityDto>>() {
                @Override
                public void onResponse(Call<List<Doctor_AvailabilityDto>> call, Response<List<Doctor_AvailabilityDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Doctor_AvailabilityDto> availabilities = response.body();
                        timeList.clear();

                        for (Doctor_AvailabilityDto availability : availabilities) {
                            String formattedDate = filterAndFormatDate(availability.getAvailability_date());
                            if (formattedDate != null && formattedDate.equals(selectedDate)) {
                                for (AvailabilitySlotDto slot : availability.getSlots()) {
                                    if ("AVAILABLE".equalsIgnoreCase(slot.getSlot_status())) {
                                        timeList.add(new Free_Appointment_Items(slot.getSlot_time()));
                                    }
                                }
                                break; // Only process the matching date
                            }
                        }
                        time_adapter.notifyDataSetChanged();

                    } else {
                        Log.e("AppointmentSelectFragment", "Failed to fetch time slots: response invalid");
                    }
                }

                @Override
                public void onFailure(Call<List<Doctor_AvailabilityDto>> call, Throwable t) {
                    Log.e("AppointmentSelectFragment", "Error fetching time slots: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("AppointmentSelectFragment", "Missing token or doctorId in Fetch time");
        }

    }

    private void fetch_doctor_information(View v) {
        Bundle bundle = getArguments();

        String doctorName = bundle.getString("Doctor Name");
        String specialization = bundle.getString("Doctor Specialization");
        int imageResId = bundle.getInt("Doctor Image");

        doctor_name.setText(doctorName);
        doctor_spec.setText(specialization);
        doctor_image.setImageResource(imageResId);

        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = bundle.getInt("Doctor Id");

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
            Call<DoctorDto> call = doctorService.getDoctorById(authHeader, doctorId);
            call.enqueue(new Callback<DoctorDto>() {
                @Override
                public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        DoctorDto doctor = response.body();
                        doctor_year.setText(doctor.getYear_experience() + " years of experience");
                        doctor_work.setText(doctor.getWork_experience().replace("; ", "\n").trim());
                    } else {
                        Log.e("AppointmentSelectFragment", "Missing token or userId");
                    }
                }

                @Override
                public void onFailure(Call<DoctorDto> call, Throwable t) {
                    Log.e("AppointmentSelectFragment", "Missing token or userId");
                }
            });

        } else {
            Log.e("AppointmentSelectFragment", "Missing token or userId");
        }
    }

    private void appointment_select_function(View v) {
        // Back
        ImageButton back_button = v.findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Continue
        Button continue_button = v.findViewById(R.id.confirm_button);
        continue_button.setOnClickListener(view -> {

            if (!(checkboxInPersonMeeting.isChecked() || checkboxOnlineMeeting.isChecked())) {
                Toast.makeText(requireContext(), "Please select Appointment Type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedDate == null || bundle.getString("Appointment Time") == null) {
                Toast.makeText(requireContext(), "Please select both date and time", Toast.LENGTH_SHORT).show();
                return;
            }

            doctorName = doctor_name.getText().toString();
            specialization = doctor_spec.getText().toString();
            int doctorId = bundle_id.getInt("Doctor Id");

            // Bundle the data
            bundle.putInt("Doctor Id", doctorId);
            bundle.putString("Doctor Name", doctorName);
            bundle.putString("Specialization", specialization);
            bundle.putString("Appointment Type", selectedType);

            // Navigate to Confirm_Fragment
            Fragment confirm_fragment = new Confirm_Fragment();
            confirm_fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(android.R.id.content, confirm_fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    private String filterAndFormatDate(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        Date currentDate = new Date();

        try {
            Date parsedDate = inputFormat.parse(dateStr);
            if (parsedDate != null) {
                int year = Integer.parseInt(dateStr.substring(0, 4));
                if (year == 2025 && !parsedDate.before(currentDate)) {
                    return outputFormat.format(parsedDate);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null; // Invalid or not matching
    }

    private void sortAvailabilitiesByDate(List<Doctor_AvailabilityDto> availabilities) {
        if (availabilities == null || availabilities.isEmpty()) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        availabilities.sort(Comparator.comparing(dto ->
                LocalDate.parse(dto.getAvailability_date(), formatter)
        ));
    }

}