package com.smithboys.acdaddy.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.smithboys.acdaddy.tabs.DemoFragment;
import com.smithboys.acdaddy.tabs.main.*;

// creates the view pager adapter for the main tab layout of the app (home, schedule, data)
public class MainTabPagerAdapter extends FragmentStateAdapter {

    // number of tabs
    public static final int CARD_ITEM_SIZE = 3;

    public MainTabPagerAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    // gets a certain tab based on the inputted position
    @NonNull @Override public Fragment createFragment(int position){
        switch (position){
            case 0: return DialFragment.newInstance("Dial Tab (DialFragment)");
            case 1: return DataFragment.newInstance("Data Tab (DataFragment)");
            case 2: return ScheduleFragment.newInstance("Schedule Tab (ScheduleFragment)");
            default: return DemoFragment.newInstance(3);
        }
    }

    @Override public int getItemCount(){
        return CARD_ITEM_SIZE;
    }
}
