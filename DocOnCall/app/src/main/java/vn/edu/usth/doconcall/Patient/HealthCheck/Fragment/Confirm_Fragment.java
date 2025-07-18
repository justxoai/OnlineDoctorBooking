package vn.edu.usth.doconcall.Patient.HealthCheck.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Models.NotificationDto;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Models.UserDto;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.NotificationAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Network.UserAPI;
import vn.edu.usth.doconcall.Patient.Dashboard.Patient_Dashboard;
import vn.edu.usth.doconcall.R;

public class Confirm_Fragment extends Fragment {

    private static final String CHANEL_ID = "doc_on_call";
    private static final String CHANEL_NAME = "DocOnCall";
    private static final String CHANEL_DESCRIPTION = "DocOnCall";

    private TextView doctorNameText, doctorSpecText, appointmentDateText, appointmentTypeText, appointmentTimeText;
    private TextView patientName, patientGender, patientDob;
    private String doctorName, specialization, appointmentType, appointmentTime, appointmentDate;
    private int slotId;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Layout
        View v = inflater.inflate(R.layout.fragment_confirm_, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANEL_DESCRIPTION);

            NotificationManager manager = getSystemService(requireContext(), NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Get arguments
        bundle = getArguments();
        if (bundle != null) {
            doctorName = bundle.getString("Doctor Name");
            specialization = bundle.getString("Specialization");
            appointmentType = bundle.getString("Appointment Type");
            appointmentTime = bundle.getString("Appointment Time");
            appointmentDate = bundle.getString("Appointment Date");

            // Set data to TextViews
            doctorNameText = v.findViewById(R.id.doctor_name_text);
            doctorSpecText = v.findViewById(R.id.doctor_spec_text);
            appointmentDateText = v.findViewById(R.id.appointment_date_select);
            appointmentTypeText = v.findViewById(R.id.appointment_type);
            appointmentTimeText = v.findViewById(R.id.appointment_time_select);

            patientName = v.findViewById(R.id.textview_patient_name);
            patientGender = v.findViewById(R.id.textview_patient_gender);
            patientDob = v.findViewById(R.id.textview_patient_dob);

            doctorNameText.setText("Doctor: " + doctorName);
            doctorSpecText.setText("Specialization: " + specialization);
            appointmentTypeText.setText("Type: " + appointmentType);
            appointmentTimeText.setText("Time: " + appointmentTime);
            appointmentDateText.setText("Date: " + appointmentDate + "/2025");
        }


        // Confirm Fragment Function
        confirm_function(v);

        // Fetch patient Information
        fetch_patient_information();

        return v;
    }

