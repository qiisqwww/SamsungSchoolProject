package com.example.samsungschoolproject.fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.activity.SettingsMenuActivity;


public class MainMenuFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        Button settingsButton = (Button) view.findViewById(R.id.settings_button);
        Button chooseStationButton = (Button) view.findViewById(R.id.choose_station_button) ;
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SettingsMenuActivity.class);
                startActivity(intent);
            }
        });

        chooseStationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StationsListFragment stationsListFragment = new StationsListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, stationsListFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}