package com.amcwustl.dailytarot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.amcwustl.dailytarot.R;
import com.amcwustl.dailytarot.activities.AboutUsActivity;
import com.amcwustl.dailytarot.activities.HomeActivity;
import com.amcwustl.dailytarot.activities.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

public class AppBarFragment extends Fragment {

    public AppBarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_bar, container, false);

        // Toolbar setup: Set the toolbar as the app's action bar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        // Navigation drawer setup
        DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar, // The toolbar to set as the navigation icon
                R.string.navigation_drawer_open, // Accessibility description for opening the drawer
                R.string.navigation_drawer_close // Accessibility description for closing the drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView setup: Handle item clicks in the navigation drawer
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            handleNavigation(item);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        return view;
    }

//    private void handleNavigation(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.nav_home:
//                // Handle navigation to HomeActivity
//                startActivity(new Intent(requireContext(), HomeActivity.class));
//                break;
//            case R.id.nav_about_us:
//                // Handle navigation to AboutUsActivity
//                startActivity(new Intent(requireContext(), AboutUsActivity.class));
//                break;
//            // Handle navigation for other items
//            //case R.id.nav_settings:
//                // Handle navigation to SettingsActivity
//                //startActivity(new Intent(requireContext(), SettingsActivity.class));
//                //break;
//        }
//    }

    private void handleNavigation(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            // Handle navigation to HomeActivity
            startActivity(new Intent(requireContext(), HomeActivity.class));
        } else if (id == R.id.nav_about_us) {
            // Handle navigation to AboutUsActivity
            startActivity(new Intent(requireContext(), AboutUsActivity.class));
        } else if (id == R.id.nav_settings) {
            // Handle navigation to SettingsActivity
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        }
        // Add more conditions for other navigation items if needed
    }


}
