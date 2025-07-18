package vn.edu.usth.doconcall.Patient.Schedule;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class Patient_Schedule_Adapter extends FragmentStateAdapter {

    public Patient_Schedule_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Patient_Monthly_Fragment();
            case 1:
                return new Patient_Weekly_Fragment();
            case 2:
                return new Patient_Daily_Fragment();
            default:
                return new Patient_Monthly_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
