package com.example.samsungschoolproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.samsungschoolproject.R;




public class SettingsMenuActivity extends AppCompatActivity {
    Button backToMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        initWidgets();
        initButtonListeners();
    };

    private void initWidgets(){
        backToMenuButton = findViewById(R.id.backToMenu);
    }

    private void initButtonListeners(){
        backToMenuButton.setOnClickListener(v -> finish());
    }

}