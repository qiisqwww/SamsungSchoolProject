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

public class CalendarFragment extends Fragment implements SwitchModeView{
    // nextFragment содержит фрагмент, который необходимо открыть следующим
    // при нажатии switchModeButton
    private Fragment nextFragment;

    // switchModeButton содержит слово, обозначающее режим календаря ("Месяц" или "Неделя")
    private Button switchModeButton;

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
        initButtonListeners();

        startWeekFragment();
    }

    private void initWidgets(View view){
        switchModeButton = view.findViewById(R.id.switchMode);
    }

    private void initButtonListeners(){
        switchModeButton.setOnClickListener(v -> switchCalendarMode());
    }

    private void startWeekFragment(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, new WeekCalendarFragment(this));
        transaction.commit();
    }

    @Override
    public void setViewToWeekly(){
        nextFragment = new MonthCalendarFragment(this);
        switchModeButton.setText(getResources().getString(R.string.week));
    }

    @Override
    public void setViewToMonthly(){
        nextFragment = new WeekCalendarFragment(this);
        switchModeButton.setText(getResources().getString(R.string.month));
    }

    private void switchCalendarMode(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, nextFragment);
        transaction.commit();
    }
}