package com.example.examedu_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    TextView examIdTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Bundle extras = getIntent().getExtras();
        String examId = extras.getString("examId");


        examIdTV = findViewById(R.id.examIdTV);

        examIdTV.setText(examId);


    }
}