package com.example.examedu_android.module_list;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.BaseActivity;
import com.example.examedu_android.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import Token.TokenManager;
import api.ApiService;
import api.CheckToken;
import models.Module;
import models.ModuleResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModuleListActivity extends BaseActivity {
    ApiService service;
    TokenManager tokenManager;

    Call<ModuleResponse> call;
    SharedPreferences prefs;
    List<ModuleResponse.Module> moduleList = new ArrayList<>();

    RecyclerView recyclerViewModule;
    ModuleAdapter moduleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);
        super.onCreateDrawer();
        recyclerViewModule=findViewById(R.id.recycleViewModule);
        //Văn mẫu phải có trong các Activity
        service = CheckToken.check(this);
        if(service == null){
            finish();
        }

        //cách lấy accountID ra
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
        int studentId =Integer.parseInt(tokenManager.getToken().getAccountId());
        call=service.moduleGetByStudentId(studentId,100);
        call.enqueue(new Callback<ModuleResponse>() {
            @Override
            public void onResponse(Call<ModuleResponse> call, Response<ModuleResponse> response) {
                if(response.isSuccessful()){
                    moduleList=response.body().getPayload();
                    setRecycleView();
                }
            }

            @Override
            public void onFailure(Call<ModuleResponse> call, Throwable t) {
                Toast.makeText(ModuleListActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void setRecycleView(){
        moduleAdapter= new ModuleAdapter(this,moduleList);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerViewModule.setLayoutManager(layoutManager);
        recyclerViewModule.setItemAnimator(new DefaultItemAnimator());
        recyclerViewModule.setAdapter(moduleAdapter);
    }


}