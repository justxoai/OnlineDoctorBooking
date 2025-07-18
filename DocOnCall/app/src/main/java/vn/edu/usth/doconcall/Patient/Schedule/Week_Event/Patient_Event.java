package vn.edu.usth.doconcall.Patient.Schedule.Week_Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Patient_Event {

    public static ArrayList<Patient_Event> eventsList = new ArrayList<>();

    public static ArrayList<Patient_Event> eventsForDate(LocalDate date)
    {
        ArrayList<Patient_Event> events = new ArrayList<>();

        for(Patient_Event event : eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    public static ArrayList<Patient_Event> eventsForDateAndTime(LocalDate date, LocalTime time)
    {
        ArrayList<Patient_Event> events = new ArrayList<>();

        for(Patient_Event event : eventsList)
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

    private String doctor_name;

    private String appointment_type;

    private String appointment_status;

    public Patient_Event( LocalDate date, LocalTime time, String doctor_name, String appointment_type, String appointment_status)
    {
        this.date = date;
        this.time = time;
        this.doctor_name = doctor_name;
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

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
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
