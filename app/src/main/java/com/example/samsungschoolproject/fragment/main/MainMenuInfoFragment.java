package com.example.samsungschoolproject.fragment.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.activity.SettingsMenuActivity;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.utils.WorkoutListUtils;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;

public class MainMenuInfoFragment extends Fragment {
    private WorkoutHelperDatabase database;
    private Button toSettingsButton;
    private Button getMotivationButton;
    private TextView workoutsCountTV, completedWorkoutsCountTV, completedWorkoutsLengthTV;
    private int workoutsCount, completedWorkoutsCount, completedWorkoutsLength;
    private final OpenMainMenuVideoFragment openMainMenuVideoFragment;

    public MainMenuInfoFragment(OpenMainMenuVideoFragment openMainMenuVideoFragment){
        this.openMainMenuVideoFragment = openMainMenuVideoFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());

        initWidgets(view);
        initButtonListeners();
        loadStatisticsData();
    }

    private void initWidgets(View view){
        toSettingsButton = view.findViewById(R.id.toSettings);
        getMotivationButton = view.findViewById(R.id.getMotivation);

        workoutsCountTV = view.findViewById(R.id.workoutsCount);
        completedWorkoutsCountTV = view.findViewById(R.id.completedWorkoutsCount);
        completedWorkoutsLengthTV = view.findViewById(R.id.completedWorkoutsLength);
    }

    private void initButtonListeners(){
        toSettingsButton.setOnClickListener(v -> startActivity(new Intent(v.getContext(), SettingsMenuActivity.class)));

        getMotivationButton.setOnClickListener(v -> openMainMenuVideoFragment.openMainMenuVideoFragment());
    }

    // Подгрузить статистику пользователя из БД
    private void loadStatisticsData(){
        CompletableFuture<Hashtable<String, Integer>> future = CompletableFuture.supplyAsync(() -> {
            Hashtable<String, Integer> statistics = new Hashtable<>();

            workoutsCount = database.getPlannedWorkoutDAO().getPlannedWorkoutsCount();
            statistics.put("workoutsCount", workoutsCount);
            completedWorkoutsCount = database.getPlannedWorkoutDAO().getCompletedPlannedWorkouts("true");
            statistics.put("completedWorkoutsCount", completedWorkoutsCount);
            completedWorkoutsLength = database.getPlannedWorkoutDAO().getSummaryApproximateLength();
            statistics.put("completedWorkoutsLength", completedWorkoutsLength);

            return statistics;
        }).thenApply(statistics -> {setStatistics(statistics); return null;
        });
    }

    // Установить статистику пользователя в необходимые поля
    private void setStatistics(Hashtable<String, Integer> statistics){
        workoutsCountTV.setText(getResources().getString(R.string.workouts_count) + " "
                + statistics.get("workoutsCount"));
        completedWorkoutsCountTV.setText(getResources().getString(R.string.completed_workouts_count) + " "
                + statistics.get("completedWorkoutsCount"));
        completedWorkoutsLengthTV.setText(getResources().getString(R.string.completed_workouts_length) + " "
                + statistics.get("completedWorkoutsLength") + WorkoutListUtils.parseLengthTime(completedWorkoutsLength));
    }

    // Открытие фрагмента, содержащего видео (реализация вынесена в MainMenuFragment)
    public interface OpenMainMenuVideoFragment{
        void openMainMenuVideoFragment();
    }
}