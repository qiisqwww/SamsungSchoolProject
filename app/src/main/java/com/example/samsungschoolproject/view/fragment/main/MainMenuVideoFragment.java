package com.example.samsungschoolproject.view.fragment.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.example.samsungschoolproject.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainMenuVideoFragment extends Fragment {
    private ImageButton backButton;
    private VideoView motivationVideoView;
    private final OpenMainMenuInfoFragment openMainMenuInfoFragment;
    private List<String> videoNames;

    public MainMenuVideoFragment(OpenMainMenuInfoFragment openMainMenuInfoFragment){
        this.openMainMenuInfoFragment = openMainMenuInfoFragment;
        videoNames = new ArrayList<>();
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

    private void loadVideo(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference videosDirectoryReference = storage.getReference().child("workout_motivation_videos");

        // Получить список всех доступных StorageReference (по факту список ссылок на все имеющиеся видео)
        videosDirectoryReference.listAll().addOnCompleteListener(listResult -> {
            List<StorageReference> videoReferences = listResult.getResult().getItems();

            // Выбирается случайное видео из доступных и запускается
            videoReferences.get((int) (Math.random() * videoReferences.size())).getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        motivationVideoView.setVideoURI(uri);
                        motivationVideoView.start();
                    });
        });
    }

    // Открытие фрагмента, содержащего интерфейс (реализация вынесена в MainMenuFragment)
    public interface OpenMainMenuInfoFragment{
        void openMainMenuInfoFragment();
    }
}