package com.example.examedu_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import Token.TokenManager;
import api.ApiService;
import api.CheckToken;
import api.RetrofitBuilder;
import models.AccessToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends BaseActivity {
    ApiService service;
    TokenManager tokenManager;
//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle drawerToggle;
    Call<JsonObject> call;
    TextView txt;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        super.onCreateDrawer();                 //Creates the drawer
        txt = findViewById(R.id.txt);

//        //Lấy Token ra
//        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
//
//        //check nếu ko có token sẽ chuyển về trang login
//        if(tokenManager.getToken() == null){
//            startActivity(new Intent(PostActivity.this, MainActivity.class));
//            finish();
//        }
//
//        //Đây là lúc nó sẽ nhận vào token để tự động nhét vào header
//        service = CheckToken.check();


        //Văn mẫu phải có trong các Activity
        service = CheckToken.check(this);
        if(service == null){
            finish();
        }

        //cách lấy accountID ra
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));


        //Gọi API
        call = service.moduleGet();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Toast.makeText(PostActivity.this,"dungsssroi",Toast.LENGTH_SHORT).show();

                    //cách lấy ra
                    txt.setText(tokenManager.getToken().getAccountId().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PostActivity.this,"sai roi",Toast.LENGTH_SHORT).show();
            }
        });


    }



}