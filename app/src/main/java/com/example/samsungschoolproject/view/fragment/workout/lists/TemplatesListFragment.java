package com.example.samsungschoolproject.view.fragment.workout.lists;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.samsungschoolproject.DTO.TemplateInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.entity.WorkoutTemplate;
import com.example.samsungschoolproject.enums.TemplatesListStates;
import com.example.samsungschoolproject.database.repository.TemplateRepository;
import com.example.samsungschoolproject.view.fragment.workout.builder.TemplatesBuilderFragment;
import com.example.samsungschoolproject.view.fragment.workout.info.TemplateInfoFragment;
import com.example.samsungschoolproject.adapter.workout.list.WorkoutTemplateListAdapter;

import java.util.List;

public class TemplatesListFragment extends Fragment implements WorkoutTemplateListAdapter.OnWorkoutItemListener, WorkoutTemplateListAdapter.DeleteWorkoutTemplateListener {
    private Button createNewTemplateButton;
    private TemplateRepository repository;
    private List<WorkoutTemplate> workoutTemplates;
    private RecyclerView workoutTemplatesRecycler;
    private WorkoutTemplateListAdapter workoutTemplateListAdapter;
    private ViewSwitcher templatesListSwitcher;
    private TemplatesListStates templatesListStates = TemplatesListStates.LIST_EMPTY_SHOWN;

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

        WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());
        repository = new TemplateRepository(database);

        initWidgets(view);
        initButtonListeners();
        loadTemplatesList();
    }

    private void initWidgets(View view){
        createNewTemplateButton = view.findViewById(R.id.addNewWorkoutTemplate);
        workoutTemplatesRecycler = view.findViewById(R.id.workoutTemplatesRecycler);
        templatesListSwitcher = view.findViewById(R.id.templatesListSwitcher);
    }

    private void initButtonListeners(){
        createNewTemplateButton.setOnClickListener(v -> {
            TemplatesBuilderFragment templatesBuilderFragment = new TemplatesBuilderFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.workoutTemplatesContainer, templatesBuilderFragment)
                    .commit();
        });
    }

    // Загружает список из WorkoutTemplate's из БД
    private void loadTemplatesList() {
        workoutTemplates = repository.getAllWorkoutTemplates();

        setCorrectState(workoutTemplates);
    }

    private void setCorrectState(List<WorkoutTemplate> workoutTemplates){
        if (workoutTemplates.isEmpty()) {
            if (templatesListStates.equals(TemplatesListStates.LIST_NOT_EMPTY_SHOWN)){
                templatesListSwitcher.showNext();
                templatesListStates = TemplatesListStates.LIST_EMPTY_SHOWN;
            }
            return;
        }

        if (templatesListStates.equals(TemplatesListStates.LIST_EMPTY_SHOWN)){
            templatesListSwitcher.showNext();
            templatesListStates = TemplatesListStates.LIST_NOT_EMPTY_SHOWN;
        }
        setWorkoutTemplatesRecycler();
    }

    private void setWorkoutTemplatesRecycler(){
        workoutTemplateListAdapter = new WorkoutTemplateListAdapter(workoutTemplates, this, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutTemplatesRecycler.setLayoutManager(layoutManager);
        workoutTemplatesRecycler.setAdapter(workoutTemplateListAdapter);
    }

    @Override
    public void onWorkoutItemClick(int position) {
        // Загрузить информацию о Template
        WorkoutTemplate workoutTemplate = workoutTemplateListAdapter.getItemByPosition(position);
        TemplateInfo templateInfo = repository.getTemplateInfo(workoutTemplate);

        // Запустить фрагмент, отображающий информацию о Template
        TemplateInfoFragment templateInfoFragment = new TemplateInfoFragment(templateInfo);
        TemplateInfoFragment.TAG = "New Instance"; // idk if this name is important
        templateInfoFragment.show(getActivity().getSupportFragmentManager(), TemplateInfoFragment.TAG);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        WorkoutTemplate workoutTemplate = workoutTemplateListAdapter.getItemByPosition(position);

        // Показать AlertDialog (подтвердить удаление template)
        new AlertDialog.Builder(requireContext(), R.style.AlertTheme)
                .setTitle(R.string.delete_template)
                .setMessage(getResources().getString(R.string.sure_delete_template))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    repository.deleteTemplate(workoutTemplate);
                    deleteTemplateFromView(position, workoutTemplate);
                    Toast.makeText(requireContext().getApplicationContext(), R.string.template_deleted, Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }

    private void deleteTemplateFromView(int position, WorkoutTemplate workoutTemplate){
        // Удалить workout template
        repository.deleteTemplate(workoutTemplate);

        // Удалить Template из отображения
        List<WorkoutTemplate> workoutTemplates = repository.getAllWorkoutTemplates();
        workoutTemplateListAdapter.removeTemplateByPosition(position);
        workoutTemplateListAdapter.notifyItemRemoved(position);
        setCorrectState(workoutTemplates);
    }
}