package vn.edu.usth.doconcall.Patient.Dashboard.Fragment;

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
import java.util.Objects;

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
import vn.edu.usth.doconcall.Patient.Dashboard.Notification.Patient_notification_adpater;
import vn.edu.usth.doconcall.Patient.Dashboard.Notification.Patient_notification_item;
import vn.edu.usth.doconcall.R;

public class Patient_Notification_Fragment extends Fragment {

    private RecyclerView notification_recycler;
    private List<Patient_notification_item> items;
    private Patient_notification_adpater adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient__notification_, container, false);

        notification_recycler = v.findViewById(R.id.notification_recycler_view);
        items = new ArrayList<>();
        adapter = new Patient_notification_adpater(requireContext(), items);
        notification_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        notification_recycler.setAdapter(adapter);

        fetchNotifications();

        return v;
    }

    private void fetchNotifications() {
        SessionManager sessionManager = SessionManager.getInstance();
        String token = sessionManager.getToken();
        int patientId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && patientId != -1) {
            String authHeader = "Bearer " + token;
            NotificationAPI notificationService = RetrofitClient.getInstance().create(NotificationAPI.class);
            Call<List<NotificationDto>> call = notificationService.getAllNotifications(authHeader);

            call.enqueue(new Callback<List<NotificationDto>>() {
                @Override
                public void onResponse(Call<List<NotificationDto>> call, Response<List<NotificationDto>> response) {
                    items.clear();
                    if (response.body() == null) return;

                    for (NotificationDto notification : response.body()) {
                        fetchEventDetails(notification, patientId, authHeader);
                    }
                }

                @Override
                public void onFailure(Call<List<NotificationDto>> call, Throwable t) {
                    Log.e("PatientNotificationFragment", "Error: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("PatientNotificationFragment", "Missing token or patientId");
        }
    }

    private void fetchEventDetails(NotificationDto notification, int patientId, String authHeader) {
        ScheduleEventAPI scheduleService = RetrofitClient.getInstance().create(ScheduleEventAPI.class);
        Call<List<ScheduleEventDto>> call = scheduleService.getByPatientId(authHeader, patientId);

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

        DoctorAPI doctorService = RetrofitClient.getInstance().create(DoctorAPI.class);
        Call<DoctorDto> doctorCall = doctorService.getDoctorById(authHeader, event.getDoctor_id());
        doctorCall.enqueue(new Callback<DoctorDto>() {
            @Override
            public void onResponse(Call<DoctorDto> call, Response<DoctorDto> response) {
                DoctorDto doctor = response.body();
                if (doctor == null) return;

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

                        addNotificationItem(notification, doctor.getName(), availability.getAvailability_date(), slotTime);
                    }

                    @Override
                    public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                        Log.e("LoadDetails", "Availability Error: " + t.getMessage(), t);
                    }
                });
            }

            @Override
            public void onFailure(Call<DoctorDto> call, Throwable t) {
                Log.e("LoadDetails", "Doctor Error: " + t.getMessage(), t);
            }
        });
    }

    private void addNotificationItem(NotificationDto notification, String doctorName, String date, String time) {
        String message;
        switch (notification.getMessage()) {
            case "You have a new appointment":
                message = "You have a new appointment with " + doctorName + " at " + date + ", " + time + ".";
                items.add(new Patient_notification_item("New Appointment", message));
                break;
            case "You have update an appointment!":
                message = "You have updated an appointment with " + doctorName + " at " + date + ", " + time + ".";
                items.add(new Patient_notification_item("Update Appointment", message));
                break;
            case "You have cancel an appointment!":
                message = "You have canceled an appointment with " + doctorName + " at " + date + ", " + time + ".";
                items.add(new Patient_notification_item("Cancel Appointment", message));
                break;
            case "You have delete an appointment!":
                message = "You have deleted an appointment with " + doctorName + " at " + date + ", " + time + ".";
                items.add(new Patient_notification_item("Delete Appointment", message));
                break;
            default:
                message = "You have an upcoming appointment with " + doctorName + " at " + date + ", " + time + ". Please be on time.";
                items.add(new Patient_notification_item("Upcoming Appointment", message));
        }

        adapter.notifyDataSetChanged();
    }
}
