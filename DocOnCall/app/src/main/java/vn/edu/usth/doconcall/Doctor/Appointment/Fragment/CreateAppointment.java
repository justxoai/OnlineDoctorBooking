package vn.edu.usth.doconcall.Doctor.Appointment.Fragment;

import static vn.edu.usth.doconcall.Utils.Calendar_Utils.dayMonthFromDate;
import static vn.edu.usth.doconcall.Utils.Calendar_Utils.daysDateMonthYearFromDate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
import vn.edu.usth.doconcall.Network.DoctorAvailabilityAPI;
import vn.edu.usth.doconcall.Network.NotificationAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;
import vn.edu.usth.doconcall.Patient.Appointment.Appointment_Details;
import vn.edu.usth.doconcall.R;

public class CreateAppointment extends Fragment {

    private static final String CHANEL_ID = "doc_on_call";
    private TextView monthYearTV;
    private CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private EditText startTimeEditText, endTimeEditText;
    private Button createSlotsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_appointment, container, false);

        monthYearTV = v.findViewById(R.id.monthYearTV_create_appointment);

        LocalDate today = LocalDate.now();

        // Get Monday of this week
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1); // Monday = 1
        LocalDate weekEnd = weekStart.plusDays(6);

        monthYearTV.setText(dayMonthFromDate(weekStart) + " - " + dayMonthFromDate(weekEnd));

        // Setup Checkbox
        monday = v.findViewById(R.id.checkbox_mon);
        tuesday = v.findViewById(R.id.checkbox_tue);
        wednesday = v.findViewById(R.id.checkbox_wed);
        thursday = v.findViewById(R.id.checkbox_thu);
        friday = v.findViewById(R.id.checkbox_fri);
        saturday = v.findViewById(R.id.checkbox_sat);
        sunday = v.findViewById(R.id.checkbox_sun);

        Select_weekdays_function(v, weekStart);

        // Setup data
        startTimeEditText = v.findViewById(R.id.input_start_time);
        endTimeEditText = v.findViewById(R.id.input_end_time);

        createSlotsButton = v.findViewById(R.id.create_slots_button);
        createSlotsButton.setOnClickListener(view -> {
            fetch_create_slot(v);
        });


        return v;
    }

    private void fetch_create_slot(View v) {
        SessionManager sessionManager = SessionManager.getInstance();

        String token = sessionManager.getToken();
        int doctorId = sessionManager.getUserId();

        if (token != null && !token.isEmpty() && doctorId != -1) {
            String authHeader = "Bearer " + token;

            List<CheckBox> dayCheckboxes = List.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday);

            for (CheckBox dayCheckbox : dayCheckboxes) {
                if (dayCheckbox.isChecked()) {
                    String fullDate = dayCheckbox.getText().toString();
                    String date = extractDateFromLabel(convertDateWithMonthName(fullDate));

                    String startTime = startTimeEditText.getText().toString();
                    String endTime = endTimeEditText.getText().toString();

                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

                    try {
                        Date start = timeFormat.parse(startTime);
                        Date end = timeFormat.parse(endTime);
                        Date minStart = timeFormat.parse("07:00");
                        Date maxEnd = timeFormat.parse("17:00");

                        if (start.before(minStart)) {
                            Toast.makeText(requireContext(), "Start time must be at or after 07:00", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (end.after(maxEnd)) {
                            Toast.makeText(requireContext(), "End time must be at or before 17:00", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!end.after(start)) {
                            Toast.makeText(requireContext(), "End time must be after start time", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<AvailabilitySlotDto> slots = generateTimeSlots(startTime, endTime);

                        Doctor_AvailabilityDto dto = new Doctor_AvailabilityDto();
                        dto.setDoctor_id(doctorId);
                        dto.setAvailability_date(date);
                        dto.setSlots(slots);

                        DoctorAvailabilityAPI api = RetrofitClient.getInstance().create(DoctorAvailabilityAPI.class);
                        Call<Doctor_AvailabilityDto> call = api.createDoctorAvailability(authHeader, dto);
                        call.enqueue(new Callback<Doctor_AvailabilityDto>() {
                            @Override
                            public void onResponse(Call<Doctor_AvailabilityDto> call, Response<Doctor_AvailabilityDto> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Create Successfully", Toast.LENGTH_SHORT).show();
                                    displayNotification("You have created availability successfully!", "Create Availability");
                                    clearForm();
                                } else {
                                    Toast.makeText(requireContext(), "Create Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Doctor_AvailabilityDto> call, Throwable t) {
                                Log.e("CreateAppointment", "Error: " + t.getMessage());
                            }
                        });

                    } catch (ParseException e) {
                        Log.e("Time", "Invalid time format");
                    }
                }
            }

        } else {
            Log.e("Create Appointment", "Missing token or doctorId");
        }


    }


    private void Select_weekdays_function(View v, LocalDate weekStart) {
        monday.setText(daysDateMonthYearFromDate(weekStart));
        tuesday.setText(daysDateMonthYearFromDate(weekStart.plusDays(1)));
        wednesday.setText(daysDateMonthYearFromDate(weekStart.plusDays(2)));
        thursday.setText(daysDateMonthYearFromDate(weekStart.plusDays(3)));
        friday.setText(daysDateMonthYearFromDate(weekStart.plusDays(4)));
        saturday.setText(daysDateMonthYearFromDate(weekStart.plusDays(5)));
        sunday.setText(daysDateMonthYearFromDate(weekStart.plusDays(6)));
    }

    private String extractDateFromLabel(String label) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("EEE dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(label);
            return outputFormat.format(date);
        } catch (ParseException e) {
            Log.e("CreateAppointment", "Date parsing error: " + e.getMessage());
            return "";
        }
    }

    public static String convertDateWithMonthName(String fullDate) {
        try {
            // Split day name and date part
            String[] parts = fullDate.split(" ");
            if (parts.length != 2) return null;

            String dayName = parts[0];
            String datePart = parts[1]; // 06/July/2025

            // Parse the date part using month name
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MMMM/yyyy", Locale.ENGLISH);
            Date date = inputFormat.parse(datePart);

            // Format back with numeric month
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            String formattedDate = outputFormat.format(date);

            return dayName + " " + formattedDate;

        } catch (ParseException e) {
            Log.e("DateConversion", "Error parsing date: " + fullDate);
            return null;
        }
    }

    private List<AvailabilitySlotDto> generateTimeSlots(String start, String end) {

        List<AvailabilitySlotDto> slots = new ArrayList<>();

        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            Calendar lunchStart = Calendar.getInstance();
            Calendar lunchEnd = Calendar.getInstance();

            startCal.setTime(format.parse(start));
            endCal.setTime(format.parse(end));
            lunchStart.setTime(format.parse("12:00"));
            lunchEnd.setTime(format.parse("13:00"));

            while (startCal.before(endCal)) {
                if (startCal.before(lunchStart) || !startCal.before(lunchEnd)) {
                    String timeStr = format.format(startCal.getTime());
                    AvailabilitySlotDto slot = new AvailabilitySlotDto();
                    slot.setSlot_time(timeStr);
                    slot.setSlot_status("AVAILABLE");
                    slots.add(slot);
                }
                startCal.add(Calendar.MINUTE, 30);
            }

        } catch (ParseException e) {
            Log.e("CreateAppointment", "Invalid time format");
        }

        return slots;
    }

    private void clearForm() {
        // Clear checkboxes
        monday.setChecked(false);
        tuesday.setChecked(false);
        wednesday.setChecked(false);
        thursday.setChecked(false);
        friday.setChecked(false);
        saturday.setChecked(false);
        sunday.setChecked(false);

        // Clear input fields
        startTimeEditText.setText("");
        endTimeEditText.setText("");
    }

    private void displayNotification(String message, String title) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANEL_ID)
                .setSmallIcon(R.drawable.doc_logo_v2)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(requireContext());
        mNotificationMgr.notify(1, builder.build());
    }
}