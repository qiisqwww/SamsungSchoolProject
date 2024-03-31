package com.example.samsungschoolproject.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    Button toSettingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        initButtonListeners();
    }

    private void initWidgets(View view){
        toSettingsButton = (Button) view.findViewById(R.id.toSettings);
    }

    private void initButtonListeners(){
        toSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SettingsMenuActivity.class);
            startActivity(intent);
        });
    }
}