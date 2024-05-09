package com.example.samsungschoolproject.fragment.main;

import android.content.Context;
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
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.utils.WorkoutListUtils;

import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainMenuInfoFragment extends Fragment {
    private Button getMotivationButton;
    private static TextView workoutsCountTV, completedWorkoutsCountTV, completedWorkoutsLengthTV;
    private static int workoutsCount, completedWorkoutsCount, completedWorkoutsLength;
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

        initWidgets(view);
        initButtonListeners();
        loadStatisticsData(requireContext().getApplicationContext(),
                getResources().getString(R.string.workouts_count),
                getResources().getString(R.string.completed_workouts_count),
                getResources().getString(R.string.completed_workouts_length));
    }

    private void initWidgets(View view){
        getMotivationButton = view.findViewById(R.id.getMotivation);

        workoutsCountTV = view.findViewById(R.id.workoutsCount);
        completedWorkoutsCountTV = view.findViewById(R.id.completedWorkoutsCount);
        completedWorkoutsLengthTV = view.findViewById(R.id.completedWorkoutsLength);
    }

    private void initButtonListeners(){
        getMotivationButton.setOnClickListener(v -> openMainMenuVideoFragment.openMainMenuVideoFragment());
    }

    // Подгрузить статистику пользователя из БД
    public static void loadStatisticsData(Context context, String workoutsCountS, String completedWorkoutsCountS, String completedWorkoutsLengthS){
        CompletableFuture<Hashtable<String, Integer>> future = CompletableFuture.supplyAsync(() -> {
            WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(context);
            Hashtable<String, Integer> statistics = new Hashtable<>();

            workoutsCount = database.getPlannedWorkoutDAO().getPlannedWorkoutsCount();
            statistics.put("workoutsCount", workoutsCount);
            completedWorkoutsCount = database.getPlannedWorkoutDAO().getCompletedPlannedWorkouts("true");
            statistics.put("completedWorkoutsCount", completedWorkoutsCount);
            completedWorkoutsLength = database.getPlannedWorkoutDAO().getSummaryApproximateLength();
            statistics.put("completedWorkoutsLength", completedWorkoutsLength);

            return statistics;
        });

        try {
            Hashtable<String, Integer> statistics = future.get();
            setStatistics(statistics ,workoutsCountS, completedWorkoutsCountS, completedWorkoutsLengthS);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Установить статистику пользователя в необходимые поля
    public static void setStatistics(Hashtable<String, Integer> statistics, String workoutsCountS, String completedWorkoutsCountS, String completedWorkoutsLengthS){
        workoutsCountTV.setText(workoutsCountS + " "
                + statistics.get("workoutsCount"));
        completedWorkoutsCountTV.setText(completedWorkoutsCountS + " "
                + statistics.get("completedWorkoutsCount"));
        completedWorkoutsLengthTV.setText(completedWorkoutsLengthS + " "
                + statistics.get("completedWorkoutsLength") + WorkoutListUtils.parseLengthTime(completedWorkoutsLength));
    }

    // Открытие фрагмента, содержащего видео (реализация вынесена в MainMenuFragment)
    public interface OpenMainMenuVideoFragment{
        void openMainMenuVideoFragment();
    }
}