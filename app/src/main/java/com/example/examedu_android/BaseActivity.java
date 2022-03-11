package com.example.examedu_android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import Token.TokenManager;
import api.ApiService;
import api.CheckToken;
import models.ResponseDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ShapeableImageView shut_down_icon;

    private ApiService apiService;
    private TokenManager tokenManager2;
    Call<ResponseDTO> call_logout;

    protected void onCreateDrawer() {
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setSupportActionBar(findViewById(R.id.myToolBar));
        getSupportActionBar().setTitle(null);

        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        //Make burger menu appears
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.vertical_navigation);
        //Set listener for selected items on the vertical navigation bar
        navigationView.setNavigationItemSelectedListener(this);


        apiService = CheckToken.check(this);
        if(apiService == null)
        {
            finish();
        }

        call_logout = apiService.logout();


        //Init components
        shut_down_icon = findViewById(R.id.shut_down_btn);

        shut_down_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    call_logout.enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            tokenManager2 = TokenManager.getInstance(getSharedPreferences("prefs",MODE_PRIVATE));
                            tokenManager2.deleteToken();

                            startActivity(new Intent(BaseActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {

                        }
                    });
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle saveInstanceState){
        super.onPostCreate(saveInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occured
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Set actions for individual item in the drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.nav_view_exam_schedule:
                Toast.makeText(this, "Go to exam schedule", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_mark_report:
                Toast.makeText(this, "Go to mark report", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Allow using back button to close drawer
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

}
