package vn.edu.usth.doconcall.Patient.Dashboard.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.usth.doconcall.Patient.Dashboard.Fragment.Patient_Dashboard_Fragment;
import vn.edu.usth.doconcall.Patient.Dashboard.Fragment.Patient_Notification_Fragment;

public class Patient_Dashboard_Adapter extends FragmentStateAdapter {

    public Patient_Dashboard_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Patient_Dashboard_Fragment();
            case 1:
                return new Patient_Notification_Fragment();
            default:
                return new Patient_Dashboard_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
