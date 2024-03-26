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
import android.widget.Toast;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class MonthCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private ViewPagerAdapter viewPagerAdapter;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    public MonthCalendarFragment(ViewPagerAdapter viewPagerAdapter){
        this.viewPagerAdapter = viewPagerAdapter;
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
        setButtonListeners(view);

        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initWidgets(View view){
        calendarRecyclerView = view.findViewById(R.id.monthCalendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthMonthYearTV);
    }

    private void setButtonListeners(View view){

        Button backButton = view.findViewById(R.id.monthBackButton);
        Button nextButton = view.findViewById(R.id.monthNextButton);
        Button weekModeButton = view.findViewById(R.id.switchToWeekButton);

        backButton.setOnClickListener(v -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
            setMonthView();
        });

        nextButton.setOnClickListener(v -> {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
            setMonthView();
        });

        weekModeButton.setOnClickListener(v -> {
            viewPagerAdapter.changeCalendarMode(new WeekCalendarFragment(viewPagerAdapter));
        });

    }

    private void setMonthView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}