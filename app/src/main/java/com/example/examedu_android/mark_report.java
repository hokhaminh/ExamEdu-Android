package com.example.examedu_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.examedu_android.mark_report_folder.MarkReportAdapter;

import java.util.ArrayList;
import java.util.List;

import Token.TokenManager;
import api.ApiService;
import api.CheckToken;
import models.MarkReport;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class mark_report  extends BaseActivity {
    ApiService service;
    TokenManager tokenManager;
    Call<List<MarkReport>> call;
    String test;
    List<MarkReport> markList;

    private RecyclerView rcvMarkReport;
    private MarkReportAdapter markReportAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_report);
        super.onCreateDrawer();

        setSupportActionBar(findViewById(R.id.myToolBar));
        getSupportActionBar().setTitle(null);
        Toast.makeText(this, "day la mark report", Toast.LENGTH_SHORT).show();
        //call api
        service = CheckToken.check(this);
        if(service == null){
            finish();
        }
       int moduleId;
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            moduleId= 0;
        } else {
            moduleId= extras.getInt("moduleId");
        }
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        int studentId =Integer.parseInt(tokenManager.getToken().getAccountId());
        call = service.markReportGet(studentId,moduleId);
        call.enqueue(new Callback<List<MarkReport>>() {
            @Override
            public void onResponse(Call<List<MarkReport>> call, Response<List<MarkReport>> response) {
                markList = response.body();
                setRcv();
            }

            @Override
            public void onFailure(Call<List<MarkReport>> call, Throwable t) {

            }
        });
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();


    }

    private void setRcv(){
        //rcv
        rcvMarkReport = findViewById(R.id.rcv_mark_report);
        markReportAdapter = new MarkReportAdapter(this);
        Context test = this;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvMarkReport.setLayoutManager(linearLayoutManager);

        markReportAdapter.setData(markList);
        rcvMarkReport.setAdapter(markReportAdapter);
    }
}