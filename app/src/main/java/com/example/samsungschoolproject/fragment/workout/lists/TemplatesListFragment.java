package com.example.samsungschoolproject.fragment.workout.lists;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.fragment.workout.builder.TemplatesBuilderFragment;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutTemplateListAdapter;

import java.util.List;

public class TemplatesListFragment extends Fragment implements WorkoutTemplateListAdapter.OnWorkoutItemListener {
    private Button createNewTemplateButton;
    private TextView templatesListInfoTV;
    private WorkoutHelperDatabase database;
    private List<WorkoutTemplate> workoutTemplates;
    private RecyclerView workoutTemplatesRecycler;
    private WorkoutTemplateListAdapter workoutTemplateListAdapter;

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

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());

        initWidgets(view);
        initButtonListeners();
        loadTemplatesList();
    }

    private void initWidgets(View view){
        createNewTemplateButton = view.findViewById(R.id.addNewWorkoutTemplate);
        templatesListInfoTV = view.findViewById(R.id.templatesListInfo);
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

    // Загружает список из WorkoutTemplate's из БД
    private void loadTemplatesList() {
        workoutTemplates = database.getWorkoutTemplateDAO().getAllWorkoutTemplates();

        if (workoutTemplates.isEmpty()) {
            return;
        }

        templatesListInfoTV.setText(getResources().getString(R.string.templates_list_info));
        setWorkoutTemplatesRecycler();
    }

    private void setWorkoutTemplatesRecycler(){
        workoutTemplateListAdapter = new WorkoutTemplateListAdapter(workoutTemplates, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutTemplatesRecycler.setLayoutManager(layoutManager);
        workoutTemplatesRecycler.setAdapter(workoutTemplateListAdapter);
    }


    // TODO: Добавить вывод информации о шаблоне по нажатии на нее
    @Override
    public void onWorkoutItemClick(int position) {
        Log.d("GG", String.valueOf(position));
    }
}