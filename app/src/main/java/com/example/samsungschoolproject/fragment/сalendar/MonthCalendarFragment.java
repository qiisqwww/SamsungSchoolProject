package com.example.samsungschoolproject.fragment.сalendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.samsungschoolproject.DTO.ExerciseInfo;
import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.Exercise;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.database.model.PlannedWorkoutExercise;
import com.example.samsungschoolproject.enums.BackFragmentForBuilderStates;
import com.example.samsungschoolproject.enums.SwitchToWeekStates;
import com.example.samsungschoolproject.fragment.workout.info.WorkoutInfoFragment;
import com.example.samsungschoolproject.fragment.workout.lists.WorkoutFromTemplateListFragment;
import com.example.samsungschoolproject.fragment.workout.builder.WorkoutsBuilderFragment;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.calendar.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.workout.list.WorkoutListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class MonthCalendarFragment extends Fragment implements CalendarAdapter.OnCalendarItemListener{
    private CalendarAdapter calendarAdapter;
    private TextView monthYearTV;
    private ModalBottomSheetFragment modalBottomSheet;
    private RecyclerView calendarRecycler;
    private Button monthBackButton, monthNextButton;
    private SwitchModeView switchModeView;

    public MonthCalendarFragment(SwitchModeView switchModeView){
        this.switchModeView = switchModeView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_month_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        setButtonListeners();

        setCurrentState();
        setMonthView();
    }

    private void initWidgets(View view){
        calendarRecycler = view.findViewById(R.id.monthCalendar);
        monthYearTV = view.findViewById(R.id.mMonthYearTV);

        monthBackButton = view.findViewById(R.id.monthBack);
        monthNextButton = view.findViewById(R.id.monthNext);
    }

    private void setButtonListeners(){
        monthBackButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.minusMonths(1);
            setMonthView();
        });

        monthNextButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.plusMonths(1);
            setMonthView();
        });
    }


    // Устанавливает корректное состояние для объектов
    private void setCurrentState(){
        CalendarUtils.state = SwitchToWeekStates.NOT_JUST_SWITCHED_TO_WEEK_MODE;

        if (CalendarUtils.dateToScroll == null){
            CalendarUtils.dateToScroll = LocalDate.now();
        }
        if (CalendarUtils.selectedDate == null){
            CalendarUtils.selectedDate = CalendarUtils.dateToScroll;
        }

        switchModeView.setViewToMonthly();
    }

    // Отрисовка "месячного" режима календаря
    private void setMonthView(){
        monthYearTV.setText(CalendarUtils.monthYearFromDate(CalendarUtils.dateToScroll));
        ArrayList<LocalDate> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.dateToScroll);

        calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecycler.setLayoutManager(layoutManager);
        calendarRecycler.setAdapter(calendarAdapter);
    }

    private void initBottomSheetFragment(){
        modalBottomSheet = new ModalBottomSheetFragment();
        ModalBottomSheetFragment.TAG = "New Instance"; // idk if this name is important

        modalBottomSheet.show(getActivity().getSupportFragmentManager(), ModalBottomSheetFragment.TAG);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.isEmpty()) {
            CalendarUtils.selectedDate = LocalDate.of(CalendarUtils.dateToScroll.getYear(), CalendarUtils.dateToScroll.getMonth(), Integer.parseInt(dayText));
            calendarAdapter.resetBacklitItem(position);
        }

        initBottomSheetFragment();
    }

    public static class ModalBottomSheetFragment extends BottomSheetDialogFragment implements
            WorkoutListAdapter.OnWorkoutItemListener,
            WorkoutListAdapter.UpdateRecycler,
            WorkoutListAdapter.SetWorkoutMarked{
        public static String TAG;
        private WorkoutHelperDatabase database;
        private RecyclerView workoutsRecycler;
        private WorkoutListAdapter workoutListAdapter;
        private Button loadFromTemplatesButton, addNewWorkoutButton;
        private ViewSwitcher viewSwitcher;
        private TextView noPlannedWorkouts;
        private List<PlannedWorkout> plannedWorkouts;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.day_info, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());

            // TODO: возможно, это костыль. Необходимо переделать.
            // Код ниже необходим для корректной работы "обновления" данных после создания новой тренировки.
            workoutListAdapter = new WorkoutListAdapter(new ArrayList<>(), this, this, this);

            initWidgets(view);
            loadWorkouts(); // Загружает список тренировок из базы данных
            initButtonListeners();
        }

        private void initWidgets(View view) {
            workoutsRecycler = view.findViewById(R.id.workoutsRecycler);
            noPlannedWorkouts = view.findViewById(R.id.noPlannedWorkouts);

            loadFromTemplatesButton = view.findViewById(R.id.loadFromTemplates);
            addNewWorkoutButton = view.findViewById(R.id.addNewWorkout);

            viewSwitcher = view.findViewById(R.id.viewSwitcher);
        }

        private void initButtonListeners(){
            loadFromTemplatesButton.setOnClickListener(v -> {
                if (CalendarUtils.selectedDate.isBefore(LocalDate.now())){
                    Toast.makeText(addNewWorkoutButton.getContext(), R.string.unable_to_create_workout, Toast.LENGTH_SHORT).show(); // TODO: поработать над визуалом
                    return;
                }

                WorkoutFromTemplateListFragment workoutFromTemplateListFragment = new WorkoutFromTemplateListFragment(workoutListAdapter);
                WorkoutFromTemplateListFragment.TAG = "New Instance"; // idk if this name is important

                workoutFromTemplateListFragment.show(getActivity().getSupportFragmentManager(), WorkoutFromTemplateListFragment.TAG);
            });

            addNewWorkoutButton.setOnClickListener(v -> {
                if (CalendarUtils.selectedDate.isBefore(LocalDate.now())){
                    Toast.makeText(addNewWorkoutButton.getContext(), R.string.unable_to_create_workout, Toast.LENGTH_SHORT).show(); // TODO: поработать над визуалом
                    return;
                }

                WorkoutsBuilderFragment workoutsBuilderFragment = new WorkoutsBuilderFragment(BackFragmentForBuilderStates.BACK_TO_WEEK_FRAGMENT, workoutListAdapter);
                WorkoutsBuilderFragment.TAG = "Another Instance"; // idk if this name is important

                workoutsBuilderFragment.show(getActivity().getSupportFragmentManager(), WorkoutsBuilderFragment.TAG);
            });
        }

        // Загружает список тренировок из БД.
        private void loadWorkouts(){
            CompletableFuture<List<PlannedWorkout>> future = CompletableFuture.supplyAsync(() -> database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(CalendarUtils.selectedDate.toString()));
            try {
                plannedWorkouts = future.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (plannedWorkouts.isEmpty()){/*
                 Если нет тренировок на этот день, то нужно выйти из метода
                 Если не установлено сообщение о том, что нет тренировок, то установить его
                */
                if (viewSwitcher.getCurrentView() != noPlannedWorkouts){
                    viewSwitcher.showNext();
                }
                return;
            }

            /* Если установлено сообщение о том, что нет тренировок, но предыдущие условия не выполнились, то нужно переключить view */
            if (viewSwitcher.getCurrentView() == noPlannedWorkouts){
                viewSwitcher.showNext();
            }

            setWorkoutsRecycler();
        }

        private void setWorkoutsRecycler(){
            workoutListAdapter = new WorkoutListAdapter(plannedWorkouts, this, this, this);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
            workoutsRecycler.setLayoutManager(layoutManager);
            workoutsRecycler.setAdapter(workoutListAdapter);
        }

        @Override
        public void onWorkoutItemClick(int position) {
            PlannedWorkout plannedWorkout = workoutListAdapter.getItemByPosition(position);

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

            WorkoutInfo workoutInfo = new WorkoutInfo(plannedWorkout, ExerciseInfo.toExerciseInfoListForPlanned(exercises, plannedWorkoutExercises));

            WorkoutInfoFragment workoutInfoFragment = new WorkoutInfoFragment(workoutInfo);
            WorkoutInfoFragment.TAG = "New Instance"; // idk if this name is important

            workoutInfoFragment.show(getActivity().getSupportFragmentManager(), WorkoutInfoFragment.TAG);
        }

        @Override
        public void updateRecycler() {
            loadWorkouts();
        }

        @Override
        public void setWorkoutMarked(PlannedWorkout plannedWorkout) {
            CompletableFuture.supplyAsync(() -> {
                plannedWorkout.is_completed = "true";
                database.getPlannedWorkoutDAO().updatePlannedWorkout(plannedWorkout);

                return null;
            });
        }
    }
}