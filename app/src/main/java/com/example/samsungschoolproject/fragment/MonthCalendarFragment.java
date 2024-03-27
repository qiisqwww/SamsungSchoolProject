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
import com.example.samsungschoolproject.utils.CalendarUtils;
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;

import java.time.LocalDate;
import java.util.ArrayList;


public class MonthCalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private ViewPagerAdapter viewPagerAdapter;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button backButton, nextButton, weekModeButton;

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

        CalendarUtils.justSwitchedFromMonth = false;

        if (CalendarUtils.dateToScroll == null){
            CalendarUtils.dateToScroll = LocalDate.now();
        }
        if (CalendarUtils.selectedDate == null){
            CalendarUtils.selectedDate = CalendarUtils.dateToScroll;
        }
        setMonthView();
    }

    private void initWidgets(View view){
        calendarRecyclerView = view.findViewById(R.id.monthCalendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthMonthYearTV);

        backButton = view.findViewById(R.id.monthBackButton);
        nextButton = view.findViewById(R.id.monthNextButton);
        weekModeButton = view.findViewById(R.id.switchToWeekButton);
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

        weekModeButton.setOnClickListener(v -> {
            viewPagerAdapter.changeCalendarMode(new WeekCalendarFragment(viewPagerAdapter));
        });

    }

    private void setMonthView(){
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.dateToScroll));
        ArrayList<LocalDate> daysInMonth = CalendarUtils.daysInMonthArray(CalendarUtils.dateToScroll);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            CalendarUtils.selectedDate = LocalDate.of(CalendarUtils.dateToScroll.getYear(), CalendarUtils.dateToScroll.getMonth(), Integer.parseInt(dayText));
            viewPagerAdapter.notifyDataSetChanged();
        }
    }
}