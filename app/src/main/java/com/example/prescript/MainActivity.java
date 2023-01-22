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
    public static final String EXTRA_TEXT = "com.example.prescript.example.EXTRA_TEXT";

    private static class User {

        public String username, password, displayName;

        public User(String u, String p, String d) {
            username = u;
            password = p;
            displayName = d;
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

    }

    private static Set<User> initializeLogins() {
        Set<User> users = new HashSet<>();

        // place logins, as pairs of usernames and passwords, below
        users.add(new User("a", "a", "Admin"));


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
                    openUI("abc");
                }else
                    Toast.makeText(MainActivity.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void openUI(String name){
        Intent intent = new Intent(this, userInterface.class);
        intent.putExtra(EXTRA_TEXT, name);
        startActivity(intent);
    }

}