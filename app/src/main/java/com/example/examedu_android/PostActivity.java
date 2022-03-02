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

public class PostActivity extends AppCompatActivity  {
    ApiService service;
    TokenManager tokenManager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    Call<JsonObject> call;
    TextView txt;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        txt = findViewById(R.id.txt);
//        setSupportActionBar(findViewById(R.id.myToolBar));
//        getSupportActionBar().setTitle(null);

//        drawerLayout = findViewById(R.id.activity_main_drawer);
//        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
//        //Make burger menu appears
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

//        NavigationView navigationView = findViewById(R.id.vertical_navigation);
        ////Set listener for selected items on the vertical navigation bar
//        navigationView.setNavigationItemSelectedListener(this);


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

//    @Override
//    protected void onPostCreate(Bundle saveInstanceState){
//        super.onPostCreate(saveInstanceState);
//        //Sync the toggle state after onRestoreInstanceState has occured
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig){
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        if(drawerToggle.onOptionsItemSelected(item)){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    //Set actions for individual item in the drawer
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        switch(id){
//            case R.id.nav_view_exam_schedule:
//                Toast.makeText(this, "Go to exam schedule", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.nav_mark_report:
//                Toast.makeText(this, "Go to mark report", Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    //Allow using back button to close drawer
//    @Override
//    public void onBackPressed(){
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }else {
//            super.onBackPressed();
//        }
//    }

}