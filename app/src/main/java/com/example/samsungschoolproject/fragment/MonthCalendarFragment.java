package com.example.samsungschoolproject.fragment;

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

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.enums.SwitchToWeekStates;
import com.example.samsungschoolproject.model.Workout;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.WorkoutListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDate;
import java.util.ArrayList;


public class MonthCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private CalendarAdapter calendarAdapter;
    private TextView monthYearText;
    private ModalBottomSheetFragment modalBottomSheet;
    private RecyclerView calendarRecyclerView;
    private Button backButton, nextButton;



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
        setButtonListeners(view);

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
        calendarRecyclerView = view.findViewById(R.id.monthCalendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthMonthYearTV);

        backButton = view.findViewById(R.id.monthBackButton);
        nextButton = view.findViewById(R.id.monthNextButton);
    }

    private void setButtonListeners(View view){
        backButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.minusMonths(1);
            setMonthView();
        });

        nextButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.plusMonths(1);
            setMonthView();
        });
    }



    private void setMonthView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.dateToScroll));
        ArrayList<LocalDate> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.dateToScroll);

        calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
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
        private RecyclerView workoutsList;
        private Button createNewTemplateButton, addNewWorkout;
        private WorkoutListAdapter workoutListAdapter;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.day_info, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            initWidgets(view);
            loadWorkouts();
        }

        private void initWidgets(View view){
            workoutsList = view.findViewById(R.id.workoutList);
            createNewTemplateButton = view.findViewById(R.id.createNewTemplateButton);
            addNewWorkout = view.findViewById(R.id.addNewWorkout);
        }

        private void loadWorkouts(){
            ArrayList<Workout> workouts = new ArrayList<>();
            workouts.add(new Workout("КАЧАЕМ СИСЕЧЬКИ))0)", LocalDate.now().toString(), 120, "TRUE")); // Here must be a logic of filling a workout list from db
            workouts.add(new Workout("КАЧАЕМ ПРЕССИК)))0)))", LocalDate.now().toString(), 70, "TRUE"));
            workouts.add(new Workout("ЗАВТРА Я НЕ ВСТАНУ)))))", LocalDate.now().toString(), 85, "TRUE"));

            workoutListAdapter = new WorkoutListAdapter(workouts);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
            workoutsList.setLayoutManager(layoutManager);
            workoutsList.setAdapter(workoutListAdapter);
        }
    }
}