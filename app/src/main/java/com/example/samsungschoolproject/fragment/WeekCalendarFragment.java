package com.example.samsungschoolproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.enums.SwitchToWeekStates;
import com.example.samsungschoolproject.database.model.Workout;

import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.WorkoutListAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private CalendarAdapter calendarAdapter;
    private WorkoutListAdapter workoutListAdapter;
    private TextView monthYearTV;
    private RecyclerView calendarRecycler, workoutsList;
    private Button weekBackButton, weekNextButton;
    private RoomDatabase.Callback callback;
    private WorkoutHelperDatabase database;

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

        initDBCallback();
        loadWorkouts(); // Загружает список тренировок из базы данных
    }

    private void initWidgets(View view){
        calendarRecycler = view.findViewById(R.id.weekCalendar);
        workoutsList = view.findViewById(R.id.workoutsRecycler);
        monthYearTV = view.findViewById(R.id.wMonthYearTV);

        weekBackButton = view.findViewById(R.id.weekBack);
        weekNextButton = view.findViewById(R.id.weekNext);
    }

    private void setButtonListeners(){

        weekBackButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.minusWeeks(1);
            setWeekView();
        });

        weekNextButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.plusWeeks(1);
            setWeekView();
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

    private void initDBCallback(){
        callback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
    }

    // Загружает список тренировок из БД.
    private void loadWorkouts(){
        database = Room.databaseBuilder(getContext().getApplicationContext(), WorkoutHelperDatabase.class, "workouthelper")
                .addCallback(callback).createFromAsset("database/workouthelper.db").build();

        ArrayList<Workout   > workouts = new ArrayList<>();
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