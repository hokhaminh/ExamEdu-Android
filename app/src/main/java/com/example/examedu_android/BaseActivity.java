package com.example.examedu_android;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.examedu_android.exam.SubmitExamActivity;
import com.example.examedu_android.module_list.ModuleListActivity;
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
    NavigationView navigationView;

    private ApiService apiService;
    private TokenManager tokenManager2;
    Call<ResponseDTO> call_logout;
    static protected int selectedItemId;

    protected void onCreateDrawer() {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post);
        setSupportActionBar(findViewById(R.id.myToolBar));
        getSupportActionBar().setTitle(null);

        drawerLayout = findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        {
            //Added to try and fix the unclickable problem
            public void onDrawerOpened(View drawerView)
            {
                findViewById(R.id.nav_mark_report).bringToFront();
                findViewById(R.id.nav_view_exam_schedule).bringToFront();
//                drawerView.bringToFront();
                drawerLayout.requestLayout();
            }
        };
        //Make burger menu appears
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = findViewById(R.id.vertical_navigation);
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
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);
        //recreate navigationView's menu, uncheck all item and set new checked item
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_main_drawer);
        //set checked false to everything
        navigationView.getMenu().findItem(R.id.nav_view_exam_schedule).setChecked(false);
        navigationView.getMenu().findItem(R.id.nav_mark_report).setChecked(false);

        if(selectedItemId == 0)
        {
            selectedItemId = R.id.nav_view_exam_schedule;
        }
        navigationView.setCheckedItem(selectedItemId);

        return true;
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
        selectedItemId = id;
        switch(id){
            case R.id.nav_view_exam_schedule:
                startActivity(new Intent(this, ExamScheduleActivity.class));
                break;
            case R.id.nav_mark_report:
                startActivity(new Intent(this, SubmitExamActivity.class));
                finish();
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
