package com.example.samsungschoolproject.fragment.main;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.samsungschoolproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

public class MainMenuVideoFragment extends Fragment {
    private Button backButton;
    private VideoView motivationVideoView;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        initButtonListeners();
        loadVideo();
    }

    private void initWidgets(View view){
        backButton = view.findViewById(R.id.back);
        motivationVideoView = view.findViewById(R.id.motivationVideo);
    }

    private void initButtonListeners(){
        backButton.setOnClickListener(v -> openMainMenuInfoFragment.openMainMenuInfoFragment());
    }

    private void loadVideo(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference firstVideo = storageRef.child("workout_motivation_videos/calm_motivation_edit.mp4");

        firstVideo.getDownloadUrl().addOnSuccessListener(uri -> {
            motivationVideoView.setVideoURI(uri);
            motivationVideoView.start();
        });
    }

    public interface OpenMainMenuInfoFragment{
        void openMainMenuInfoFragment();
    }
}