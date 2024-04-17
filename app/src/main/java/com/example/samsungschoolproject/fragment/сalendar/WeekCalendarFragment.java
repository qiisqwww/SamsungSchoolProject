package com.example.samsungschoolproject.fragment.сalendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.enums.BackFragmentForBuilderStates;
import com.example.samsungschoolproject.enums.SwitchToWeekStates;

import com.example.samsungschoolproject.fragment.workout.lists.WorkoutFromTemplateListFragment;
import com.example.samsungschoolproject.fragment.workout.builder.WorkoutsBuilderFragment;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.calendar.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.workout.WorkoutListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnCalendarItemListener, WorkoutListAdapter.OnWorkoutItemListener, WorkoutListAdapter.UpdateRecycler{
    private CalendarAdapter calendarAdapter;
    private WorkoutListAdapter workoutListAdapter;
    private TextView monthYearTV;
    private RecyclerView calendarRecycler, workoutsRecycler;
    private Button weekBackButton, weekNextButton, loadFromTemplatesButton, addNewWorkoutButton;
    private BottomSheetDialogFragment workoutsBuilderFragment;
    private TextView noPlannedWorkouts;
    private ViewSwitcher viewSwitcher;
    private WorkoutHelperDatabase database;
    private List<PlannedWorkout> plannedWorkouts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());

        // TODO: возможно, это костыль. Необходимо переделать.
        // Код ниже необходим для корректной работы "обновления" данных после создания новой тренировки.
        workoutListAdapter = new WorkoutListAdapter(new ArrayList<>(), this, this);


        initWidgets(view);
        setCurrentState();

        setWeekView();
        loadWorkouts(); // Загружает список тренировок из базы данных
        initButtonListeners();
    }

    private void initWidgets(View view){
        calendarRecycler = view.findViewById(R.id.weekCalendar);
        workoutsRecycler = view.findViewById(R.id.workoutsRecycler);
        monthYearTV = view.findViewById(R.id.wMonthYearTV);
        noPlannedWorkouts = view.findViewById(R.id.noPlannedWorkouts);

        weekNextButton = view.findViewById(R.id.weekNext);
        weekBackButton = view.findViewById(R.id.weekBack);
        loadFromTemplatesButton = view.findViewById(R.id.loadFromTemplates);
        addNewWorkoutButton = view.findViewById(R.id.addNewWorkout);

        viewSwitcher = view.findViewById(R.id.viewSwitcher);
    }

    private void initButtonListeners(){
        weekNextButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.plusWeeks(1);
            setWeekView();
        });

        weekBackButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.minusWeeks(1);
            setWeekView();
        });

        loadFromTemplatesButton.setOnClickListener(v -> {
            WorkoutFromTemplateListFragment workoutFromTemplateListFragment = new WorkoutFromTemplateListFragment(workoutListAdapter);
            WorkoutFromTemplateListFragment.TAG = "New Instance"; // idk if this name is important

           workoutFromTemplateListFragment.show(getActivity().getSupportFragmentManager(), WorkoutFromTemplateListFragment.TAG);
        });

        addNewWorkoutButton.setOnClickListener(v -> {
            workoutsBuilderFragment = new WorkoutsBuilderFragment(BackFragmentForBuilderStates.BACK_TO_WEEK_FRAGMENT, workoutListAdapter);
            WorkoutsBuilderFragment.TAG = "Another Instance"; // idk if this name is important

            workoutsBuilderFragment.show(getActivity().getSupportFragmentManager(), WorkoutsBuilderFragment.TAG);
        });
    }

    // Устанавливает корректное состояние для объектов
    private void setCurrentState(){
        // Устанавливается состояние об переходе на "недельныЙ" фрагмент
        CalendarUtils.state = SwitchToWeekStates.JUST_SWITCHED_TO_WEEK_MODE;

        if (CalendarUtils.dateToScroll == null){
            CalendarUtils.dateToScroll = LocalDate.now();
        }
        if (CalendarUtils.selectedDate == null){
            CalendarUtils.selectedDate = CalendarUtils.dateToScroll;
        }

        CalendarFragment.setViewToWeekly(getResources().getString(R.string.week));
    }

    // Отрисовка "недельного" режима календаря
    private void setWeekView(){
        monthYearTV.setText(CalendarUtils.monthYearFromDate(CalendarUtils.dateToScroll)) ;
        ArrayList<LocalDate> daysInWeek = CalendarUtils.daysInWeekArray(CalendarUtils.dateToScroll);

        calendarAdapter = new CalendarAdapter(daysInWeek, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecycler.setLayoutManager(layoutManager);
        calendarRecycler.setAdapter(calendarAdapter);
    }

    // Загружает список тренировок из БД.
    private void loadWorkouts(){
        plannedWorkouts = database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(CalendarUtils.selectedDate.toString());

        if (plannedWorkouts.isEmpty()){ // Если нет тренировок на этот день, то нужно выйти из метода
            // Если не установлено сообщение о том, что нет тренировок, то установить его
            if (viewSwitcher.getCurrentView() != noPlannedWorkouts){
                viewSwitcher.showNext();
            }
            return;
        }

        // Если установлено сообщение о том, что нет тренировок, но предыдущие условия не выполнились, то нужно переключить view
        if (viewSwitcher.getCurrentView() == noPlannedWorkouts){
            viewSwitcher.showNext();
        }

        setWorkoutsRecycler();
    }

    private void setWorkoutsRecycler(){
        workoutListAdapter = new WorkoutListAdapter(plannedWorkouts, this, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutsRecycler.setLayoutManager(layoutManager);
        workoutsRecycler.setAdapter(workoutListAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.isEmpty()) {
            // Логика ниже необходима для избежания багов, возникающих с выбором дня при отображении недели, лежащей на стыке двух месяцев.
            // shift - сдвиг, который необходимо учесть в selectedDate, чтобы правильно отрисовать изменения
            int shift = 0;
            if (Integer.parseInt(dayText) < CalendarUtils.dateToScroll.getDayOfMonth() - 15){
                shift = 1;
            }
            if (Integer.parseInt(dayText) - 15 > CalendarUtils.dateToScroll.getDayOfMonth()){
                shift = -1;
            }

            CalendarUtils.selectedDate = LocalDate.of(CalendarUtils.dateToScroll.getYear(), CalendarUtils.dateToScroll.getMonth().plus(shift), Integer.parseInt(dayText));
            calendarAdapter.resetBacklitItem(position);

            loadWorkouts();
        }
    }

    // TODO: Добавить вывод информации о тренировке по нажатии на нее
    @Override
    public void onWorkoutItemClick(int position) {

    }

    @Override
    public void updateRecycler() {
        loadWorkouts();
    }
}