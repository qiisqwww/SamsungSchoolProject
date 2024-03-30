package com.example.samsungschoolproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.enums.SwitchToWeekStates;
import com.example.samsungschoolproject.model.Workout;
import com.example.samsungschoolproject.noificator.ExampleNotificator;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;
import com.example.samsungschoolproject.view_adapter.WorkoutListAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private CalendarAdapter calendarAdapter;
    private WorkoutListAdapter workoutListAdapter;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView, workoutsList;
    private Button nextButton, backButton;


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
        calendarRecyclerView = view.findViewById(R.id.weekCalendarRecyclerView);
        workoutsList = view.findViewById(R.id.workoutList);
        monthYearText = view.findViewById(R.id.weekMonthYearTV);

        backButton = view.findViewById(R.id.weekBackButton);
        nextButton = view.findViewById(R.id.weekNextButton);
    }

    private void setButtonListeners(){

        backButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.minusWeeks(1);
            setWeekView();
        });

        nextButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.plusWeeks(1);
            setWeekView();
        });
    }

    // Отрисовка "недельного" режима календаря
    private void setWeekView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.dateToScroll)) ;
        ArrayList<LocalDate> daysInWeek = CalendarUtils.daysInWeekArray(CalendarUtils.dateToScroll);

        calendarAdapter = new CalendarAdapter(daysInWeek, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    // Загружает список тренировок из БД. Пока что логика работы с БД не реализована
    private void loadWorkouts(){
        ArrayList<Workout> workouts = new ArrayList<>();
        workouts.add(new Workout("1234", LocalDate.now().toString(), 120, "TRUE")); // Here must be a logic of filling a workout list from db
        workouts.add(new Workout("4321", LocalDate.now().toString(), 70, "TRUE"));
        workouts.add(new Workout("12344", LocalDate.now().toString(), 85, "TRUE"));

        workoutListAdapter = new WorkoutListAdapter(workouts);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        workoutsList.setLayoutManager(layoutManager);
        workoutsList.setAdapter(workoutListAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            // Логика ниже необходима для избежания багов, возникающих с выбором дня при отображении
            // недели, лежащей на стыке двух месяцев.
            // shift - сдвиг, который необходимо учесть в selectedDate, чтобы правильно отрисовать
            // изменения
            int shift = 0;
            if (Integer.parseInt(dayText) < CalendarUtils.dateToScroll.getDayOfMonth() - 15){
                shift = 1;
            }
            if (Integer.parseInt(dayText) - 15 > CalendarUtils.dateToScroll.getDayOfMonth()){
                shift = -1;
            }

            CalendarUtils.selectedDate = LocalDate.of(CalendarUtils.dateToScroll.getYear(), CalendarUtils.dateToScroll.getMonth().plus(shift), Integer.parseInt(dayText));
            calendarAdapter.resetBacklitItem(position);
        }
    }
}