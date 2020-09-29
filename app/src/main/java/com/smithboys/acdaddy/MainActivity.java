package com.smithboys.acdaddy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.smithboys.acdaddy.adapters.MainTabPagerAdapter;
import com.smithboys.acdaddy.dialogs.SettingsDialog;
import com.smithboys.acdaddy.tabs.main.DataFragment;
import com.smithboys.acdaddy.tabs.main.DialFragment;
import com.smithboys.acdaddy.tabs.main.ScheduleFragment;
import com.smithboys.acdaddy.util.LayoutUtil;

// This activity contains the main tabs of the app

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    Toolbar toolbar;
    ImageButton settingsButton;
    private int[] tabIcons = {
            // place tabIcons here, ie: R.drawable.house
    };
    Context context;

    ScheduleFragment scheduleTab;
    DialFragment dialTab;
    DataFragment dataTab;

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        context = this;


        // set up the tabs
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        viewPager.setAdapter(LayoutUtil.createCardAdapter(this));
        viewPager.setCurrentItem(1);

        // equivalent to tabLayout.setupWithViewPager(viewPager);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch(position){
                            case 0: tab.setText("Schedule");
                            case 1: tab.setText("Dial");
                            case 2: tab.setText("Data");
                        }
                    }
                }).attach();

        LayoutUtil.setupTabIcons(tabLayout, tabIcons);

        scheduleTab = (ScheduleFragment) ((MainTabPagerAdapter)viewPager.getAdapter()).getFragment(0);
        dialTab = (DialFragment) ((MainTabPagerAdapter)viewPager.getAdapter()).getFragment(1);
        dataTab = (DataFragment) ((MainTabPagerAdapter)viewPager.getAdapter()).getFragment(2);

        System.out.println(dialTab);
        System.out.println("fragment: " + viewPager.getAdapter().getItemId(0));


        // set up the top toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("AC Daddy");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.home_icon);

        // I can't figure out how to set a title for the toolbar
        //toolbar.setTitle("");
        //setSupportActionBar(toolbar);
        //toolbar.setTitle("AC Daddy");

        settingsButton = findViewById(R.id.settings_button);
        View.OnClickListener settingsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open the settings dialog
                Dialog dialog = SettingsDialog.onCreateDialog(context);
                dialog.show();
            }
        };
        settingsButton.setOnClickListener(settingsListener);

        // navigation menu
        navigationView = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_schedule:
                        item.setChecked(true);
                        viewPager.setCurrentItem(0);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_dial:
                        item.setChecked(true);
                        viewPager.setCurrentItem(1);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_data:
                        item.setChecked(true);
                        viewPager.setCurrentItem(2);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_settings:
                        item.setChecked(true);
                        Dialog dialog = SettingsDialog.onCreateDialog(context);
                        dialog.show();
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
