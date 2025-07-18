package vn.edu.usth.doconcall.Patient.Schedule.Daily_Hour;

import java.time.LocalTime;
import java.util.ArrayList;

import vn.edu.usth.doconcall.Patient.Schedule.Week_Event.Patient_Event;

public class Patient_HourEvent {

    LocalTime time;
    ArrayList<Patient_Event> events;

    public Patient_HourEvent(LocalTime time, ArrayList<Patient_Event> events)
    {
        this.time = time;
        this.events = events;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public void setTime(LocalTime time)
    {
        this.time = time;
    }

    public ArrayList<Patient_Event> getEvents()
    {
        return events;
    }

    public void setEvents(ArrayList<Patient_Event> events)
    {
        this.events = events;
    }
}
