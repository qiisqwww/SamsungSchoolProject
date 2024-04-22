package com.example.samsungschoolproject.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.fragment.сalendar.CalendarFragment;
import com.example.samsungschoolproject.noificator.PlannedWorkoutNotificator;
import com.example.samsungschoolproject.view_adapter.main.MainFragmentsAdapter;
import com.example.samsungschoolproject.fragment.main.MainMenuFragment;
import com.example.samsungschoolproject.fragment.workout.TemplatesFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private ViewPager mainViewPager;
    private TabLayout tabNavigation;
    private MainFragmentsAdapter mainFragmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        initPagerAdapter();
        connectWidgetsWithAdapter();
        initDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        initNotificator(context);
        return super.onCreateView(name, context, attrs);
    }

    private void initWidgets(){
        mainViewPager = findViewById(R.id.mainViewPager);

        tabNavigation = findViewById(R.id.tabNavigation);
        tabNavigation.setTabTextColors(Color.WHITE, Color.WHITE);
    }

    private void initPagerAdapter(){
        mainFragmentsAdapter = new MainFragmentsAdapter(
                getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );

        mainFragmentsAdapter.Add(new CalendarFragment() , getResources().getString(R.string.calendar));
        mainFragmentsAdapter.Add(new MainMenuFragment(), getResources().getString(R.string.menu));
        mainFragmentsAdapter.Add(new TemplatesFragment(), getResources().getString(R.string.templates));
    }

    // Вызов метода getInstance, чтобы создать объект БД при создании MainActivity
    private void initDatabase(){
        WorkoutHelperDatabase.getInstance(getApplicationContext());
    }

    private void connectWidgetsWithAdapter(){
        mainViewPager.setAdapter(mainFragmentsAdapter);
        tabNavigation.setupWithViewPager(mainViewPager);

        mainViewPager.setCurrentItem(1); // Устанавливает MainMenuFragment при открытии MainActivity
    }

    private void initNotificator(Context context){
        PlannedWorkoutNotificator.scheduleNotification(context);
    }
}