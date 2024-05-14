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
import android.widget.ImageButton;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.samsungschoolproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class MainMenuVideoFragment extends Fragment {
    private ImageButton backButton;
    private VideoView motivationVideoView;
    private final OpenMainMenuInfoFragment openMainMenuInfoFragment;
    private final ArrayList<String> videoNames = new ArrayList<>();

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
        initVideoConfig();
        initVideoNames();
        loadVideo();
    }

    private void initWidgets(View view){
        backButton = view.findViewById(R.id.back);
        motivationVideoView = view.findViewById(R.id.motivationVideo);
    }

    private void initButtonListeners(){
        backButton.setOnClickListener(v -> openMainMenuInfoFragment.openMainMenuInfoFragment());
    }

    private void initVideoConfig(){
        motivationVideoView.setOnCompletionListener(v -> openMainMenuInfoFragment.openMainMenuInfoFragment());
    }

    private void initVideoNames(){
        videoNames.add("keep_going.mp4");
        videoNames.add("no_tomorrow.mp4");
        videoNames.add("strong_weak.mp4");
        videoNames.add("trust_me.mp4");
    }

    private void loadVideo(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String videoName = videoNames.get((int)(Math.random() * videoNames.size()));
        StorageReference firstVideo = storageRef.child("workout_motivation_videos/" + videoName);

        firstVideo.getDownloadUrl().addOnSuccessListener(uri -> {
            motivationVideoView.setVideoURI(uri);
            motivationVideoView.start();
        });
    }

    // Открытие фрагмента, содержащего интерфейс (реализация вынесена в MainMenuFragment)
    public interface OpenMainMenuInfoFragment{
        void openMainMenuInfoFragment();
    }
}