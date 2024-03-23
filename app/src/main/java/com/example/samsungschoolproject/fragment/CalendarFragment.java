package com.example.samsungschoolproject.fragment;

import android.os.Bundle;

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
import com.example.samsungschoolproject.view_adapter.CalendarAdapter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private Button backButton, nextButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);

        initWidgets(view);

        selectedDate = LocalDate.now();
        setMonthView();

        backButton = view.findViewById(R.id.backButton);
        nextButton = view.findViewById(R.id.nextButton);

        backButton.setOnClickListener(v -> {
            selectedDate = selectedDate.minusMonths(1);
            setMonthView();
        });

        nextButton.setOnClickListener(v -> {
            selectedDate = selectedDate.plusMonths(1);
            setMonthView();
        });

        return view;
    }

    private void initWidgets(View view){
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    private void setMonthView(){
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date){
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 2; i <= 41; i++){
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }
            else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (dayText.equals("")) {
            String message = "Selected Date" + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}