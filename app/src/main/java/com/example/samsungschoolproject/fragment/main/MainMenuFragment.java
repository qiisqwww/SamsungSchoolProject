package com.example.samsungschoolproject.fragment.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.activity.SettingsMenuActivity;


public class MainMenuFragment extends Fragment {
    Button toSettingsButton;
    Button getMotivationButton;
    RecyclerView statisticsRecyclerView;

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
        toSettingsButton = view.findViewById(R.id.toSettings);
        getMotivationButton = view.findViewById(R.id.getMotivation);
        statisticsRecyclerView = view.findViewById(R.id.statisticsRecycler); // TODO: Сделать сбор статистики + адаптер
    }

    private void initButtonListeners(){
        toSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SettingsMenuActivity.class);
            startActivity(intent);
        });

        getMotivationButton.setOnClickListener(v -> {
            // TODO: работа с медиа
        });
    }
}