package com.example.samsungschoolproject.fragment.templates;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.DTO.WorkoutTemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.view_adapter.WorkoutBuilderAdapter;
import com.example.samsungschoolproject.view_adapter.WorkoutTemplateListAdapter;

import java.util.ArrayList;
import java.util.List;


public class TemplatesBuilderFragment extends Fragment {
    private Button goBackButton;
    private RecyclerView workoutBuilderRecycler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_templates_builder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        initButtonListeners();
        setWorkoutBuilderRecycler();
    }

    private void initWidgets(View view){
        goBackButton = view.findViewById(R.id.goBack);
        workoutBuilderRecycler = view.findViewById(R.id.workoutBuilderRecycler);
    }

    private void initButtonListeners(){
        goBackButton.setOnClickListener(v -> {
            TemplatesListFragment templatesListFragment = new TemplatesListFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction()
                    .replace(R.id.workoutTemplatesContainer, templatesListFragment)
                    .commit();
        });
    }

    private void setWorkoutBuilderRecycler(){
        WorkoutBuilderAdapter workoutBuilderAdapter = new WorkoutBuilderAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutBuilderRecycler.setLayoutManager(layoutManager);
        workoutBuilderRecycler.setAdapter(workoutBuilderAdapter);
    }
}