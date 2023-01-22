package com.example.prescript;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class userInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.DISPLAY_NAME);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(text);

        List<TextView> medications = new ArrayList<>();

        int medCount = intent.getIntExtra(MainActivity.MEDICATION_COUNT, 0);
        for (int i = 0; i < medCount; i++) {
            String medName = intent.getStringExtra(MainActivity.MEDICATION_NAMES.get(i));
            String medTime = intent.getStringExtra(MainActivity.MEDICATION_TIMES.get(i));
            medications.add((TextView) findViewById(R.id.textView));
            String setTo = medName + " (" + medTime + ")";
            medications.get(i).setText(setTo);
        }

    }
}