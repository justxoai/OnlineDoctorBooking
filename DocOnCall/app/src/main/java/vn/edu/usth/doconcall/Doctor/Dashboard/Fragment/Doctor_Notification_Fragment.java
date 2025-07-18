package vn.edu.usth.doconcall.Doctor.Dashboard.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Models.AvailabilitySlotDto;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;
import vn.edu.usth.doconcall.Models.NotificationDto;
import vn.edu.usth.doconcall.Models.PatientDto;
import vn.edu.usth.doconcall.Models.ScheduleEventDto;
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.NotificationAPI;
import vn.edu.usth.doconcall.Network.PatientAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.ScheduleEventAPI;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Doctor.Dashboard.Notification.Doctor_notification_adapter;
import vn.edu.usth.doconcall.Doctor.Dashboard.Notification.Doctor_notification_items;
import vn.edu.usth.doconcall.R;

public class Doctor_Notification_Fragment extends Fragment {

    private RecyclerView notification_recycler;
    private List<Doctor_notification_items> items;
    private Doctor_notification_adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_doctor__notification_, container, false);

        notification_recycler = v.findViewById(R.id.doctor_notification_recycler_view);
        items = new ArrayList<>();
        adapter = new Doctor_notification_adapter(requireContext(), items);
        notification_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        notification_recycler.setAdapter(adapter);

        fetchNotifications();

        return v;
    }

    private void fetchNotifications() {
        SessionManager sessionManager = SessionManager.getInstance();
        String token = sessionManager.getToken();
        int doctorId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;
            NotificationAPI notificationService = RetrofitClient.getInstance().create(NotificationAPI.class);
            Call<List<NotificationDto>> call = notificationService.getAllNotifications(authHeader);

            call.enqueue(new Callback<List<NotificationDto>>() {
                @Override
                public void onResponse(Call<List<NotificationDto>> call, Response<List<NotificationDto>> response) {
                    items.clear();
                    if (response.body() == null) return;

                    for (NotificationDto notification : response.body()) {
                        fetchEventDetails(notification, doctorId, authHeader);
                    }
                }

                @Override
                public void onFailure(Call<List<NotificationDto>> call, Throwable t) {
                    Log.e("DoctorNotificationFragment", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("DoctorNotificationFragment", "Missing token or doctorId");
        }
    }

    private void fetchEventDetails(NotificationDto notification, int doctorId, String authHeader) {
        ScheduleEventAPI scheduleService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
        Call<List<ScheduleEventDto>> call = scheduleService.getByDoctorId(authHeader, doctorId);
        call.enqueue(new Callback<List<ScheduleEventDto>>() {
            @Override
            public void onResponse(Call<List<ScheduleEventDto>> call, Response<List<ScheduleEventDto>> response) {
                if (response.body() == null) return;

                for (ScheduleEventDto event : response.body()) {
                    if (event.getSchedule_id() == notification.getEvent_id()) {
                        loadFullDetails(event, notification);
                        break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ScheduleEventDto>> call, Throwable t) {
                Log.e("FetchEventDetails", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void loadFullDetails(ScheduleEventDto event, NotificationDto notification) {
        String authHeader = "Bearer " + SessionManager.getInstance().getToken();

        PatientAPI patientService = RetrofitClient.getInstance().create(PatientAPI.class);
        Call<PatientDto> patientCall = patientService.getPatientById(authHeader, event.getPatient_id());

        patientCall.enqueue(new Callback<PatientDto>() {
            @Override
            public void onResponse(Call<PatientDto> call, Response<PatientDto> response) {
                PatientDto patient = response.body();
                if (patient == null) return;

                DoctorAvailabilityAPI availabilityService = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
                Call<Doctor_AvailabilityDto> availabilityCall = availabilityService.getAvailabilityById(authHeader, event.getDoctor_availability_id());

                availabilityCall.enqueue(new Callback<Doctor_AvailabilityDto>() {
                    @Override
                    public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                        Doctor_AvailabilityDto availability = response.body();
                        if (availability == null) return;

                        String slotTime = "";
                        for (AvailabilitySlotDto slot : availability.getSlots()) {
                            if (slot.getSlot_id() == event.getTime_slot_id()) {
                                slotTime = slot.getSlot_time();
                                break;
                            }
                        }

                        addNotificationItem(notification, patient.getName(), availability.getAvailability_date(), slotTime);
                    }

                    @Override
                    public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                        Log.e("LoadDetails", "Availability Error: " + t.getMessage(), t);
                    }
                });
            }

            @Override
            public void onFailure(Call<PatientDto> call, Throwable t) {
                Log.e("LoadDetails", "Patient Error: " + t.getMessage(), t);
            }
        });
    }

    private void addNotificationItem(NotificationDto notification, String patientName, String date, String time) {
        String message;
        switch (notification.getMessage()) {
            case "You have a new appointment":
                message = "New appointment with patient " + patientName + " on " + date + " at " + time + ".";
                items.add(new Doctor_notification_items("New Appointment", message));
                break;
            case "You have update an appointment!":
                message = patientName + " updated their appointment on " + date + " at " + time + ".";
                items.add(new Doctor_notification_items("Updated Appointment", message));
                break;
            case "You have cancel an appointment!":
                message = patientName + " canceled their appointment on " + date + " at " + time + ".";
                items.add(new Doctor_notification_items("Canceled Appointment", message));
                break;
            case "You have delete an appointment!":
                message = patientName + " deleted their appointment on " + date + " at " + time + ".";
                items.add(new Doctor_notification_items("Deleted Appointment", message));
                break;
            default:
                message = "Upcoming appointment with " + patientName + " on " + date + " at " + time + ".";
                items.add(new Doctor_notification_items("Appointment Reminder", message));
        }

        adapter.notifyDataSetChanged();
    }
}
