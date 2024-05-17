package com.example.samsungschoolproject.view.fragment.workout.info;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.samsungschoolproject.model.DTO.WorkoutInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.model.adapter.workout.info.WorkoutInfoAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class WorkoutInfoFragment extends BottomSheetDialogFragment {
    public static String TAG;
    private WorkoutInfoAdapter workoutInfoAdapter;
    private WorkoutInfo workoutInfo;
    private RecyclerView workoutInfoRecycler;
    private ImageButton backButton;

    public WorkoutInfoFragment(WorkoutInfo workoutInfo){
        this.workoutInfo = workoutInfo;
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

        initWidgets(view);
        initButtonListeners();
        setWorkoutInfoRecycler();
    }

    private void initWidgets(View view){
        backButton = view.findViewById(R.id.back);
        workoutInfoRecycler = view.findViewById(R.id.workoutInfoRecycler);
    }

    private void initButtonListeners(){
        backButton.setOnClickListener( v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
        });
    }

    private void setWorkoutInfoRecycler(){
        workoutInfoAdapter = new WorkoutInfoAdapter(workoutInfo);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutInfoRecycler.setLayoutManager(layoutManager);
        workoutInfoRecycler.setAdapter(workoutInfoAdapter);
    }
}