package com.example.samsungschoolproject.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.noificator.ExampleNotificator;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private ViewPagerAdapter viewPagerAdapter;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button nextButton, weekModeButton, backButton, newWorkoutButton;

    public WeekCalendarFragment(ViewPagerAdapter viewPagerAdapter){
        this.viewPagerAdapter = viewPagerAdapter;
    }

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

        CalendarUtils.selectedDate = LocalDate.now();
        setWeekView();
    }

    private void initWidgets(View view){
        calendarRecyclerView = view.findViewById(R.id.weekCalendarRecyclerView);
        monthYearText = view.findViewById(R.id.weekMonthYearTV);

        backButton = view.findViewById(R.id.weekBackButton);
        nextButton = view.findViewById(R.id.weekNextButton);
        weekModeButton = view.findViewById(R.id.switchToMonthButton);
        newWorkoutButton = view.findViewById(R.id.newWorkoutButton);
    }

    private void setButtonListeners(View view){

        backButton.setOnClickListener(v -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
            setWeekView();
        });

        nextButton.setOnClickListener(v -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
            setWeekView();
        });

        weekModeButton.setOnClickListener(v -> viewPagerAdapter.changeCalendarMode(new MonthCalendarFragment(viewPagerAdapter)));

        newWorkoutButton.setOnClickListener(v -> {
            ExampleNotificator.scheduleNotification(requireContext().getApplicationContext());
        });
    }

    private void setWeekView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInWeek = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInWeek, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        String message = "Selected Date " + dayText + " " + CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}