package vn.edu.usth.doconcall.Doctor.Dashboard.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.usth.doconcall.Doctor.Dashboard.Fragment.Doctor_Dashboard_Fragment;
import vn.edu.usth.doconcall.Doctor.Dashboard.Fragment.Doctor_Notification_Fragment;

public class Doctor_Dashboard_Adapter extends FragmentStateAdapter {

    public Doctor_Dashboard_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Doctor_Dashboard_Fragment();
            case 1:
                return new Doctor_Notification_Fragment();
            default:
                return new Doctor_Dashboard_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
