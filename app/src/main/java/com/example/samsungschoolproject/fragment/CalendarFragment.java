package com.example.samsungschoolproject.fragment;

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
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class CalendarFragment extends Fragment {
    public static Button switchModeButton;
    public static Fragment nextFragment;
    private BottomSheetBehavior bottomSheetBehavior;

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
        initBottomSheetFragment(view);
    }

    private void initWidgets(View view){
        switchModeButton = view.findViewById(R.id.switchModeButton);
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

    private void switchCalendarMode(){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, nextFragment);
        transaction.commit();
    }

    private void initBottomSheetFragment(View view){
        final BottomSheetFragment bottomFragment = new BottomSheetFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerBottomSheet, bottomFragment)
                .commit();

        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.containerBottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}