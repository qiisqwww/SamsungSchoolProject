package com.example.samsungschoolproject.fragment.workout;

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

import com.example.samsungschoolproject.DTO.WorkoutTemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutTemplateListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TemplatesListFragment extends Fragment implements WorkoutTemplateListAdapter.OnWorkoutTemplateItemListener{
    private Button createNewTemplateButton;
    private WorkoutHelperDatabase database;
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

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());

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
        if (!WorkoutListUtils.name.isEmpty()){
            Log.d("GG", WorkoutListUtils.name);
            loadNewTemplateFromUtils();
        }


        List<WorkoutTemplate> workoutTemplates = database.getWorkoutTemplateDAO().getAllWorkoutTemplates();

        if (workoutTemplates.isEmpty()) {
            Log.d("GG", "isEmpty");
            return;
        }

        List<WorkoutTemplateInfo> workoutTemplatesInfo = WorkoutListUtils.parseWorkoutTemplatesForAdapter(workoutTemplates);
        setWorkoutTemplatesRecycler(workoutTemplatesInfo);
    }

    private void loadNewTemplateFromUtils(){
        WorkoutTemplate workoutTemplate = new WorkoutTemplate(WorkoutListUtils.name, WorkoutListUtils.countWorkoutLength());
        database.getWorkoutTemplateDAO().addWorkoutTemplate(workoutTemplate);

        workoutTemplate = database.getWorkoutTemplateDAO().getWorkoutTemplateByName(WorkoutListUtils.name);
        for (int i = 0; i < WorkoutListUtils.exercises.size(); i++){
            ArrayList<String> exerciseInfo = WorkoutListUtils.exercises.get(i);
            Exercise exercise = database.getExerciseDAO().getExerciseByName(exerciseInfo.get(0));
            database.getWorkoutTemplateExerciseDAO().addWorkoutTemplateExercise(new WorkoutTemplateExercise(
                    workoutTemplate.id,
                    exercise.id,
                    Integer.valueOf(exerciseInfo.get(2)),
                    Integer.valueOf(exerciseInfo.get(1)),
                    i+1
            ));
        }

        WorkoutListUtils.name = "";
        WorkoutListUtils.exercises = new ArrayList<>(); // Очищаю введенные данные
    }

    private void setWorkoutTemplatesRecycler(List<WorkoutTemplateInfo> workoutTemplatesInfo){
        WorkoutTemplateListAdapter workoutTemplateListAdapter = new WorkoutTemplateListAdapter(workoutTemplatesInfo, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutTemplatesRecycler.setLayoutManager(layoutManager);
        workoutTemplatesRecycler.setAdapter(workoutTemplateListAdapter);
    }

    @Override
    public void onItemClick(int position) {

    }
}