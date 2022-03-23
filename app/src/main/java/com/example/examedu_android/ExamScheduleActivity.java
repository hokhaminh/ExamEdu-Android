package com.example.examedu_android;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Token.TokenManager;
import adapter.ExamScheduleAdapter;
import api.ApiService;
import api.CheckToken;
import models.ExamSchedule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamScheduleActivity extends BaseActivity {
    ApiService service;
    private RecyclerView rcvExam;
    private ExamScheduleAdapter examScheduleAdapter;
    TokenManager tokenManager;
    Call<ExamSchedule> call;
    ExamSchedule examSchedule;
    TextView today;
    TextView upcomingexam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);
        super.onCreateDrawer();
        //get current date

        SimpleDateFormat sdfSource = new SimpleDateFormat("EEEE, d MMMM yyyy");
        String date = sdfSource.format(Calendar.getInstance().getTime());

        upcomingexam = findViewById(R.id.upcomingexam);
        today = findViewById(R.id.tvToday);
        today.setText(date);

        service = CheckToken.check(this);
        if(service == null){
            finish();
        }
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));

        String accountID=tokenManager.getToken().getAccountId().toString();
        call = service.getSchedule(accountID,100);

        call.enqueue(new Callback<ExamSchedule>() {
            @Override
            public void onResponse(Call<ExamSchedule> call, Response<ExamSchedule> response) {
                examSchedule = response.body();

                if(response.isSuccessful()){
                    upcomingexam.setText("Your upcoming exam");
                    upcomingexam.setTextColor(Color.parseColor("#3D5AF1"));

                    rcvExam = findViewById(R.id.rcv_exam);

                    examScheduleAdapter = new ExamScheduleAdapter(ExamScheduleActivity.this,examSchedule.getPayload());

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExamScheduleActivity.this);
                    rcvExam.setLayoutManager(linearLayoutManager);
                    rcvExam.setAdapter(examScheduleAdapter);
                }else {
                    upcomingexam.setText("Your Exam Schedule is empty!!!");
                    upcomingexam.setTextColor(Color.parseColor("#ff0000"));

                }
            }

            @Override
            public void onFailure(Call<ExamSchedule> call, Throwable t) {
                Toast.makeText(ExamScheduleActivity.this,"An Error happened",Toast.LENGTH_SHORT).show();
            }
        });


    }
}