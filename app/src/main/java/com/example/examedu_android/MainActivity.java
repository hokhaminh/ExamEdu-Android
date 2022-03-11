package com.example.examedu_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import Token.TokenManager;
import api.ApiService;
import api.RetrofitBuilder;
import models.AccessToken;
import models.LoginBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ApiService service;
    TokenManager tokenManager;

    Call<AccessToken> call;
    EditText email;
    EditText password;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLogin = findViewById(R.id.btn_login);

        service = RetrofitBuilder.createService(ApiService.class);
        //cách lấy token ra
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(MainActivity.this, PostActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();
            }
        });

    }

    private void login() {
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextNumberPassword);
        String strEmail = email.getText().toString();
        String strPassword= password.getText().toString();

        LoginBody body = new LoginBody(strEmail,strPassword);


        call = service.login(body);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {

                    tokenManager.saveToken(response.body());

                    Toast.makeText(MainActivity.this,"dung roi",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, mark_report.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Toast.makeText(MainActivity.this,"sai roi",Toast.LENGTH_SHORT).show();
            }
        });

    }


}