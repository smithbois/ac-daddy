package com.smithboys.acdaddy.adapters;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.smithboys.acdaddy.tabs.DemoFragment;
import com.smithboys.acdaddy.tabs.main.*;

import java.util.ArrayList;

// creates the view pager adapter for the main tab layout of the app (home, schedule, data)
public class MainTabPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();

    // number of tabs


    public MainTabPagerAdapter(@NonNull FragmentActivity fragmentActivity ){
        super(fragmentActivity);
    }

    // gets a certain tab based on the inputted position
    @NonNull @Override public Fragment createFragment(int position){
        return fragments.get(position);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public Fragment getFragment(int position){
        return fragments.get(position);
    }



    @Override public int getItemCount(){
        return fragments.size();
    }
}
