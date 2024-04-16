package com.example.samsungschoolproject.fragment.workout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.database.model.WorkoutExercise;
import com.example.samsungschoolproject.enums.BackFragmentForBuilder;
import com.example.samsungschoolproject.fragment.сalendar.MonthCalendarFragment;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutBuilderAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsBuilderFragment extends BottomSheetDialogFragment implements WorkoutBuilderAdapter.StartPreviousFragment, WorkoutBuilderAdapter.LoadJustCreated {
    public static String TAG;
    private Button goBackButton;
    private BackFragmentForBuilder backFragmentForBuilder;
    private WorkoutHelperDatabase database;
    private RecyclerView workoutBuilderRecycler;

    public WorkoutsBuilderFragment(BackFragmentForBuilder backFragmentForBuilder){
        this.backFragmentForBuilder = backFragmentForBuilder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workouts_builder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext()); //  Получить объект БД

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
            startPreviousFragment();
        });
    }

    private List<Exercise> getAllExercises(){
        List<Exercise> exercises = database.getExerciseDAO().getAllExercises();
        return exercises;
    }

    private void setWorkoutBuilderRecycler(){
        List<String> stringExercises = ExerciseListUtils.parseExerciseToStrings(getAllExercises());

        WorkoutBuilderAdapter workoutBuilderAdapter = new WorkoutBuilderAdapter(stringExercises, workoutBuilderRecycler, this, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutBuilderRecycler.setLayoutManager(layoutManager);
        workoutBuilderRecycler.setAdapter(workoutBuilderAdapter);
    }

    @Override
    public void loadJustCreated(String name, ArrayList<ArrayList<String>> exercises) {
        Workout workout = new Workout(name, WorkoutListUtils.countWorkoutLength(exercises));
        database.getWorkoutDAO().addWorkout(workout);

        workout = database.getWorkoutDAO().getWorkoutByName(name);

        database.getPlannedWorkoutDAO().addPlannedWorkout(new PlannedWorkout(workout.id, "False", CalendarUtils.selectedDate.toString()));
        // TODO: Сделать enum для is_completed

        for (int i = 0; i < exercises.size(); i++){
            ArrayList<String> exerciseInfo = exercises.get(i);
            Exercise exercise = database.getExerciseDAO().getExerciseByName(exerciseInfo.get(0));
            database.getWorkoutExerciseDAO().addWorkoutExercise(new WorkoutExercise(
                    workout.id,
                    exercise.id,
                    Integer.valueOf(exerciseInfo.get(2)),
                    Integer.valueOf(exerciseInfo.get(1)),
                    i+1
            ));
        }
    }

    @Override
    public void startPreviousFragment() {
        if (backFragmentForBuilder == BackFragmentForBuilder.BACK_TO_WEEK_FRAGMENT){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
        }
        if (backFragmentForBuilder == BackFragmentForBuilder.BACK_TO_MONTH_FRAGMENT){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
            MonthCalendarFragment.ModalBottomSheetFragment modalBottomSheet = new MonthCalendarFragment.ModalBottomSheetFragment();
            MonthCalendarFragment.ModalBottomSheetFragment.TAG = "Another Instance"; // idk if this name is important

            modalBottomSheet.show(getActivity().getSupportFragmentManager(), MonthCalendarFragment.ModalBottomSheetFragment.TAG);
        }
    }


}