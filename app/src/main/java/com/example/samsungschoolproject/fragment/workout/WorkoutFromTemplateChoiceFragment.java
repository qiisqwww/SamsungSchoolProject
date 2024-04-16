package com.example.samsungschoolproject.fragment.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samsungschoolproject.DTO.WorkoutTemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.WorkoutTemplate;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutTemplateListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class WorkoutFromTemplateChoiceFragment extends BottomSheetDialogFragment implements WorkoutTemplateListAdapter.OnWorkoutTemplateItemListener{
    private WorkoutHelperDatabase database;
    private RecyclerView workoutTemplatesRecycler;
    private TextView headInfoTV;
    private WorkoutTemplateListAdapter workoutTemplateListAdapter;
    private List<WorkoutTemplateInfo> workoutTemplatesInfo;
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

    private void initWidgets(View view){
        workoutTemplatesRecycler = view.findViewById(R.id.workoutTemplatesRecycler);
        headInfoTV = view.findViewById(R.id.headInfoTV);
    }

    private void loadTemplates(){
        List<WorkoutTemplate> workoutTemplates = database.getWorkoutTemplateDAO().getAllWorkoutTemplates();

        if (workoutTemplates.isEmpty()){
            headInfoTV.setText(getResources().getString(R.string.no_templates_yet));
            return;
        }

        workoutTemplatesInfo = WorkoutListUtils.parseWorkoutTemplatesForAdapter(workoutTemplates);
        setWorkoutTemplatesRecycler();
    }

    private void setWorkoutTemplatesRecycler(){
        workoutTemplateListAdapter = new WorkoutTemplateListAdapter(workoutTemplatesInfo, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutTemplatesRecycler.setLayoutManager(layoutManager);
        workoutTemplatesRecycler.setAdapter(workoutTemplateListAdapter);
    }

    @Override
    public void onItemClick(int position) {
        // TODO: Открыть информацию о тренировка
    }
}
