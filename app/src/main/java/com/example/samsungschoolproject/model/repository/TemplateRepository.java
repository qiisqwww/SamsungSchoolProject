package com.example.samsungschoolproject.model.repository;

import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.entity.Exercise;
import com.example.samsungschoolproject.database.entity.WorkoutTemplate;
import com.example.samsungschoolproject.database.entity.WorkoutTemplateExercise;
import com.example.samsungschoolproject.model.DTO.ExerciseInfo;
import com.example.samsungschoolproject.model.DTO.TemplateInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TemplateRepository {
    private WorkoutHelperDatabase database;

    public TemplateRepository (WorkoutHelperDatabase database){
        this.database  = database;
    }

    public boolean loadNewTemplate(WorkoutTemplate workoutTemplate, ArrayList<ArrayList<String>> unparsedExercises){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // Необходимо проверить, что шаблон с таким именем еще не существует
            List<WorkoutTemplate> workoutTemplates = database.getWorkoutTemplateDAO().getAllWorkoutTemplates();
            for (int i = 0; i < workoutTemplates.size(); i++){
                if (workoutTemplate.name.equals(workoutTemplates.get(i).name)){
                    return "false";
                }
            }

            // Добавляем запись в таблицу workout_templates
            long newWorkoutTemplateID = database.getWorkoutTemplateDAO().addWorkoutTemplate(workoutTemplate);
            int workoutTemplateId = Math.toIntExact(newWorkoutTemplateID);

            // Добавляем связанные записи в таблицу workout_template_exercises
            for (int i = 0; i < unparsedExercises.size(); i++){
                ArrayList<String> exerciseInfo = unparsedExercises.get(i);
                Exercise exercise = database.getExerciseDAO().getExerciseByName(exerciseInfo.get(0));
                database.getWorkoutTemplateExerciseDAO().addWorkoutTemplateExercise(new WorkoutTemplateExercise(
                        workoutTemplateId,
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

    public List<WorkoutTemplate> getAllWorkoutTemplates(){
        CompletableFuture<List<WorkoutTemplate>> future = CompletableFuture.supplyAsync(() -> database.getWorkoutTemplateDAO().getAllWorkoutTemplates());

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public TemplateInfo getTemplateInfo(WorkoutTemplate workoutTemplate){
        CompletableFuture<TemplateInfo> future = CompletableFuture.supplyAsync(() -> {
            List<Exercise> exercises = database.getExerciseDAO().getAllExercises();
            List<WorkoutTemplateExercise> workoutTemplateExercises = database.getWorkoutTemplateExerciseDAO().getWorkoutTemplateExercisesByWorkoutTemplateId(workoutTemplate.id);

            return new TemplateInfo(
                    workoutTemplate,
                    ExerciseInfo.toExerciseInfoListForTemplate(
                            exercises,
                            workoutTemplateExercises)
            );
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTemplate(WorkoutTemplate workoutTemplate){
        CompletableFuture.runAsync(() -> {
            database.getWorkoutTemplateDAO().deleteWorkoutTemplate(workoutTemplate);
            database.getWorkoutTemplateExerciseDAO().deleteWorkoutTemplateExerciseByTemplateId(workoutTemplate.id);
        });
    }


}
