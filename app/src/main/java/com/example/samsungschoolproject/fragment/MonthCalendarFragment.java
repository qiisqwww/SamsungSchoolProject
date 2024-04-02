package com.example.samsungschoolproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;


public class MonthCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private CalendarAdapter calendarAdapter;
    private TextView monthYearTV;
    private ModalBottomSheetFragment modalBottomSheet;
    private RecyclerView calendarRecycler;
    private Button monthBackButton, monthNextButton;



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

        CalendarUtils.state = SwitchToWeekStates.NOT_JUST_SWITCHED_TO_WEEK_MODE;

        if (CalendarUtils.dateToScroll == null){
            CalendarUtils.dateToScroll = LocalDate.now();
        }
        if (CalendarUtils.selectedDate == null){
            CalendarUtils.selectedDate = CalendarUtils.dateToScroll;
        }
        setMonthView();

        CalendarFragment.nextFragment = new WeekCalendarFragment();
        CalendarFragment.switchModeButton.setText(getResources().getString(R.string.month));
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
        ModalBottomSheetFragment.TAG = "New Instance";

        modalBottomSheet.show(getActivity().getSupportFragmentManager(), ModalBottomSheetFragment.TAG);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            CalendarUtils.selectedDate = LocalDate.of(CalendarUtils.dateToScroll.getYear(), CalendarUtils.dateToScroll.getMonth(), Integer.parseInt(dayText));
            calendarAdapter.resetBacklitItem(position);
        }

        initBottomSheetFragment();
    }

    public static class ModalBottomSheetFragment extends BottomSheetDialogFragment {
        public static String TAG;
        private RecyclerView workoutsRecycler;
        private Button createNewTemplateButton, addNewWorkoutButton;
        private WorkoutListAdapter workoutListAdapter;
        private RoomDatabase.Callback callback;
        private WorkoutHelperDatabase database;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.day_info, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            initWidgets(view);
            initDBCallback();
            loadWorkouts(); // Загружает список тренировок из базы данных
        }

        private void initWidgets(View view){
            workoutsRecycler = view.findViewById(R.id.workoutsRecycler);
            createNewTemplateButton = view.findViewById(R.id.createNewTemplate);
            addNewWorkoutButton = view.findViewById(R.id.addNewWorkout);
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
            database = Room.databaseBuilder(getContext().getApplicationContext(), WorkoutHelperDatabase.class, "workout_helper")
                    .addCallback(callback).createFromAsset("database/workouthelper.db").build();

            ArrayList<Workout> workouts = new ArrayList<>();
            workouts.add(new Workout("КАЧАЕМ СИСЕЧЬКИ))0)", LocalDate.now().toString(), 120, "TRUE")); // Here must be a logic of filling a workout list from db
            workouts.add(new Workout("КАЧАЕМ ПРЕССИК)))0)))", LocalDate.now().toString(), 70, "TRUE"));
            workouts.add(new Workout("ЗАВТРА Я НЕ ВСТАНУ)))))", LocalDate.now().toString(), 85, "TRUE"));

            workoutListAdapter = new WorkoutListAdapter(workouts);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
            workoutsRecycler.setLayoutManager(layoutManager);
            workoutsRecycler.setAdapter(workoutListAdapter);
        }
    }
}