package com.example.prescript;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserInterface extends AppCompatActivity {

    public static final String MEDICATION_NAME = "com.example.userInterface.example.MEDICATION_NAME";
    public static final String MEDICATION_IS_P = "com.example.userInterface.example.MEDICATION_IS_P";
    public static final String MEDICATION_DESC = "com.example.userInterface.example.MEDICATION_DESC";

    private static int selectMed(int i) {
        switch (i) {
            case 0:
                return R.id.med0;
            case 1:
                return R.id.med1;
            case 2:
                return R.id.med2;
        }
        throw new RuntimeException();
    }

    public void openMedDetails(String medName, String medDesc, boolean medIsP) {
        Intent intent = new Intent(this, MedDetails.class);
        intent.putExtra(MEDICATION_NAME, medName);
        intent.putExtra(MEDICATION_DESC, medDesc);
        intent.putExtra(MEDICATION_IS_P, medIsP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);

        Intent intent = getIntent();
        String text = intent.getStringExtra(MainActivity.DISPLAY_NAME);
        TextView textView = (TextView) findViewById(R.id.displayName);
        textView.setText(text);

        List<TextView> medications = new ArrayList<>();

        int medCount = intent.getIntExtra(MainActivity.MEDICATION_COUNT, 0);
        int i;
        for (i = 0; i < medCount; i++) {
            String medName = intent.getStringExtra(MainActivity.MEDICATION_NAMES.get(i));
            String medTime = intent.getStringExtra(MainActivity.MEDICATION_TIMES.get(i));
            String medDesc = intent.getStringExtra(MainActivity.MEDICATION_DESC.get(i));
            boolean medIsP = intent.getBooleanExtra(MainActivity.MEDICATION_IS_P.get(i), false);
            medications.add((TextView) findViewById(selectMed(i)));
            SpannableString setTo = new SpannableString(medName + " (" + medTime + ")   ");

            ClickableSpan clickableMed = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    openMedDetails(medName, medDesc, medIsP);
                }
            };

            setTo.setSpan(clickableMed, 0, setTo.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            medications.get(i).setText(setTo);
            medications.get(i).setMovementMethod(LinkMovementMethod.getInstance());
        }

        for (; i < MainActivity.UPPER_LIMIT_OF_MEDS_PER_PERSON; i++) {
            medications.add((TextView) findViewById(selectMed(i)));
            String setTo = "";
            medications.get(i).setText(setTo);
        }

    }
}