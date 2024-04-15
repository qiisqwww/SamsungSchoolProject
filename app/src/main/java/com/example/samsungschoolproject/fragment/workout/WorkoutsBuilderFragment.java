package com.example.samsungschoolproject.fragment.workout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.enums.BackFragmentForBuilder;
import com.example.samsungschoolproject.utils.ExerciseListUtils;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutBuilderAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class WorkoutsBuilderFragment extends BottomSheetDialogFragment implements WorkoutBuilderAdapter.StartTemplateListFragment{
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
            if (backFragmentForBuilder == BackFragmentForBuilder.BACK_TO_WEEK_FRAGMENT){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(this).commit();
            }
            if (backFragmentForBuilder == BackFragmentForBuilder.BACK_TO_MONTH_FRAGMENT){
                // Нужно обдумать логику
            }
        });
    }

    private List<Exercise> getAllExercises(){
        List<Exercise> exercises = database.getExerciseDAO().getAllExercises();
        return exercises;
    }

    private void setWorkoutBuilderRecycler(){
        List<String> stringExercises = ExerciseListUtils.parseExerciseToStrings(getAllExercises());

        WorkoutBuilderAdapter workoutBuilderAdapter = new WorkoutBuilderAdapter(stringExercises, workoutBuilderRecycler, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutBuilderRecycler.setLayoutManager(layoutManager);
        workoutBuilderRecycler.setAdapter(workoutBuilderAdapter);
    }

    @Override
    public void startTemplateListFragment() {
        TemplatesListFragment templatesListFragment = new TemplatesListFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(new TemplatesListFragment());
        fragmentTransaction.replace(R.id.workoutTemplatesContainer, templatesListFragment).commit();
    }
}