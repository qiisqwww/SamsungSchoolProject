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

import com.example.samsungschoolproject.DTO.WorkoutInfo;
import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.PlannedWorkout;
import com.example.samsungschoolproject.enums.SwitchToWeekStates;
import com.example.samsungschoolproject.database.model.Workout;

import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.utils.WorkoutListUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.WorkoutListAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private CalendarAdapter calendarAdapter;
    private TextView monthYearTV;
    private RecyclerView calendarRecycler, workoutsRecycler;
    private Button weekBackButton, weekNextButton, createNewTemplateButton, addNewWorkoutButton;;
    private TextView noPlannedWorkouts;
    private ViewSwitcher viewSwitcher;

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

        initWidgets(view);
        setButtonListeners();

        // Устанавливается состояние об переходе на "недельныЙ" фрагмент
        CalendarUtils.state = SwitchToWeekStates.JUST_SWITCHED_TO_WEEK_MODE;

        if (CalendarUtils.dateToScroll == null){
            CalendarUtils.dateToScroll = LocalDate.now();
        }
        if (CalendarUtils.selectedDate == null){
            CalendarUtils.selectedDate = CalendarUtils.dateToScroll;
        }
        setWeekView();

        CalendarFragment.nextFragment = new MonthCalendarFragment();
        CalendarFragment.switchModeButton.setText(getResources().getString(R.string.week));

        loadWorkouts(); // Загружает список тренировок из базы данных
    }

    private void initWidgets(View view){
        calendarRecycler = view.findViewById(R.id.weekCalendar);
        workoutsRecycler = view.findViewById(R.id.workoutsRecycler);
        monthYearTV = view.findViewById(R.id.wMonthYearTV);
        noPlannedWorkouts = view.findViewById(R.id.noPlannedWorkouts);

        weekNextButton = view.findViewById(R.id.weekNext);
        weekBackButton = view.findViewById(R.id.weekBack);
        createNewTemplateButton = view.findViewById(R.id.createNewTemplate);
        addNewWorkoutButton = view.findViewById(R.id.addNewWorkout);

        viewSwitcher = view.findViewById(R.id.viewSwitcher);
    }

    private void setButtonListeners(){
        weekNextButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.plusWeeks(1);
            setWeekView();
        });

        weekBackButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.minusWeeks(1);
            setWeekView();
        });

        createNewTemplateButton.setOnClickListener(v -> { // Логика должна быть добавлена

        });

        addNewWorkoutButton.setOnClickListener(v -> { // Логика должна быть добавлена

        });
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
        WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(requireContext().getApplicationContext());
        List<PlannedWorkout> plannedWorkouts = database.getPlannedWorkoutDAO().getPlannedWorkoutsByDate(CalendarUtils.selectedDate.toString());

        if (plannedWorkouts.size() == 0){ // Если нет тренировок на этот день, то нужно выйти из метода
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

        List<WorkoutInfo> workoutsInfo = WorkoutListUtils.parseWorkoutsForAdapter(plannedWorkouts, database);
        setCalendarRecycler(workoutsInfo);
    }

    private void setCalendarRecycler(List<WorkoutInfo> workoutsInfo){
        WorkoutListAdapter workoutListAdapter = new WorkoutListAdapter(workoutsInfo);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutsRecycler.setLayoutManager(layoutManager);
        workoutsRecycler.setAdapter(workoutListAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            // Логика ниже необходима для избежания багов, возникающих с выбором дня при отображении
            // недели, лежащей на стыке двух месяцев.
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
}