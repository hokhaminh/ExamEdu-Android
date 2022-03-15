package com.example.examedu_android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.exam_questions.AnswerAdapter;

import Token.TokenManager;
import api.ApiService;
import api.CheckToken;
import models.ExamQuestion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamQuestionsActivity extends AppCompatActivity {

    ApiService service;
    Call<ExamQuestion> call;
    TokenManager tokenManager;

    ExamQuestion examQuestion;
    TextView tvExamType, tvModuleName, tvExamTime, tvQuestionNum, tvQuestion;
    RecyclerView rcvAnswer;
    ImageButton btnPrev, btnNext;
    Button btnFinish;
    CountDownTimer countDownTimer;
    long timeLeftInMilliseconds;
    int currentQuestionIndex = 0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_questions);

        initialize();

        service = CheckToken.check(this);
        if (service == null) {
            finish();
        }

        sharedPreferences=getSharedPreferences("answerChecked",MODE_PRIVATE);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        call = service.examQuestionGet(26, Integer.parseInt(tokenManager.getToken().getAccountId()));
        call.enqueue(new Callback<ExamQuestion>() {
            @Override
            public void onResponse(Call<ExamQuestion> call, Response<ExamQuestion> response) {
                if (response != null) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    examQuestion = response.body();
                    setView();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot request data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExamQuestion> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "sai roi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        tvExamType = findViewById(R.id.tv_exam_type);
        tvModuleName = findViewById(R.id.tv_module_name);
        tvExamTime = findViewById(R.id.tv_exam_time);
        tvQuestionNum = findViewById(R.id.tv_question_num);
        tvQuestion = findViewById(R.id.tv_question);
        rcvAnswer = findViewById(R.id.rcv_answer);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnFinish = findViewById(R.id.btn_finish);
    }

    private void setView() {
        tvExamType.setText(examQuestion.isFinalExam() == true ? "Final Exam" : "Progress Test");
        tvModuleName.setText(examQuestion.getModuleCode());

        updateQuestion();

        timeLeftInMilliseconds = examQuestion.getDurationInMinute() * 60000;
        startTimer();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionIndex < examQuestion.getQuestionAnswer().size()) {
                    currentQuestionIndex = currentQuestionIndex + 1;

                    //The last question
                    if (currentQuestionIndex == examQuestion.getQuestionAnswer().size()-1) {
                        btnNext.setEnabled(false);
                        updateQuestion();
                    } else {
                        updateQuestion();
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionIndex > 0) {
                    btnNext.setEnabled(true);
                    currentQuestionIndex = (currentQuestionIndex - 1) % examQuestion.getQuestionAnswer().size();
                    updateQuestion();
                }
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                //tự nộp bài khi hết giờ
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        timeLeftText += " Min";

        tvExamTime.setText(timeLeftText);
    }

    private void updateQuestion() {
        tvQuestionNum.setText("Question " + (currentQuestionIndex + 1));
        tvQuestion.setText(examQuestion.getQuestionAnswer().get(currentQuestionIndex).getQuestionContent());

        AnswerAdapter answerAdapter = new AnswerAdapter(this, examQuestion.getQuestionAnswer().get(currentQuestionIndex).getAnswers(), currentQuestionIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvAnswer.setLayoutManager(linearLayoutManager);
        rcvAnswer.setAdapter(answerAdapter);
    }
}