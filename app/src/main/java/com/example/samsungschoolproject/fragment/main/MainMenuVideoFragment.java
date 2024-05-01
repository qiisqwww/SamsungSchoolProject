package com.example.samsungschoolproject.fragment.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.samsungschoolproject.R;
import com.example.samsungschoolproject.enums.MainMenuInfoOpenedStates;

public class MainMenuVideoFragment extends Fragment {
    OpenMainMenuInfoFragment openMainMenuInfoFragment;

    public MainMenuVideoFragment(OpenMainMenuInfoFragment openMainMenuInfoFragment){
        this.openMainMenuInfoFragment = openMainMenuInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu_video, container, false);
    }

    public interface OpenMainMenuInfoFragment{
        void openMainMenuInfoFragment(MainMenuInfoOpenedStates mainMenuInfoOpenedStates);
    }
}