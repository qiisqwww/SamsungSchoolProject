package com.example.samsungschoolproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.view_adapter.ViewPagerAdapter;
import com.example.samsungschoolproject.fragment.CalendarFragment;
import com.example.samsungschoolproject.fragment.MainMenuFragment;
import com.example.samsungschoolproject.fragment.TrainTemplatesFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tablayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        );

        viewPagerAdapter.Add(new CalendarFragment(), "Календарь");
        viewPagerAdapter.Add(new MainMenuFragment(), "Меню");
        viewPagerAdapter.Add(new TrainTemplatesFragment(), "Шаблоны");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}