package vn.edu.usth.doconcall.Doctor.Schedule.Week_Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Doctor_Event {

    public static ArrayList<Doctor_Event> eventsList = new ArrayList<>();

    public static ArrayList<Doctor_Event> eventsForDate(LocalDate date)
    {
        ArrayList<Doctor_Event> events = new ArrayList<>();

        for(Doctor_Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    public static ArrayList<Doctor_Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Doctor_Event> events = new ArrayList<>();

        for(Doctor_Event event : eventsList)
        {
            int eventHour = event.time.getHour();
            int cellHour = time.getHour();
            if(event.getDate().equals(date) && eventHour == cellHour)
                events.add(event);
        }

        return events;
    }


    private LocalDate date;
    private LocalTime time;

    private String patient_name;

    private String appointment_type;

    private String appointment_status;

    public Doctor_Event(LocalDate date, LocalTime time, String patient_name, String appointment_type, String appointment_status)
    {
        this.date = date;
        this.time = time;
        this.patient_name = patient_name;
        this.appointment_type = appointment_type;
        this.appointment_status = appointment_status;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getAppointment_type() {
        return appointment_type;
    }

    public void setAppointment_type(String appointment_type) {
        this.appointment_type = appointment_type;
    }

    public String getAppointment_status() {
        return appointment_status;
    }

    public void setAppointment_status(String appointment_status) {
        this.appointment_status = appointment_status;
    }
}
