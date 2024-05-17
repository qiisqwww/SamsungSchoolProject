package com.example.samsungschoolproject.database.repository;

import android.util.Log;

import com.example.samsungschoolproject.database.entity.Exercise;
import com.example.samsungschoolproject.database.entity.WorkoutTemplate;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.entity.PlannedWorkout;
import com.example.samsungschoolproject.database.entity.PlannedWorkoutExercise;
import com.example.samsungschoolproject.database.entity.WorkoutTemplateExercise;
import com.example.samsungschoolproject.DTO.ExerciseInfo;
import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.util.CalendarUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WorkoutRepository {
    private WorkoutHelperDatabase database;

    public WorkoutRepository (WorkoutHelperDatabase database){
        this.database  = database;
    }

    public boolean loadNewPlannedWorkout(PlannedWorkout plannedWorkout, ArrayList<ArrayList<String>> unparsedExercises){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // Необходимо проверить, что тренировка с таким же именем еще не запланирована на сегодня
            List<PlannedWorkout> plannedWorkouts = database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(CalendarUtils.selectedDate.toString());
            for (int i = 0; i < plannedWorkouts.size(); i++){
                if(plannedWorkout.name.equals(plannedWorkouts.get(i).name)){
                    return "false";
                }
            }

            // Добавляем новую запись в таблицу planned_workouts
            long newPlannedWorkoutID = database.getPlannedWorkoutDAO().addPlannedWorkout(plannedWorkout);
            int plannedWorkoutID = Math.toIntExact(newPlannedWorkoutID);

            // Добавляем связанные записи в таблицу planned_workout_exercises
            for (int i = 0; i < unparsedExercises.size(); i++){
                ArrayList<String> exerciseInfo = unparsedExercises.get(i);
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
            return future.get().equals("true");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean createNewPlannedWorkoutFromTemplate(WorkoutTemplate workoutTemplate){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // Необходимо проверить, не пытается ли человек запланировать одинаковый шаблон дважды на один и тот же день
            List<PlannedWorkout> alreadyPlannedWorkouts = database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(CalendarUtils.selectedDate.toString());
            for (int i = 0; i < alreadyPlannedWorkouts.size(); i++){
                Log.d("GG", alreadyPlannedWorkouts.get(i).name + " " + workoutTemplate.name);
                if (alreadyPlannedWorkouts.get(i).name.equals(workoutTemplate.name)){
                    return "false";
                }
            }

            // Добавляем запись в таблицу planned_workouts
            PlannedWorkout newPlannedWorkout = new PlannedWorkout(workoutTemplate.name, workoutTemplate.approximate_length, "false", CalendarUtils.selectedDate.toString());
            long newPlannedWorkoutID = database.getPlannedWorkoutDAO().addPlannedWorkout(newPlannedWorkout);
            int plannedWorkoutID = Math.toIntExact(newPlannedWorkoutID);

            // Добавляем связанные записи в таблицу planned_workout_exercises
            List<WorkoutTemplateExercise> workoutTemplateExercises = database.getWorkoutTemplateExerciseDAO().getWorkoutTemplateExercisesByWorkoutTemplateId(workoutTemplate.id);
            for (int i = 0; i < workoutTemplateExercises.size(); i++){
                WorkoutTemplateExercise workoutTemplateExercise = workoutTemplateExercises.get(i);
                database.getPlannedWorkoutExerciseDAO().addPlannedWorkoutExercise(new PlannedWorkoutExercise(
                        plannedWorkoutID,
                        workoutTemplateExercise.exercise_id,
                        workoutTemplateExercise.approaches,
                        workoutTemplateExercise.repeats,
                        workoutTemplateExercise.number_in_query
                ));
            }

            return "true";
        });

        try {
            return future.get().equals("true");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PlannedWorkout> getPlannedWorkoutsByDate(String date){
        CompletableFuture<List<PlannedWorkout>> future = CompletableFuture.supplyAsync(() -> database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(date));

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public WorkoutInfo getWorkoutInfo(PlannedWorkout plannedWorkout){
        CompletableFuture<List<Exercise>> future = CompletableFuture.supplyAsync(() -> database.getExerciseDAO().getAllExercises());
        List<Exercise> exercises;
        try {
            exercises = future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        CompletableFuture<List<PlannedWorkoutExercise>> future1 = CompletableFuture.supplyAsync(() -> database.getPlannedWorkoutExerciseDAO().getPlannedWorkoutExercisesByWorkoutId(plannedWorkout.id));
        List<PlannedWorkoutExercise> plannedWorkoutExercises = null;
        try {
            plannedWorkoutExercises = future1.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new WorkoutInfo(plannedWorkout, ExerciseInfo.toExerciseInfoListForPlanned(exercises, plannedWorkoutExercises));
    }

    public void setPlannedWorkoutCompleted(PlannedWorkout plannedWorkout){
        CompletableFuture.runAsync(() -> {
            plannedWorkout.is_completed = "true";
            database.getPlannedWorkoutDAO().updatePlannedWorkout(plannedWorkout);
        });
    }

    public Hashtable<String, String> loadStatisticsData(){
        CompletableFuture<Hashtable<String, String>> future = CompletableFuture.supplyAsync(() -> {
            Hashtable<String, String> statistics = new Hashtable<>();

            String workoutsCount = String.valueOf(database.getPlannedWorkoutDAO().getPlannedWorkoutsCount());
            statistics.put("workoutsCount", workoutsCount);
            String completedWorkoutsCount = String.valueOf(database.getPlannedWorkoutDAO().getCompletedPlannedWorkouts("true"));
            statistics.put("completedWorkoutsCount", completedWorkoutsCount);
            String completedWorkoutsLength = String.valueOf(database.getPlannedWorkoutDAO().getSummaryApproximateLength());
            statistics.put("completedWorkoutsLength", completedWorkoutsLength);
            int theMostPreferredExerciseid = database.getPlannedWorkoutExerciseDAO().getTheMostPreferredExercise();

            String theMostPreferredExercise = "";
            if (database.getExerciseDAO().getExerciseById(theMostPreferredExerciseid) != null)
                theMostPreferredExercise = database.getExerciseDAO().getExerciseById(theMostPreferredExerciseid).name;
            statistics.put("theMostPreferredExercise", theMostPreferredExercise);

            return statistics;
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePlannedWorkout(PlannedWorkout plannedWorkout){
        CompletableFuture.runAsync(() -> {
            database.getPlannedWorkoutDAO().deletePlannedWorkout(plannedWorkout);
            database.getPlannedWorkoutExerciseDAO().deletePlannedWorkoutExerciseByWorkoutId(plannedWorkout.id);
        });
    }
}
