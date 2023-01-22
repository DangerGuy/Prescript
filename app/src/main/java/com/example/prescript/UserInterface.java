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
import android.widget.Button;
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

        TextView medAddMsg = (TextView) findViewById(R.id.medAddMsg);
        medAddMsg.setText("");

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

        List<MainActivity.Medication> meds = MainActivity.constructFromDeconstructedMedicationInfo(
                MainActivity.MEDICATION_NAMES, MainActivity.MEDICATION_DESC, MainActivity.MEDICATION_IS_P,
                MainActivity.MEDICATION_TIMES, MainActivity.MEDICATION_CONFS, medCount, intent
        );

        String medToAdd = ((TextView) findViewById(R.id.typeMedBox)).getText().toString();
        Button medAddBut = (Button) findViewById(R.id.addMedBut);

        medAddBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                1. not in database
                2. already in meds
                3. in database, not in meds, has conflict
                4. in database, not in meds, no conflict
                5. already 3 drugs
                */
                try {

                    MainActivity.Medication addedMed = MainActivity.MED_DATABASE.findMedication(medToAdd);

                    if (MainActivity.hasMedication(meds, medToAdd)) {
                        // case 2
                        medAddMsg.setText("You are already taking " + addedMed.name + ", so no need to add it again.");
                        return;
                    }

                    if (MainActivity.hasConflicts(addedMed, meds)) {
                        // case 3
                        medAddMsg.setText("You cannot add " + addedMed.name
                                + " because it has conflicting medication that you are already taking: "
                                + MainActivity.getConflicts(addedMed, meds) + ".");
                        return;
                    }

                    if (meds.size() == MainActivity.UPPER_LIMIT_OF_MEDS_PER_PERSON) {
                        // case 5
                        medAddMsg.setText("Oops, this prototype Prescript program does not support people taking more than " +
                                MainActivity.UPPER_LIMIT_OF_MEDS_PER_PERSON + " medications.");
                        return;
                    }

                    // case 4
                    medAddMsg.setText("Success, " + addedMed.name + " has been added to the medications you are taking.");
                    int index = meds.size();
                    int medId = selectMed(index);
                    medications.add((TextView) findViewById(medId));
                    SpannableString setTo = new SpannableString(addedMed.name + " (" + addedMed.time + ")   ");

                    ClickableSpan clickableMed = new ClickableSpan() {
                        @Override
                        public void onClick(@NonNull View view) {
                            openMedDetails(addedMed.name, addedMed.description, addedMed.isPrescription);
                        }
                    };

                    setTo.setSpan(clickableMed, 0, setTo.length() - 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    medications.get(index).setText(setTo);
                    medications.get(index).setMovementMethod(LinkMovementMethod.getInstance());

                    meds.add(addedMed);

                } catch (MedicationsDatabase.NoMedicationFoundException e) {
                    // case 1
                    medAddMsg.setText("Sorry, but the medication " + medToAdd + " does not currently exist within our database.");
                }

            }

        });

    }
}