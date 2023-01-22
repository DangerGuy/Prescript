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
        public Medication(String n, String t, boolean isP, String desc) {
            name = n;
            time = t;
            isPrescription = isP;
            description = desc;
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

        public static List<Medication> getMedication(Set<User> users, String username) {
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

        users.add(new User("a", "a", "Admin", new ArrayList<>(Arrays.asList(
                new Medication("Hydrophil", "9:00 a.m. daily", true,
                        "a truly competent drug that will make you fly"),
                new Medication("Polybene", "12:00 p.m. once every 2 days", false,
                        "a medicine that makes you grow 4 legs")
        ))));

        users.add(new User("b", "b", "Bob-rat", new ArrayList<>(Arrays.asList(
                new Medication("Banetane", "once every week at 4:00 p.m.", false,
                        "this medication makes you invincible!"),
                new Medication("Heptaforis", "every day at 9 a.m. and 9 p.m.", true,
                        "makes you live forever"),
                new Medication("Lethlisen", "twice a day, at any time", false,
                        "makes you invisible")
        ))));

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
                           User.getMedication(users, username.getText().toString()));
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
            i++;
        }
        intent.putExtra(MEDICATION_COUNT, i);
        startActivity(intent);
    }

}