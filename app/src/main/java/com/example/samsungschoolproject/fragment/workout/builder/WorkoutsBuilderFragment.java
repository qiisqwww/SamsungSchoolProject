package com.example.samsungschoolproject.fragment.workout.builder;

import android.content.Context;
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
import android.widget.Toast;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.database.model.WorkoutTemplateExercise;
import com.example.samsungschoolproject.enums.BackFragmentForBuilderStates;
import com.example.samsungschoolproject.fragment.сalendar.MonthCalendarFragment;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.workout.builder.WorkoutBuilderAdapter;
import com.example.samsungschoolproject.view_adapter.workout.list.WorkoutListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorkoutsBuilderFragment extends BottomSheetDialogFragment implements WorkoutBuilderAdapter.LoadJustCreated {
    public static String TAG;
    private Button goBackButton;
    private BackFragmentForBuilderStates backFragmentForBuilderStates;
    private WorkoutListAdapter workoutListAdapter;
    private WorkoutHelperDatabase database;
    private RecyclerView workoutBuilderRecycler;

    public WorkoutsBuilderFragment(BackFragmentForBuilderStates backFragmentForBuilderStates, WorkoutListAdapter workoutListAdapter){
        this.backFragmentForBuilderStates = backFragmentForBuilderStates;
        this.workoutListAdapter = workoutListAdapter;
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

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());

        initWidgets(view);
        initButtonListeners();

        setWorkoutBuilderRecycler();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        workoutListAdapter.update();
    }

    private void initWidgets(View view){
        goBackButton = view.findViewById(R.id.back);

        workoutBuilderRecycler = view.findViewById(R.id.workoutBuilderRecycler);
    }

    private void initButtonListeners(){
        goBackButton.setOnClickListener(v -> {
            startPreviousFragment();
        });
    }

    private void setWorkoutBuilderRecycler(){
       CompletableFuture<List<Exercise>> future = CompletableFuture.supplyAsync(() -> database.getExerciseDAO().getAllExercises());
       List<String> stringExercises = null;
        try {
            stringExercises = ExerciseListUtils.parseExerciseToStrings(future.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        WorkoutBuilderAdapter workoutBuilderAdapter = new WorkoutBuilderAdapter(stringExercises, workoutBuilderRecycler, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutBuilderRecycler.setLayoutManager(layoutManager);
        workoutBuilderRecycler.setAdapter(workoutBuilderAdapter);
    }

    // Загружает в БД только что созданную тренировку
    @Override
    public void loadJustCreated(String name, ArrayList<ArrayList<String>> exercises) {
        PlannedWorkout plannedWorkout = new PlannedWorkout(name, WorkoutListUtils.countWorkoutLength(exercises), "false", CalendarUtils.selectedDate.toString());

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // Необходимо проверить, что тренировка с таким же именем еще не запланирована на сегодня
            List<PlannedWorkout> plannedWorkouts = database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(CalendarUtils.selectedDate.toString());
            for (int i = 0; i < plannedWorkouts.size(); i++){
                if(plannedWorkout.name.equals(plannedWorkouts.get(i).name)){
                    Toast.makeText(requireContext().getApplicationContext(),  R.string.workout_already_exists, Toast.LENGTH_LONG).show();
                    return "false";
                }
            }

            // Добавляем новую запись в таблицу planned_workouts
            long newPlannedWorkoutID = database.getPlannedWorkoutDAO().addPlannedWorkout(plannedWorkout);
            int plannedWorkoutID = Math.toIntExact(newPlannedWorkoutID);

            // Добавляем связанные записи в таблицу planned_workout_exercises
            for (int i = 0; i < exercises.size(); i++){
                ArrayList<String> exerciseInfo = exercises.get(i);
                Exercise exercise = database.getExerciseDAO().getExerciseByName(exerciseInfo.get(0));
                database.getPlannedWorkoutExerciseDAO().addPlannedWorkoutExercise(new PlannedWorkoutExercise(
                        plannedWorkoutID,
                        exercise.id,
                        Integer.parseInt(exerciseInfo.get(1)),
                        Integer.parseInt(exerciseInfo.get(2)),
                        i+1
                ));
            }

            return "true";
        });

        try {
            if (future.get().equals("true"))
                startPreviousFragment();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Запускает предыдущий фрагмент исходя из состояния BackFragmentForBuilderStates
    public void startPreviousFragment() {
        if (backFragmentForBuilderStates == BackFragmentForBuilderStates.BACK_TO_WEEK_FRAGMENT){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
        }
        if (backFragmentForBuilderStates == BackFragmentForBuilderStates.BACK_TO_MONTH_FRAGMENT){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
            MonthCalendarFragment.ModalBottomSheetFragment modalBottomSheet = new MonthCalendarFragment.ModalBottomSheetFragment();
            MonthCalendarFragment.ModalBottomSheetFragment.TAG = "Another Instance"; // idk if this name is important

            modalBottomSheet.show(getActivity().getSupportFragmentManager(), MonthCalendarFragment.ModalBottomSheetFragment.TAG);
        }
    }


}