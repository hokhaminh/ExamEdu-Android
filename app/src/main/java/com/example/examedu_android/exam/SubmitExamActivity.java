package com.example.examedu_android.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.examedu_android.R;
import com.example.examedu_android.module_list.ModuleListActivity;

import java.util.ArrayList;
import java.util.List;

import Token.TokenManager;
import api.ApiService;
import api.CheckToken;
import cn.pedant.SweetAlert.SweetAlertDialog;
import models.ModuleResponse;
import models.ResponseDTO;
import models.StudentAnswerInput;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitExamActivity extends AppCompatActivity {
    Button btnSubmit;

    ApiService service;
    TokenManager tokenManager;
    Call<ResponseDTO> call;

    List<StudentAnswerInput> answerInputs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_exam);
        btnSubmit = findViewById(R.id.btn_submit);

        //Văn mẫu phải có trong các Activity
        service = CheckToken.check(this);
        if (service == null) {
            finish();
        }
        //cách lấy accountID ra
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        int studentId = Integer.parseInt(tokenManager.getToken().getAccountId());

        // khi onclick vào submit sẽ có popup để confirm
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SweetAlertDialog confirmSubmit = new SweetAlertDialog(SubmitExamActivity.this, SweetAlertDialog.WARNING_TYPE);
                confirmSubmit.setTitleText("Are you sure?")
                        .setContentText("Do you really want to submit the exam?")
                        .setConfirmText("Confirm!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            // bấm confirm sẽ gọi Post API để submit answer
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                answerInputs.add(new StudentAnswerInput("7", studentId, 73));
                                answerInputs.add(new StudentAnswerInput("46", studentId, 71));
                                answerInputs.add(new StudentAnswerInput("49", studentId, 72));
                                call = service.submitExam(31, studentId, answerInputs);
                                call.enqueue(new Callback<ResponseDTO>() {
                                    @Override
                                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                        if (response.isSuccessful()) {
                                            // nếu submit thành công sẽ hiện popup để thông báo điểm
                                            SweetAlertDialog sDialog = new SweetAlertDialog(SubmitExamActivity.this);
                                            sDialog.setTitleText("Successful")
                                                    .setContentText(response.body().getMessage())

                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        // bấm Ok để chuyển trang
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            Intent intent = new Intent(SubmitExamActivity.this, ModuleListActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });

                                            sDialog.setCancelable(false);
                                            sDialog.show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                        Toast.makeText(SubmitExamActivity.this, "Failed 5000", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        });
//                        Button btn= confirmSubmit.getButton(SweetAlertDialog.BUTTON_CONFIRM);
                confirmSubmit.show();
            }

        });
    }
}