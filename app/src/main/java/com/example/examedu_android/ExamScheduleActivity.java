package com.example.examedu_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);
        super.onCreateDrawer();

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

                rcvExam = findViewById(R.id.rcv_exam);

                examScheduleAdapter = new ExamScheduleAdapter(ExamScheduleActivity.this,examSchedule.getPayload());

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExamScheduleActivity.this);
                rcvExam.setLayoutManager(linearLayoutManager);
                rcvExam.setAdapter(examScheduleAdapter);

            }

            @Override
            public void onFailure(Call<ExamSchedule> call, Throwable t) {
                Toast.makeText(ExamScheduleActivity.this,"sais roi",Toast.LENGTH_SHORT).show();
            }
        });


    }
}