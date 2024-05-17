package com.example.samsungschoolproject.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.database.WorkoutHelperDatabase;
import com.example.samsungschoolproject.view.fragment.сalendar.CalendarFragment;
import com.example.samsungschoolproject.model.notificator.PlannedWorkoutNotificator;
import com.example.samsungschoolproject.model.util.TypefaceUtils;
import com.example.samsungschoolproject.model.adapter.main.MainFragmentsAdapter;
import com.example.samsungschoolproject.view.fragment.main.MainMenuFragment;
import com.example.samsungschoolproject.view.fragment.workout.TemplatesFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private ViewPager mainViewPager;
    private TabLayout tabNavigation;
    private MainFragmentsAdapter mainFragmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанавливаетяс тема приложения
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        // Инициалиация объекта БД (singletone)
        initDatabase();

        // Инициализация view'шек и адаптеров
        initWidgets();
        initPagerAdapter();

        // Соединить tab с adapter
        connectWidgetsWithAdapter();

        /* Установить конфиг для TabLayout (иконки, цвета).
        ВЫЗЫВАТЬ СТРОГО ПОСЛЕ connectWidgetsWithAdapter() (т.к. необходимо сначала инициалиировать) */
        configureTabView();

        // Переопределяется установленный для всего приложения шрифт на кастомный
        TypefaceUtils.overrideFont(getApplicationContext(), "SANS_SERIF", "fonts/centurygothic.ttf");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        // Инициализация шедулинга Notificator'а. Вызывается отсюда, т.к. необходим context
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

        // Добавление трех фрагментов
        mainFragmentsAdapter.Add(new CalendarFragment());
        mainFragmentsAdapter.Add(new MainMenuFragment());
        mainFragmentsAdapter.Add(new TemplatesFragment());
    }

    // Вызов метода getInstance, чтобы создать объект БД при создании MainActivity
    private void initDatabase(){
        WorkoutHelperDatabase.getInstance(getApplicationContext());
    }

    private void connectWidgetsWithAdapter(){
        mainViewPager.setAdapter(mainFragmentsAdapter);
        tabNavigation.setupWithViewPager(mainViewPager);
    }

    private void configureTabView(){
        // Установить соответствующие иконки для tabItem's
        tabNavigation.getTabAt(0).setIcon(R.drawable.calendar);
        tabNavigation.getTabAt(1).setIcon(R.drawable.menu);
        tabNavigation.getTabAt(2).setIcon(R.drawable.templates);

        // Устанавливается белый цвет для подсветки иконок
        tabNavigation.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconSelectedColor = ContextCompat.getColor(tab.view.getContext(), R.color.tabIconSelectedColor);
                tab.getIcon().setColorFilter(tabIconSelectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconUnselectedColor = ContextCompat.getColor(tab.view.getContext(), R.color.tabIconUnselectedColor);
                tab.getIcon().setColorFilter(tabIconUnselectedColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Устанавливает MainMenuFragment при открытии MainActivity
        mainViewPager.setCurrentItem(1);
    }

    private void initNotificator(Context context){
        PlannedWorkoutNotificator.scheduleNotification(context);
    }
}