package com.example.samsungschoolproject.fragment.workout.info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.view_adapter.workout.info.TemplateInfoAdapter;
import com.example.samsungschoolproject.view_adapter.workout.info.WorkoutInfoAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class WorkoutInfoFragment extends BottomSheetDialogFragment {
    public static String TAG;
    private WorkoutInfoAdapter workoutInfoAdapter;
    private WorkoutInfo workoutInfo;
    private PlannedWorkout plannedWorkout;
    private RecyclerView workoutInfoRecycler;

    public WorkoutInfoFragment(PlannedWorkout plannedWorkout){
        this.plannedWorkout = plannedWorkout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}