package com.example.samsungschoolproject.fragment.workout.lists;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutListAdapter;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutTemplateListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class WorkoutFromTemplateListFragment extends BottomSheetDialogFragment implements WorkoutTemplateListAdapter.OnWorkoutItemListener {
    public static String TAG;
    private WorkoutHelperDatabase database;
    private RecyclerView workoutTemplatesRecycler;
    private TextView headInfoTV;
    private WorkoutTemplateListAdapter workoutTemplateListAdapter;
    private final WorkoutListAdapter workoutListAdapter;
    private List<WorkoutTemplate> workoutTemplates;

    public WorkoutFromTemplateListFragment(WorkoutListAdapter workoutListAdapter){
        this.workoutListAdapter = workoutListAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_from_template_choice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());

        initWidgets(view);
        loadTemplates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        workoutListAdapter.update();
    }

    private void initWidgets(View view){
        workoutTemplatesRecycler = view.findViewById(R.id.workoutTemplatesRecycler);
        headInfoTV = view.findViewById(R.id.headInfoTV);
    }

    private void loadTemplates(){
        workoutTemplates = database.getWorkoutTemplateDAO().getAllWorkoutTemplates();

        if (workoutTemplates.isEmpty()){
            headInfoTV.setText(getResources().getString(R.string.no_templates_yet));
            return;
        }

        setWorkoutTemplatesRecycler();
    }

    private void setWorkoutTemplatesRecycler(){
        workoutTemplateListAdapter = new WorkoutTemplateListAdapter(workoutTemplates, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutTemplatesRecycler.setLayoutManager(layoutManager);
        workoutTemplatesRecycler.setAdapter(workoutTemplateListAdapter);
    }

    @Override
    public void onWorkoutItemClick(int position) {
        WorkoutTemplate workoutTemplate = workoutTemplates.get(position);

        // Необходимо проверить, не пытается ли человек запланировать одинаковый шаблон дважды на один и тот же день
        List<PlannedWorkout> alreadyPlannedWorkouts = database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(CalendarUtils.selectedDate.toString());
        for (int i = 0; i < alreadyPlannedWorkouts.size(); i++){
            Log.d("GG", alreadyPlannedWorkouts.get(i).name + " " + workoutTemplate.name);
            if (alreadyPlannedWorkouts.get(i).name.equals(workoutTemplate.name)){
                Toast.makeText(getContext(), R.string.already_planned_today, Toast.LENGTH_LONG).show();
                return;
            }
        }

        // Добавляем запись в таблицу planned_workouts
        PlannedWorkout newPlannedWorkout = new PlannedWorkout(workoutTemplate.name, workoutTemplate.approximate_length, "False", CalendarUtils.selectedDate.toString());
        long newPlannedWorkoutID = database.getPlannedWorkoutDAO().addPlannedWorkout(newPlannedWorkout);
        int plannedWorkoutID = Math.toIntExact(newPlannedWorkoutID);

        // Добавляем связанные записи в таблицу planned_workout_exercises
        List<WorkoutTemplateExercise> workoutTemplateExercises = database.getWorkoutTemplateExerciseDAO().getWorkoutTemplateExercisesByWorkoutTemplateId(workoutTemplate.id);
        for (int i = 0; i < workoutTemplateExercises.size(); i++){
            WorkoutTemplateExercise workoutTemplateExercise = workoutTemplateExercises.get(i);
            database.getPlannedWorkoutExerciseDAO().addPlannedWorkoutExercise(new PlannedWorkoutExercise(
                    plannedWorkoutID,
                    workoutTemplateExercise.exercise_id,
                    workoutTemplateExercise.repeats,
                    workoutTemplateExercise.approaches,
                    workoutTemplateExercise.number_in_query
                    ));
        }

        // Закрыть текущий фрагмент после выбора
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(this).commit();
    }
}