    private void fetch_patient_information() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int patientId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && patientId != -1) {
            String authHeader = "Bearer " + token;

            UserAPI userService = RetrofitClient.getInstance().create(UserAPI.class);
            Call<UserDto> call = userService.getUserById(authHeader, patientId);
            call.enqueue(new Callback<UserDto>() {
                @Override
                public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDto user = response.body();
                        patientName.setText("Name: " + user.getName());
                        patientGender.setText("Gender: " + user.getGender());
                        patientDob.setText("DoB: " + user.getBirthday());
                    } else {
                        Log.e("ConfirmFragmentPatientInformation", "Response not successful or body is null");
                    }
                }

                @Override
                public void onFailure(Call<UserDto> call, Throwable t) {
                    Log.e("ConfirmFragmentPatientInformation", "Error: " + t.getMessage(), t);
                }
            });

        } else {
            Log.e("ConfirmFragmentPatientInformation", "Missing token or patientId");
        }
    }

    private void confirm_function(View v) {
        // Back
        ImageButton back_button = v.findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Continue
        Button continue_button = v.findViewById(R.id.button_Contact);
        continue_button.setOnClickListener(view -> update_appointment_status());
    }

    private void update_appointment_status() {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int patientId = sessionManager.getUserId();
        int doctorId = bundle.getInt("Doctor Id");

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            DoctorAvailabilityAPI doctorAvailabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);

            Call<List<Doctor_AvailabilityDto>> call = doctorAvailabilityService.getAllByDoctorId(authHeader, doctorId);
            call.enqueue(new Callback<List<Doctor_AvailabilityDto>>() {
                @Override
                public void onResponse(Call<List<Doctor_AvailabilityDto>> call, Response<List<Doctor_AvailabilityDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Doctor_AvailabilityDto> availabilities = response.body();

                        List<AvailabilitySlotDto> update_slot = new ArrayList<>();

                        for (Doctor_AvailabilityDto availability : availabilities) {
                            if (availability.getAvailability_date().equals(formatDate(appointmentDate))) {
                                for (AvailabilitySlotDto slot : availability.getSlots()) {
                                    if (slot.getSlot_time().equals(appointmentTime)) {
                                        update_slot.add(new AvailabilitySlotDto(slot.getSlot_id(), slot.getSlot_time(), "BOOKED"));
                                        slotId = slot.getSlot_id();
                                    }
                                    else {
                                        update_slot.add(new AvailabilitySlotDto(slot.getSlot_id(), slot.getSlot_time(), slot.getSlot_status()));
                                    }
                                }
                            }
                        }

                        Doctor_AvailabilityDto update_doctor_avail = new Doctor_AvailabilityDto();
                        update_doctor_avail.setDoctor_id(doctorId);
                        update_doctor_avail.setAvailability_date(formatDate(appointmentDate));
                        update_doctor_avail.setSlots(update_slot);

                        Call<Doctor_AvailabilityDto> update_call = doctorAvailabilityService.updateByIdAndDate(authHeader, doctorId, formatDate(appointmentDate), update_doctor_avail);

                        update_call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                            @Override
                            public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    ScheduleEventDto create_schedule_event = new ScheduleEventDto();
                                    create_schedule_event.setDoctor_id(doctorId);
                                    create_schedule_event.setPatient_id(patientId);

                                    if (appointmentType.equals("Online Meeting")) {
                                        create_schedule_event.setAppointment_style("VIRTUAL_MEETING");
                                    } else {
                                        create_schedule_event.setAppointment_style("IN_PERSON_MEETING");
                                    }

                                    create_schedule_event.setDoctor_availability_id(response.body().getAvailability_id());

                                    create_schedule_event.setTime_slot_id(slotId);

                                    create_schedule_event.setAppointment_status("SCHEDULED");

                                    ScheduleEventAPI scheduleEventService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
                                    Call<ScheduleEventDto> schedule_call = scheduleEventService.createScheduleEvent(authHeader, create_schedule_event);

                                    schedule_call.enqueue(new Callback<ScheduleEventDto>() {
                                        @Override
                                        public void onResponse(Call<ScheduleEventDto> call, Response<ScheduleEventDto> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                Toast.makeText(requireContext(), "Appointment updated!", Toast.LENGTH_SHORT).show();
                                                displayNotification();
                                                fetchNotification(token, doctorId, patientId, "APPOINTMENT", response.body().getSchedule_id());
                                                Intent i= new Intent(requireContext(), Patient_Dashboard.class);
                                                startActivity(i);
                                            } else {
                                                Log.e("ScheduleEvent", "Response not successful or body is null");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ScheduleEventDto> call, Throwable t) {
                                            Log.e("ScheduleEvent", "Error: " + t.getMessage(), t);
                                        }
                                    });

                                } else {
                                    Log.e("UpdateAvailability", "Response not successful or body is null");
                                }
                            }

                            @Override
                            public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                Log.e("UpdateAvailability", "Error: " + t.getMessage(), t);
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<List<Doctor_AvailabilityDto>> call, Throwable t) {
                    Log.e("ConfirmFragment", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("ConfirmFragment", "Missing token or doctorId");
        }

    }

    private static String formatDate(String inputDate) {
        SimpleDateFormat input = new SimpleDateFormat("dd/MM", Locale.getDefault());
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            // Parse without year first
            Date date = input.parse(inputDate);

            // Set the parsed date's year to the current year
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.setTime(date);
            calendar.set(Calendar.YEAR, currentYear);

            // Format back to your API format
            return output.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // or handle gracefully
        }
    }

    private void fetchNotification(String token, int doctor_id, int patient_id, String event_type, int schedule_event){
        if(token != null && !token.isEmpty()){
            String authHeader = "Bearer " + token;

            NotificationAPI notificationService = RetrofitClient.getInstance().create(NotificationAPI.class);
            NotificationDto notification = new NotificationDto();
            notification.setSender_id(patient_id);
            notification.setReceiver_id(doctor_id);
            notification.setEventType(event_type);
            notification.setEvent_id(schedule_event);
            notification.setMessage("You have a new appointment");

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

    private void displayNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANEL_ID)
                .setSmallIcon(R.drawable.doc_logo_v2)
                .setContentTitle("Appointment Booked!")
                .setContentText("You’ve booked an appointment with " + doctorName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(requireContext());
        mNotificationMgr.notify(1, builder.build());
    }
}