package vn.edu.usth.doconcall.Doctor.Appointment.Date;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Appointment.Time.TimeAdapter;
import vn.edu.usth.doconcall.Doctor.Appointment.Time.TimeItems;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Patient.Appointment.Appointment_Details;
import vn.edu.usth.doconcall.R;

public class Time_Availability extends AppCompatActivity {

    private static final String CHANEL_ID = "doc_on_call";
    private RecyclerView time_slot_recycler;
    private TimeAdapter adapter;
    private List<TimeItems> items;
    private MaterialButton delete_all_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_availability);

        // Time Slot RecyclerView
        time_slot_recyclerview();

        button_function();
    }

    private void button_function() {
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> {
            onBackPressed();
        });

        delete_all_button = findViewById(R.id.delete_all_button);
        delete_all_button.setOnClickListener(v -> {
            delete_all_time_slots();
        });
    }

    private void delete_all_time_slots() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = getIntent().getIntExtra("Doctor Id", 0);
        int availabilityId = getIntent().getIntExtra("Availability Id", 0);

        if (token != null && !token.isEmpty() && doctorId != -1 && availabilityId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<Doctor_AvailabilityDto> call = doctorAvailabilityService.getAvailabilityById(authHeader, availabilityId);
            call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                @Override
                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        List<AvailabilitySlotDto> slots = response.body().getSlots();

                        if (slots.isEmpty()) {
                            TextView no_time_slots_text = findViewById(R.id.no_time_slots_text);
                            no_time_slots_text.setVisibility(View.VISIBLE);

                            time_slot_recycler.setVisibility(View.GONE);

                        } else {
                            Doctor_AvailabilityDto update_dto = new Doctor_AvailabilityDto();
                            update_dto.setDoctor_id(doctorId);
                            update_dto.setAvailability_date(response.body().getAvailability_date());
                            update_dto.setSlots(new ArrayList<>());

                            Call<Doctor_AvailabilityDto> update_call = doctorAvailabilityService.updateByIdAndDate(authHeader, doctorId, response.body().getAvailability_date(), update_dto);
                            update_call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                                @Override
                                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Toast.makeText(Time_Availability.this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                                        displayNotification("You have delete all timeslots of availability!", "Delete Availability");
                                        onBackPressed();
                                    } else {
                                        Toast.makeText(Time_Availability.this, "Delete Failed!", Toast.LENGTH_SHORT).show();
                                        Log.e("DeleteAllTimeSlot", "No response body");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                    Log.e("DeleteAllTimeSlot", "Error: " + t.getMessage(), t);
                                }
                            });

                        }

                    } else {
                        Log.e("DeleteAllTimeSlot", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                    Log.e("DeleteAllTimeSlot", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("DeleteAllTimeSlot", "Missing token or userId");
        }

    }

    private void time_slot_recyclerview() {
        time_slot_recycler = findViewById(R.id.recycler_time_slot);

        items = new ArrayList<TimeItems>();

        fetch_time_slots_list();

        adapter = new TimeAdapter(this, items);

        time_slot_recycler.setAdapter(adapter);
        time_slot_recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetch_time_slots_list() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = getIntent().getIntExtra("Doctor Id", 0);
        int availabilityId = getIntent().getIntExtra("Availability Id", 0);

        if (token != null && !token.isEmpty() && doctorId != -1 && availabilityId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<Doctor_AvailabilityDto> call = doctorAvailabilityService.getAvailabilityById(authHeader, availabilityId);
            call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                @Override
                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        TextView availability_date = findViewById(R.id.availability_date);
                        availability_date.setText(response.body().getAvailability_date());

                        List<AvailabilitySlotDto> slots = response.body().getSlots();

                        if (slots.isEmpty()) {
                            TextView no_time_slots_text = findViewById(R.id.no_time_slots_text);
                            no_time_slots_text.setVisibility(View.VISIBLE);

                            time_slot_recycler.setVisibility(View.GONE);

                        } else {
                            items.clear();

                            Collections.sort(slots, new Comparator<AvailabilitySlotDto>() {
                                final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                                @Override
                                public int compare(AvailabilitySlotDto o1, AvailabilitySlotDto o2) {
                                    try {
                                        return sdf.parse(o1.getSlot_time()).compareTo(sdf.parse(o2.getSlot_time()));
                                    } catch (ParseException e) {
                                        return o1.getSlot_time().compareTo(o2.getSlot_time()); // fallback
                                    }
                                }
                            });

                            for (AvailabilitySlotDto slot : slots) {
                                items.add(new TimeItems(slot.getSlot_time(), doctorId, availabilityId, slot.getSlot_id()));
                            }

                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        Log.e("GetTimeSlots", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                    Log.e("GetTimeSlots", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("GetTimeSlots", "Missing token or userId");
        }
    }

    private void displayNotification(String message, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Time_Availability.this, CHANEL_ID)
                .setSmallIcon(R.drawable.doc_logo_v2)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(Time_Availability.this);
        mNotificationMgr.notify(1, builder.build());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}