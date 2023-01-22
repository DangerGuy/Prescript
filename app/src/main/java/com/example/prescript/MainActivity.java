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
    private static Map<String, String> initializeLogins() {
        Map<String, String> lg = new HashMap<>();

        // place logins, as pairs of usernames and passwords, below
        lg.put("a", "a");

        return lg;
    }

    private static Map<String, String> logins = initializeLogins();

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
                if(logins.containsKey(username.getText().toString())
                        && logins.get(username.getText().toString()).equals(password.getText().toString())) {
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