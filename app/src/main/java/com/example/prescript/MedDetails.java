package com.example.prescript;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MedDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_details);

        Intent intent = getIntent();

        String name = intent.getStringExtra(UserInterface.MEDICATION_NAME);
        TextView nameTV = (TextView) findViewById(R.id.medicationName);
        nameTV.setText(name);

        String isP = intent.getBooleanExtra(UserInterface.MEDICATION_IS_P, false) ?
                "This medication is prescribed." : "This medication is over the counter.";
        TextView pTV = (TextView) findViewById(R.id.medicationIsP);
        pTV.setText(isP);

        String desc = intent.getStringExtra(UserInterface.MEDICATION_DESC);
        TextView descTV = (TextView) findViewById(R.id.medicationDesc);
        descTV.setText(desc);
    }
}