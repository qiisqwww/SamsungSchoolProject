package com.example.samsungschoolproject.view.fragment.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.R;


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
        openMainMenuInfoFragment();
    }

    @Override
    public void openMainMenuInfoFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.mainMenuContainer, new MainMenuInfoFragment(this))
                .commit();
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