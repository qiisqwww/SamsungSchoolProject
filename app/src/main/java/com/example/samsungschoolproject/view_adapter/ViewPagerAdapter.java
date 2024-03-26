package com.example.samsungschoolproject.view_adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object){return POSITION_NONE;}

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void Add(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titles.add(title);
        notifyDataSetChanged();
    }

    public void changeCalendarMode(Fragment fragment){
        fragmentList.set(0, fragment);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}