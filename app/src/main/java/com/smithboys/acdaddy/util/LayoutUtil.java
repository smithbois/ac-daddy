package com.smithboys.acdaddy.util;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.tabs.TabLayout;
import com.smithboys.acdaddy.adapters.MainTabPagerAdapter;
import com.smithboys.acdaddy.tabs.main.DataFragment;
import com.smithboys.acdaddy.tabs.main.DialFragment;
import com.smithboys.acdaddy.tabs.main.ScheduleFragment;

public class LayoutUtil {

    // creates the adapter that supplies the main screen's view pager with the other tabs
    public static MainTabPagerAdapter createCardAdapter(FragmentActivity fA){
        MainTabPagerAdapter adapter = new MainTabPagerAdapter(fA);
        adapter.addFragment(new ScheduleFragment());
        adapter.addFragment(new DialFragment());
        adapter.addFragment(new DataFragment());
        return adapter;
    }

    // gives each tab an icon based on an array of icons passed in
    public static void setupTabIcons(TabLayout tabLayout, int[] icons) {
        if (icons.length == 0){
            return;
        }
        int count = tabLayout.getTabCount();
        for (int i = 0; i < count; i++){
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
    }


}
