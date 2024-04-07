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

import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.DTO.WorkoutTemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.WorkoutListAdapter;
import com.example.samsungschoolproject.view_adapter.WorkoutTemplateListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TemplatesListFragment extends Fragment {
    private Button createNewTemplateButton;
    private RecyclerView workoutTemplatesRecycler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_templates_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        initButtonListeners();
        loadTemplatesList();
    }

    private void initWidgets(View view){
        createNewTemplateButton = view.findViewById(R.id.addNewWorkoutTemplate);
        workoutTemplatesRecycler = view.findViewById(R.id.workoutTemplatesRecycler);
    }

    private void initButtonListeners(){
        createNewTemplateButton.setOnClickListener(v -> {
            TemplatesBuilderFragment templatesListFragment = new TemplatesBuilderFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.workoutTemplatesContainer, templatesListFragment)
                    .addToBackStack("templates_list_fragment")
                    .commit();
        });
    }

    private void loadTemplatesList() {
        WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());
        List<WorkoutTemplate> workoutTemplates = database.getWorkoutTemplateDAO().getAllWorkoutTemplates();

        if (workoutTemplates.size() == 0) {
            return;
        }

        List<WorkoutTemplateInfo> workoutTemplatesInfo = WorkoutListUtils.parseWorkoutTemplatesForAdapter(workoutTemplates);
        setWorkoutTemplatesRecycler(workoutTemplatesInfo);
    }

    private void setWorkoutTemplatesRecycler(List<WorkoutTemplateInfo> workoutTemplatesInfo){
        WorkoutTemplateListAdapter workoutTemplateListAdapter = new WorkoutTemplateListAdapter(workoutTemplatesInfo);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutTemplatesRecycler.setLayoutManager(layoutManager);
        workoutTemplatesRecycler.setAdapter(workoutTemplateListAdapter);
    }
}