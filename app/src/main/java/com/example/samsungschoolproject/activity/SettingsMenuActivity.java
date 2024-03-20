package com.example.samsungschoolproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.samsungschoolproject.activity.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.samsungschoolproject.R;




public class SettingsMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
        Button goBackButton = (Button) findViewById(R.id.back_to_main_menu);
        goBackButton.setOnClickListener(v -> finish());


    };

}