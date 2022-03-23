package com.example.examedu_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    TextView tvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnLogin = findViewById(R.id.btn_login);
        tvLoading = findViewById(R.id.tv_loading_login);
        tvLoading.setVisibility(View.GONE);

        service = RetrofitBuilder.createService(ApiService.class);
        //cách lấy token ra
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (tokenManager.getToken().getAccessToken() != null) {
            startActivity(new Intent(MainActivity.this, ExamScheduleActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvLoading.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                login();
            }
        });

    }

    private void login() {
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextNumberPassword);
        String strEmail = email.getText().toString();
        String strPassword = password.getText().toString();

        LoginBody body = new LoginBody(strEmail, strPassword);


        call = service.login(body);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    tvLoading.setVisibility(View.GONE);
                    tokenManager.saveToken(response.body());

//                    Toast.makeText(MainActivity.this,"dung roi",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ExamScheduleActivity.class));
                    finish();

                }else{
                    tvLoading.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                    btnLogin.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Some error happen", Toast.LENGTH_SHORT).show();
            }
        });

    }


}