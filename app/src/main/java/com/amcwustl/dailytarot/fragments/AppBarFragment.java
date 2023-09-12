package com.amcwustl.dailytarot.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.amcwustl.dailytarot.R;


public class AppBarFragment extends Fragment {

    public AppBarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_bar, container, false);

        // Handle app bar functionality here (e.g., navigation drawer, search, settings).

        return view;
    }
}
