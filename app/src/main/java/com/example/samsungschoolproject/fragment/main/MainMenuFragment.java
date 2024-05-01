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

import com.example.samsungschoolproject.R;


public class MainMenuFragment extends Fragment implements MainMenuInfoFragment.OpenMainMenuVideoFragment, MainMenuVideoFragment.OpenMainMenuInfoFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main_menu, container, false);
        openMainMenuInfoFragment();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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