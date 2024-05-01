package com.example.samsungschoolproject.fragment.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.activity.SettingsMenuActivity;
import com.example.samsungschoolproject.enums.MainMenuInfoOpenedStates;


public class MainMenuFragment extends Fragment implements MainMenuInfoFragment.OpenMainMenuVideoFragment, MainMenuVideoFragment.OpenMainMenuInfoFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        openMainMenuInfoFragment(MainMenuInfoOpenedStates.FRAGMENT_FIRST_OPENED);
    }

    @Override
    public void openMainMenuInfoFragment(MainMenuInfoOpenedStates mainMenuInfoOpenedStates){
        if (mainMenuInfoOpenedStates.equals(MainMenuInfoOpenedStates.FRAGMENT_FIRST_OPENED)){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainMenuContainer, new MainMenuInfoFragment(this))
                    .commit();
        }
        if (mainMenuInfoOpenedStates.equals(MainMenuInfoOpenedStates.FRAGMENT_OPENED_FROM_VIDEO)){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(this).commit();
        }
    }

    @Override
    public void openMainMenuVideoFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, new MainMenuVideoFragment(this))
                .addToBackStack("MainMenuInfoFragment")
                .commit();
    }
}