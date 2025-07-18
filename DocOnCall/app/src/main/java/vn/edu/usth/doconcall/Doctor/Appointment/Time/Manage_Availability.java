package vn.edu.usth.doconcall.Doctor.Appointment.Time;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Doctor.Appointment.Date.Time_Availability;
import vn.edu.usth.doconcall.Doctor.Appointment.DoctorManageAppointment;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.R;

public class Manage_Availability extends AppCompatActivity {

    private static final String CHANEL_ID = "doc_on_call";
    private MaterialButton update_button, delete_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_availability);

        TextView current_time_slot = findViewById(R.id.current_time_slot);
        current_time_slot.setText("Current Time: " + getIntent().getStringExtra("Current Time"));

        button_function();

    }

    private void button_function() {
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v -> {
            onBackPressed();
        });


        cancel_button = findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(v -> {
            onBackPressed();
        });

        delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(v -> {
            delete_time_slot();
        });

        update_button = findViewById(R.id.update_button);
        update_button.setOnClickListener(v -> {
            update_time_slot();
        });

    }

    private void delete_time_slot() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = getIntent().getIntExtra("Doctor Id", 0);
        int availabilityId = getIntent().getIntExtra("Availability Id", 0);
        int slotId = getIntent().getIntExtra("Slot Id", 0);

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<Doctor_AvailabilityDto> call = doctorAvailabilityService.getAvailabilityById(authHeader, availabilityId);
            call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                @Override
                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        List<AvailabilitySlotDto> slots = response.body().getSlots();

                        List<AvailabilitySlotDto> update_slots = new ArrayList<>();

                        for (AvailabilitySlotDto slot : slots) {
                            if (slot.getSlot_id() == slotId) {
                                continue;
                            } else {
                                update_slots.add(slot);
                            }
                        }

                        Doctor_AvailabilityDto update_dto = new Doctor_AvailabilityDto();
                        update_dto.setDoctor_id(doctorId);
                        update_dto.setAvailability_date(response.body().getAvailability_date());
                        update_dto.setSlots(update_slots);

                        Call<Doctor_AvailabilityDto> update_call = doctorAvailabilityService.updateByIdAndDate(authHeader, doctorId, response.body().getAvailability_date(), update_dto);
                        update_call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                            @Override
                            public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(Manage_Availability.this, "Delete Successfully!", Toast.LENGTH_SHORT).show();
                                    displayNotification("You have delete a time slot!", "Delete Time Slot");
                                    startActivity(new Intent(Manage_Availability.this, DoctorManageAppointment.class));
                                } else {
                                    Toast.makeText(Manage_Availability.this, "Delete Failed!", Toast.LENGTH_SHORT).show();
                                    Log.e("DeleteTimeSlot", "No response body");
                                }
                            }

                            @Override
                            public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                Log.e("DeleteTimeSlot", "Error: " + t.getMessage(), t);
                            }
                        });


                    } else {
                        Log.e("DeleteTimeSlot", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                    Log.e("DeleteTimeSlot", "Error: " + t.getMessage(), t);
                }
            });


        } else {
            Log.e("DeleteTimeSlot", "Missing token or doctorId");
        }
    }

    private void displayNotification(String message, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Manage_Availability.this, CHANEL_ID)
                .setSmallIcon(R.drawable.doc_logo_v2)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(Manage_Availability.this);
        mNotificationMgr.notify(1, builder.build());
    }

    private void update_time_slot() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = getIntent().getIntExtra("Doctor Id", 0);
        int availabilityId = getIntent().getIntExtra("Availability Id", 0);
        int slotId = getIntent().getIntExtra("Slot Id", 0);

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<Doctor_AvailabilityDto> call = doctorAvailabilityService.getAvailabilityById(authHeader, availabilityId);
            call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                @Override
                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                    if (response.isSuccessful() && response.body() != null) {

                        List<AvailabilitySlotDto> slots = response.body().getSlots();

                        List<AvailabilitySlotDto> update_slots = new ArrayList<>();

                        for (AvailabilitySlotDto slot : slots) {
                            if (slot.getSlot_id() == slotId) {
                                TimePicker timePicker = findViewById(R.id.time_picker_start);
                                int hour = timePicker.getHour();
                                int minute = timePicker.getMinute();

                                if ((hour < 8) || (hour >= 17)) {
                                    Toast.makeText(Manage_Availability.this, "Please select a time between 08:00 and 16:00", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // Format time to HH:mm
                                String formattedTime = String.format("%02d:%02d", hour, minute);

                                slot.setSlot_time(formattedTime);

                                update_slots.add(slot);
                            } else {
                                update_slots.add(slot);
                            }
                        }

                        Doctor_AvailabilityDto update_dto = new Doctor_AvailabilityDto();
                        update_dto.setDoctor_id(doctorId);
                        update_dto.setAvailability_date(response.body().getAvailability_date());
                        update_dto.setSlots(update_slots);

                        Call<Doctor_AvailabilityDto> update_call = doctorAvailabilityService.updateByIdAndDate(authHeader, doctorId, response.body().getAvailability_date(), update_dto);
                        update_call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                            @Override
                            public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    Toast.makeText(Manage_Availability.this, "Update Successfully!", Toast.LENGTH_SHORT).show();
                                    displayNotification("You have update a time slot!", "Update Time Slot");
                                    startActivity(new Intent(Manage_Availability.this, DoctorManageAppointment.class));
                                } else {
                                    Toast.makeText(Manage_Availability.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                                    Log.e("UpdateTimeSlot", "No response body");
                                }
                            }

                            @Override
                            public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                Log.e("UpdateTimeSlot", "Error: " + t.getMessage(), t);
                            }
                        });


                    } else {
                        Log.e("UpdateTimeSlot", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                    Log.e("UpdateTimeSlot", "Error: " + t.getMessage(), t);
                }
            });


        } else {
            Log.e("UpdateTimeSlot", "Missing token or doctorId");
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}