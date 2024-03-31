package com.example.samsungschoolproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;

public class WorkoutTemplatesFragment extends Fragment {
    private Button addNewWorkoutTemplateButton;
    private RecyclerView workoutTemplatesRecycler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_templates, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
    }

    private void initWidgets(View view){
        addNewWorkoutTemplateButton = view.findViewById(R.id.addNewWorkoutTemplate);
        workoutTemplatesRecycler = view.findViewById(R.id.workoutTemplatesRecycler);
    }
}