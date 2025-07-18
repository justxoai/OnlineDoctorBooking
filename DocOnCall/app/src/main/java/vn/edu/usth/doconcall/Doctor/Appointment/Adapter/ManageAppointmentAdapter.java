package vn.edu.usth.doconcall.Doctor.Appointment.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.usth.doconcall.Doctor.Appointment.Fragment.CreateAppointment;
import vn.edu.usth.doconcall.Doctor.Appointment.Fragment.ManageAppointment;

public class ManageAppointmentAdapter extends FragmentStateAdapter {

    public ManageAppointmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CreateAppointment();
            case 1:
                return new ManageAppointment();
            default:
                return new CreateAppointment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
