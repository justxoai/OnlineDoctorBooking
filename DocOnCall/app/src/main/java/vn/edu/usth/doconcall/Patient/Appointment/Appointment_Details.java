package vn.edu.usth.doconcall.Patient.Appointment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.DoctorDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Models.NotificationDto;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Network.DoctorAPI;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.NotificationAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.R;

public class Appointment_Details extends AppCompatActivity {

    private static final String CHANEL_ID = "doc_on_call";
    private static final String CHANEL_NAME = "DocOnCall";
    private static final String CHANEL_DESCRIPTION = "DocOnCall";

    private String status = "";
    private String style = "";
    private CardView change_layout;

    private String new_appointment_type = "";
    private String new_appointment_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_appointment_details);

        change_layout = findViewById(R.id.update_layout);

        // Set Doctor Information
        fetch_doctor_information();

        // Set Appointment Information
        fetch_appointment_information();

        // Button function
        button_function();
    }
    private void fetch_doctor_information() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctor_id = getIntent().getIntExtra("Doctor Id", 0);

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
            Call<DoctorDto> call = doctorService.getDoctorById(authHeader, doctor_id);
            call.enqueue(new Callback<DoctorDto>() {
                @Override
                public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        DoctorDto doctor_response = response.body();

                        TextView doctor_name = findViewById(R.id.doctor_name);
                        doctor_name.setText("Name: " + doctor_response.getName());

                        TextView doctor_specialty = findViewById(R.id.doctor_specialty);
                        doctor_specialty.setText("Specialization: " + doctor_response.getSpecialization());
                    } else {
                        Log.e("GetDoctorInformation", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<DoctorDto> call, Throwable t) {
                    Log.e("GetDoctorInformation", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("GetDoctorInformation", "Missing token or doctorId");
        }
    }

    private void fetch_appointment_information() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int patient_id = sessionManager.getUserId();
        int availability_id = getIntent().getIntExtra("Availability Id", 0);
        int time_slot_id = getIntent().getIntExtra("Slot Id", 0);
        int schedule_id = getIntent().getIntExtra("Schedule Id", 0);

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
            Call<Doctor_AvailabilityDto> call = doctorAvailabilityService.getAvailabilityById(authHeader, availability_id);
            call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                @Override
                public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Doctor_AvailabilityDto availability = response.body();

                        TextView appointment_date = findViewById(R.id.appointment_date);
                        appointment_date.setText("Date: " + availability.getAvailability_date());

                        for (AvailabilitySlotDto slotDto : availability.getSlots()) {
                            if (slotDto.getSlot_id() == time_slot_id) {
                                TextView appointment_time = findViewById(R.id.appointment_time);
                                appointment_time.setText("Time: " + slotDto.getSlot_time());

                                try {
                                    String dateStr = availability.getAvailability_date(); // e.g., "2025-07-01"
                                    String timeStr = slotDto.getSlot_time();              // e.g., "15:30"

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                    Date appointmentDateTime = sdf.parse(dateStr + " " + timeStr);
                                    Date currentDateTime = new Date(); // now

                                    long diffMillis = appointmentDateTime.getTime() - currentDateTime.getTime();
                                    long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis);

                                    boolean isEligibleTime = diffMinutes >= 60;

                                    fetchScheduleInfo(authHeader, patient_id, schedule_id, isEligibleTime);

                                } catch (ParseException e) {
                                    Log.e("DateParseError", "Error parsing date/time", e);
                                }
                            }
                        }

                    } else {
                        Log.e("GetAppointmentInformation", "No response body");
                    }
                }

                @Override
                public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                    Log.e("GetAppointmentInformation", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("GetAppointmentInformation", "Missing token or doctorId");
        }
    }

    private void fetchScheduleInfo(String authHeader, int patient_id, int schedule_id, boolean isEligibleTime) {
        ScheduleEventAPI scheduleEventService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
        Call<List<ScheduleEventDto>> call2 = scheduleEventService.getByPatientId(authHeader, patient_id);
        call2.enqueue(new Callback<List<ScheduleEventDto>>() {
            @Override
            public void onResponse(Call<List<ScheduleEventDto>> call, Response<List<ScheduleEventDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ScheduleEventDto eventDto : response.body()) {
                        if (eventDto.getSchedule_id() == schedule_id) {
                            TextView appointment_type = findViewById(R.id.appointment_type);
                            TextView appointment_status = findViewById(R.id.appointment_status);
                            CardView update_layout = findViewById(R.id.update_layout);
                            MaterialButton update_button = findViewById(R.id.update_button);
                            MaterialButton delete_button = findViewById(R.id.delete_button);

                            String styleDisplay;
                            switch (eventDto.getAppointment_style()) {
                                case "VIRTUAL_MEETING":
                                    styleDisplay = "Online meeting";
                                    break;
                                case "IN_PERSON_MEETING":
                                    styleDisplay = "In person meeting";
                                    break;
                                default:
                                    styleDisplay = "In person meeting";
                                    break;
                            }

                            String statusDisplay;
                            switch (eventDto.getAppointment_status()) {
                                case "SCHEDULED":
                                    statusDisplay = "Upcoming";
                                    break;
                                case "COMPLETED":
                                    statusDisplay = "Complete";
                                    break;
                                case "CANCELLED":
                                    statusDisplay = "Cancel";
                                    break;
                                default:
                                    statusDisplay = "Upcoming";
                                    break;
                            }

                            appointment_type.setText("Type: " + styleDisplay);
                            appointment_status.setText("Status: " + statusDisplay);

                            // Hide if status is CANCELLED or time is not eligible
                            if ("CANCELLED".equals(eventDto.getAppointment_status()) || !isEligibleTime) {
                                update_layout.setVisibility(View.GONE);
                                update_button.setVisibility(View.GONE);
                                delete_button.setVisibility(View.GONE);
                            } else {
                                update_layout.setVisibility(View.VISIBLE);
                                update_button.setVisibility(View.VISIBLE);
                                delete_button.setVisibility(View.VISIBLE);
                            }

                            // Store for later use
                            status = eventDto.getAppointment_status();
                            style = eventDto.getAppointment_style();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                Log.e("GetAppointmentInformation", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void update_appointment() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int patient_id = sessionManager.getUserId();
        int schedule_id = getIntent().getIntExtra("Schedule Id", 0);

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            CheckBox online_meeting = findViewById(R.id.checkbox_online_meeting);
            CheckBox in_person_meeting = findViewById(R.id.checkbox_in_person_meeting);
            CheckBox cancel = findViewById(R.id.checkbox_cancel);

            boolean isOnlineMeetingChecked = online_meeting.isChecked();
            boolean isInPersonMeetingChecked = in_person_meeting.isChecked();
            boolean isCancelChecked = cancel.isChecked();

            if (!isOnlineMeetingChecked && !isInPersonMeetingChecked && !isCancelChecked) {
                onBackPressed();
                return;
            }

            if (isOnlineMeetingChecked) {
                new_appointment_type = "VIRTUAL_MEETING";
            } else if (isInPersonMeetingChecked){
                new_appointment_type = "IN_PERSON_MEETING";
            } else {
                new_appointment_type = style;
            }

            if (isCancelChecked) {
                new_appointment_status = "CANCELLED";
            } else {
                new_appointment_status = status;
            }

            ScheduleEventDto scheduleEventDto = new ScheduleEventDto();
            scheduleEventDto.setSchedule_id(schedule_id);
            scheduleEventDto.setPatient_id(patient_id);
            scheduleEventDto.setDoctor_id(getIntent().getIntExtra("Doctor Id", 0));
            scheduleEventDto.setDoctor_availability_id(getIntent().getIntExtra("Availability Id", 0));
            scheduleEventDto.setTime_slot_id(getIntent().getIntExtra("Slot Id", 0));
            scheduleEventDto.setAppointment_style(new_appointment_type);
            scheduleEventDto.setAppointment_status(new_appointment_status);

            ScheduleEventAPI scheduleEventService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<ScheduleEventDto> call = scheduleEventService.updateScheduleEvent(authHeader, schedule_id, scheduleEventDto);
            call.enqueue(new Callback<ScheduleEventDto>() {
                @Override
                public void onResponse(Call<ScheduleEventDto> call, Response<ScheduleEventDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(Appointment_Details.this, "Appointment updated successfully", Toast.LENGTH_SHORT).show();
                        if (new_appointment_status == "CANCELLED"){
                            displayNotification("You have canceled an appointment!", "Appointment Canceled!");
                            fetchNotification(token, getIntent().getIntExtra("Doctor Id", 0),  patient_id, "APPOINTMENT", schedule_id, "You have cancel an appointment!");
                        } else {
                            displayNotification("You have update an appointment!", "Appointment Updated!");
                            fetchNotification(token, getIntent().getIntExtra("Doctor Id", 0),  patient_id, "APPOINTMENT", schedule_id, "You have update an appointment!");
                        }
                        startActivity(new Intent(Appointment_Details.this, PatientManageAppointment.class));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ScheduleEventDto> call, Throwable t) {
                    Log.e("UpdateAppointment", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("UpdateAppointment", "Missing token");
        }
    }

    private void delete_appointment() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int schedule_id = getIntent().getIntExtra("Schedule Id", 0);

        if (token != null && !token.isEmpty()) {
            String authHeader = "Bearer " + token;

            ScheduleEventAPI scheduleEventService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
            Call<ResponseBody> call = scheduleEventService.deleteScheduleEvent(authHeader, schedule_id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(Appointment_Details.this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show();
                        displayNotification("You have delete an appointment!", "Appointment Deleted!");
                        fetchNotification(token, getIntent().getIntExtra("Doctor Id", 0), sessionManager.getUserId(), "APPOINTMENT", schedule_id, "You have delete an appointment!");
                        startActivity(new Intent(Appointment_Details.this, PatientManageAppointment.class));
                        finish();
                    } else {
                        Log.e("DeleteSchedule", "Respone null");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("DeleteSchedule", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("DeleteSchedule", "Missing token");
        }
    }

    private void fetchNotification(String token, int doctor_id, int patient_id, String event_type, int schedule_event, String message){
        if(token != null && !token.isEmpty()){
            String authHeader = "Bearer " + token;

            NotificationAPI notificationService = RetrofitClient.getInstance().create(NotificationAPI.class);
            NotificationDto notification = new NotificationDto();
            notification.setSender_id(patient_id);
            notification.setReceiver_id(doctor_id);
            notification.setEventType(event_type);
            notification.setEvent_id(schedule_event);
            notification.setMessage(message);

            Call<NotificationDto> call = notificationService.createNotification(authHeader, notification);
            call.enqueue(new Callback<NotificationDto>() {
                @Override
                public void onResponse(Call<NotificationDto> call, Response<NotificationDto> response) {

                }

                @Override
                public void onFailure(Call<NotificationDto> call, Throwable t) {
                    Log.e("CreateNotification", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("CreateNotification", "Mising token");
        }

    }

    private void displayNotification(String message, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Appointment_Details.this, CHANEL_ID)
                .setSmallIcon(R.drawable.doc_logo_v2)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(Appointment_Details.this);
        mNotificationMgr.notify(1, builder.build());
    }

    private void button_function() {
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(v ->
                onBackPressed()
        );

        MaterialButton cancel_button = findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(v ->
                onBackPressed()
        );

        MaterialButton delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(v ->
                delete_appointment()
        );

        MaterialButton update_button = findViewById(R.id.update_button);
        update_button.setOnClickListener(v ->
                update_appointment()
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}