package com.example.prescript;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

public class MainActivity extends AppCompatActivity {


    public static MedicationsDatabase MED_DATABASE = new MedicationsDatabase();

    public static final int UPPER_LIMIT_OF_MEDS_PER_PERSON = 3;
    public static final String DISPLAY_NAME = "com.example.prescript.example.DISPLAY_NAME";
    public static final List<String> MEDICATION_NAMES
            = getConstants("com.example.prescript.example.MEDICATION_NAMES", UPPER_LIMIT_OF_MEDS_PER_PERSON);
    public static final List<String> MEDICATION_TIMES
            = getConstants("com.example.prescript.example.MEDICATION_TIMES", UPPER_LIMIT_OF_MEDS_PER_PERSON);
    public static final List<String> MEDICATION_IS_P
            = getConstants("com.example.prescript.example.MEDICATION_IS_P", UPPER_LIMIT_OF_MEDS_PER_PERSON);
    public static final List<String> MEDICATION_DESC
            = getConstants("com.example.prescript.example.MEDICATION_DESC", UPPER_LIMIT_OF_MEDS_PER_PERSON);
    public static final List<String> MEDICATION_CONFS
            = getConstants("com.example.prescript.example.MEDICATION_CONFS", UPPER_LIMIT_OF_MEDS_PER_PERSON);
    public static final String MEDICATION_COUNT = "com.example.prescript.example.MEDICATION_COUNT";

    private static List<String> getConstants(String constant, int num) {
        List<String> medNames = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            medNames.add(constant + "[" + i + "]");
        }
        return medNames;
    }

    public static class Medication {

        String name, time, description;
        boolean isPrescription;

        List<String> conflicts;
        public Medication(String n, String t, boolean isP, String desc, List<String> confs) {
            name = n;
            time = t;
            isPrescription = isP;
            description = desc;
            conflicts = confs;
        }
    }

    // input: "Banetene,Polybene,Paloene"
    // output: {"Banetene", "Polybene", "Paloene"}
    private static List<String> parseConflictList(String rawConfList) {
        return Arrays.asList(rawConfList.split(","));
    }

    public static List<Medication> constructFromDeconstructedMedicationInfo(
            List<String> names, List<String> descs, List<String> isPs, List<String> times, List<String> conflictLists,
            int medCount, Intent intent) {
        List<Medication> meds = new ArrayList<>();
        for (int i = 0;  i < medCount; i++) {
            meds.add(new Medication(
                    intent.getStringExtra(names.get(i)),
                    intent.getStringExtra(times.get(i)),
                    intent.getBooleanExtra(isPs.get(i), false),
                    intent.getStringExtra(descs.get(i)),
                    parseConflictList(intent.getStringExtra(descs.get(i)))
            ));
        }
        return meds;
    }

    public static Medication getMedication(Collection<Medication> meds, String getName)
            throws MedicationsDatabase.NoMedicationFoundException {
        for (Medication med : meds) {
            if (med.name.equalsIgnoreCase(getName)) {
                return med;
            }
        }
        throw new MedicationsDatabase.NoMedicationFoundException();
    }

    public static boolean hasMedication(Collection<Medication> meds, String getName) {
        try {
            getMedication(meds, getName);
            return true;
        } catch (MedicationsDatabase.NoMedicationFoundException e) {
            return false;
        }
    }

    public static boolean containsIgnoreCase(Collection<String> strings, String searchValue) {
        for (String s : strings) {
            if (s.equalsIgnoreCase(searchValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasConflicts(Medication newMed, Collection<Medication> existingMeds) {
        for (Medication existingMed : existingMeds) {
            if (containsIgnoreCase(newMed.conflicts, existingMed.name)) {
                return true;
            }
        }
        return false;
    }

    public static String getConflicts(Medication newMed, Collection<Medication> existingMeds) {
        String conflicts = "";
        for (Medication existingMed : existingMeds) {
            if (containsIgnoreCase(newMed.conflicts, existingMed.name)) {
                conflicts += ", " + existingMed.name;
            }
        }
        if (conflicts.isEmpty()) {
            return conflicts;
        } else {
            return conflicts.substring(2);
        }
    }

    private static class User {

        public String username, password, displayName;
        List<Medication> meds;

        public User(String u, String p, String d, List<Medication> m) {
            username = u;
            password = p;
            displayName = d;
            meds = m;
        }

        public static boolean containsUsername(Set<User> users, String username) {
            for (User u : users) {
                if (u.username.equals(username)) {
                    return true;
                }
            }
            return false;
        }

        public static boolean userHasPassword(Set<User> users, String username, String password) {
            for (User u : users) {
                if (u.username.equals(username) && u.password.equals(password)) {
                    return true;
                }
            }
            return false;
        }

        public static String getDisplayName(Set<User> users, String username) {
            for (User u : users) {
                if (u.username.equals(username)) {
                    return u.displayName;
                }
            }
            throw new RuntimeException();
        }

        public static List<Medication> getMedicationForUser(Set<User> users, String username) {
            for (User u : users) {
                if (u.username.equals(username)) {
                    return u.meds;
                }
            }
            throw new RuntimeException();
        }

    }

    private static Set<User> initializeLogins() {
        Set<User> users = new HashSet<>();

        try {

            users.add(new User("a", "a", "Admin", new ArrayList<>(Arrays.asList(
                    MED_DATABASE.findMedication("Hydrophil"),
                    MED_DATABASE.findMedication("Polybene")
            ))));

            users.add(new User("b", "b", "Bob-rat", new ArrayList<>(Arrays.asList(
                    MED_DATABASE.findMedication("Polybene"),
                    MED_DATABASE.findMedication("Heptaforis"),
                    MED_DATABASE.findMedication("Lethlisen")
            ))));

        } catch (MedicationsDatabase.NoMedicationFoundException e) {
            throw new RuntimeException();
        }

        return users;
    }

    private static final Set<User> users = initializeLogins();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.editTextTextEmailAddress);
        TextView password = (TextView) findViewById(R.id.editTextTextPassword);

        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(User.userHasPassword(users, username.getText().toString(), password.getText().toString())) {
                    Toast.makeText(MainActivity.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    openUI(User.getDisplayName(users, username.getText().toString()),
                           User.getMedicationForUser(users, username.getText().toString()));
                }else
                    Toast.makeText(MainActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openUI(String name, List<Medication> meds){
        Intent intent = new Intent(this, UserInterface.class);
        intent.putExtra(DISPLAY_NAME, name);
        int i = 0;
        for (Medication med : meds) {
            intent.putExtra(MEDICATION_NAMES.get(i), med.name);
            intent.putExtra(MEDICATION_TIMES.get(i), med.time);
            intent.putExtra(MEDICATION_IS_P.get(i), med.isPrescription);
            intent.putExtra(MEDICATION_DESC.get(i), med.description);
            String allConfsTogether = med.conflicts.isEmpty() ? "" : med.conflicts.get(0);
            for (int ii = 1; ii < med.conflicts.size(); ii++) {
                allConfsTogether += "," + med.conflicts.get(ii);
            }
            intent.putExtra(MEDICATION_CONFS.get(i), allConfsTogether);
            i++;
        }
        intent.putExtra(MEDICATION_COUNT, i);
        startActivity(intent);
    }

}