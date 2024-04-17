package com.example.samsungschoolproject.fragment.сalendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;

public class CalendarFragment extends Fragment {
    private static Button switchModeButton;

    // nextFragment содержит фрагмент, который необходимо открыть следующим
    // при нажатии switchModeButton
    private static Fragment nextFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        setOnclickListeners();

        startMonthFragment();
    }

    private void initWidgets(View view){
        switchModeButton = view.findViewById(R.id.switchMode);
    }

    private void setOnclickListeners(){
        switchModeButton.setOnClickListener(v -> {
            switchCalendarMode();
        });
    }

    private void startMonthFragment(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, new MonthCalendarFragment());
        transaction.commit();
    }

    public static void setViewToWeekly(String week){
        nextFragment = new MonthCalendarFragment();
        switchModeButton.setText(week);
    }

    public static void setViewToMonthly(String month){
        nextFragment = new WeekCalendarFragment();
        switchModeButton.setText(month);
    }

    private void switchCalendarMode(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, nextFragment);
        transaction.commit();
    }
}