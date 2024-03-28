package com.example.samsungschoolproject.fragment;

import android.annotation.SuppressLint;
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

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.enums.SwitchToWeekStates;
import com.example.samsungschoolproject.noificator.ExampleNotificator;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private CalendarAdapter calendarAdapter;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button nextButton, backButton, newWorkoutButton;

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
        setButtonListeners(view);

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
    }

    private void initWidgets(View view){
        calendarRecyclerView = view.findViewById(R.id.weekCalendarRecyclerView);
        monthYearText = view.findViewById(R.id.weekMonthYearTV);

        backButton = view.findViewById(R.id.weekBackButton);
        nextButton = view.findViewById(R.id.weekNextButton);
        newWorkoutButton = view.findViewById(R.id.newWorkoutButton);
    }

    private void setButtonListeners(View view){

        backButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.minusWeeks(1);
            setWeekView();
        });

        nextButton.setOnClickListener(v -> {
            CalendarUtils.dateToScroll = CalendarUtils.dateToScroll.plusWeeks(1);
            setWeekView();
        });

        newWorkoutButton.setOnClickListener(v -> {
            ExampleNotificator.scheduleNotification(requireContext().getApplicationContext());
        });
    }

    private void setWeekView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.dateToScroll));
        ArrayList<LocalDate> daysInWeek = CalendarUtils.daysInWeekArray(CalendarUtils.dateToScroll);

        calendarAdapter = new CalendarAdapter(daysInWeek, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            CalendarUtils.selectedDate = LocalDate.of(CalendarUtils.dateToScroll.getYear(), CalendarUtils.dateToScroll.getMonth(), Integer.parseInt(dayText));
            calendarAdapter.notifyDataSetChanged();
        }
    }
}