package com.example.samsungschoolproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.database.model.Workout;
import com.example.samsungschoolproject.fragment.CalendarFragment;
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;
import com.example.samsungschoolproject.fragment.MainMenuFragment;
import com.example.samsungschoolproject.fragment.WorkoutTemplatesFragment;
import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    private ViewPager mainViewPager;
    private TabLayout tabNavigation;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        initPagerAdapter();
        connectWidgetsWithAdapter();
        initDatabase();
    }

    private void initWidgets(){
        mainViewPager = findViewById(R.id.mainViewPager);
        tabNavigation = findViewById(R.id.tabNavigation);
    }

    private void initPagerAdapter(){
        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );

        viewPagerAdapter.Add(new CalendarFragment() , getResources().getString(R.string.calendar));
        viewPagerAdapter.Add(new MainMenuFragment(), getResources().getString(R.string.menu));
        viewPagerAdapter.Add(new WorkoutTemplatesFragment(), getResources().getString(R.string.templates));
    }

    private void initDatabase(){
        WorkoutHelperDatabase database = WorkoutHelperDatabase.getInstance(getApplicationContext());
    }

    private void connectWidgetsWithAdapter(){
        mainViewPager.setAdapter(viewPagerAdapter);
        tabNavigation.setupWithViewPager(mainViewPager);

        mainViewPager.setCurrentItem(1); // Устанавливает MainMenuFragment при открытии MainActivity
    }
}