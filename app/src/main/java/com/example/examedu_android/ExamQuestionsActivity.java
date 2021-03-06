package com.example.examedu_android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.exam_questions.AnswerAdapter;
import com.example.examedu_android.exam_questions.QuestionNumAdapter;
import com.example.examedu_android.module_list.ModuleListActivity;

import java.util.ArrayList;
import java.util.List;

import Token.TokenManager;
import api.ApiService;
import api.CheckToken;
import cn.pedant.SweetAlert.SweetAlertDialog;
import models.ExamQuestion;
import models.ResponseDTO;
import models.StudentAnswerInput;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamQuestionsActivity extends AppCompatActivity {

    ApiService service;
    Call<ExamQuestion> call;
    TokenManager tokenManager;
    Call<ResponseDTO> callSubmit;

    ExamQuestion examQuestion;
    TextView tvExamType, tvModuleName, tvExamTime, tvQuestionNum, tvQuestion;
    RecyclerView rcvAnswer, rcvQuestionList;
    ImageButton btnPrev, btnNext;
    Button btnFinish;
    CountDownTimer countDownTimer;
    LinearLayout linExamQuestion;
    long timeLeftInMilliseconds;
    int currentQuestionIndex = 0, numberOfColumns;
    int[] questionsNum;
    int studentId, examId;
    List<StudentAnswerInput> answerInputList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_questions);

        initialize();

        service = CheckToken.check(this);
        if (service == null) {
            finish();
        }

        sharedPreferences = getSharedPreferences("answerChecked", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Bundle extras = getIntent().getExtras();
        examId = Integer.parseInt(extras.getString("examId"));
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        studentId = Integer.parseInt(tokenManager.getToken().getAccountId());
        call = service.examQuestionGet(examId, studentId);
        call.enqueue(new Callback<ExamQuestion>() {
            @Override
            public void onResponse(Call<ExamQuestion> call, Response<ExamQuestion> response) {
                if (response != null) {
                    examQuestion = response.body();
                    if (response.isSuccessful()) {
                        setView();
                    } else {
                        linExamQuestion.removeAllViewsInLayout();
                        tvExamType.setText("Something error. Please try again!");
                        tvExamType.setTextColor(Color.parseColor("#ff0000"));
                        linExamQuestion.setGravity(Gravity.CENTER);
                        ((ViewGroup) tvExamType.getParent()).removeView(tvExamType);
                        linExamQuestion.addView(tvExamType);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot request data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExamQuestion> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Cannot request data", Toast.LENGTH_SHORT).show();
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
        rcvQuestionList = findViewById(R.id.rcv_question_list);
        btnPrev = findViewById(R.id.btn_prev);
        btnNext = findViewById(R.id.btn_next);
        btnFinish = findViewById(R.id.btn_finish);
        linExamQuestion = findViewById(R.id.lin_exam_question);
        QuestionNumAdapter.selectedItem = 0;
        QuestionNumAdapter.lastSelectedItem = 0;
    }

    private void setView() {
        tvExamType.setText(examQuestion.isFinalExam() == true ? "Final Exam" : "Progress Test");
        tvModuleName.setText(examQuestion.getModuleCode());

        questionsNum = new int[examQuestion.getQuestionAnswer().size()];
        for (int i = 0; i < questionsNum.length; i++) {
            questionsNum[i] = i + 1;
        }
        numberOfColumns = calculateNoOfColumns(this, 80);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        QuestionNumAdapter questionNumAdapter = new QuestionNumAdapter(this, questionsNum, new QuestionNumAdapter.IOnItemClickListener() {
            @Override
            public void onItemClick(int questionNum) {
                currentQuestionIndex = questionNum - 1;
                updateQuestion();

                if (currentQuestionIndex != examQuestion.getQuestionAnswer().size() - 1) {
                    btnNext.setEnabled(true);
                }
            }
        });
        rcvQuestionList.setLayoutManager(gridLayoutManager);
        rcvQuestionList.setAdapter(questionNumAdapter);

        updateQuestion();

        timeLeftInMilliseconds = examQuestion.getDurationInMinute() * 60000;
        startTimer();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionIndex < examQuestion.getQuestionAnswer().size() - 1) {
                    currentQuestionIndex = currentQuestionIndex + 1;
                    QuestionNumAdapter.lastSelectedItem = QuestionNumAdapter.selectedItem;
                    QuestionNumAdapter.selectedItem = currentQuestionIndex;
                    questionNumAdapter.notifyItemChanged(QuestionNumAdapter.lastSelectedItem);
                    questionNumAdapter.notifyItemChanged(QuestionNumAdapter.selectedItem);

                    //The last question
                    if (currentQuestionIndex == examQuestion.getQuestionAnswer().size() - 1) {
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
                    QuestionNumAdapter.lastSelectedItem = QuestionNumAdapter.selectedItem;
                    QuestionNumAdapter.selectedItem = currentQuestionIndex;
                    questionNumAdapter.notifyItemChanged(QuestionNumAdapter.lastSelectedItem);
                    questionNumAdapter.notifyItemChanged(QuestionNumAdapter.selectedItem);
                    updateQuestion();
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int uncheckedQuestionCount = 0;
                for (int i = 0; i < examQuestion.getQuestionAnswer().size(); i++) {
                    String answer = sharedPreferences.getString(Integer.toString(examQuestion.getQuestionAnswer().get(i).getExamQuestionId()), "");
                    if (answer != "") {
                        if (uncheckedQuestionCount > 0) {
                            uncheckedQuestionCount--;
                        }
                        answerInputList.add(new StudentAnswerInput(
                                answer,
                                studentId,
                                examQuestion.getQuestionAnswer().get(i).getExamQuestionId()
                        ));
                    } else {
                        uncheckedQuestionCount++;
                        continue;
                    }
                }

                //SUBMIT EXAM T???I ????Y (???? c?? ?????y ????? 3 bi???n examId, studentId, answerInputList ch??? c???n g???i l???i ????ng t??n l?? x??i ???????c)

                SweetAlertDialog confirmSubmit = new SweetAlertDialog(ExamQuestionsActivity.this, SweetAlertDialog.WARNING_TYPE);
                confirmSubmit.setTitleText("Are you sure?")
                        .setContentText((uncheckedQuestionCount == 0 ?
                                "" : "You have " + uncheckedQuestionCount + " questions unchecked. ") + "Do you really want to submit the exam?")
                        .setConfirmText("Confirm!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            // b???m confirm s??? g???i Post API ????? submit answer
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                callSubmit = service.submitExam(examId, studentId, answerInputList);
                                callSubmit.enqueue(new Callback<ResponseDTO>() {
                                    @Override
                                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                                        if (!response.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), Integer.toString(response.code()), Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            // n???u submit th??nh c??ng s??? hi???n popup ????? th??ng b??o ??i???m
                                            SweetAlertDialog sDialog = new SweetAlertDialog(ExamQuestionsActivity.this);
                                            sDialog.setTitleText("Successful")
                                                    .setContentText(response.body().getMessage())

                                                    .setConfirmText("Ok")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        // b???m Ok ????? chuy???n trang
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            //D???i nh???ng d??ng d?????i v??o ch??? sau khi submit exam v?? ch???m ??i???m th??nh c??ng
                                                            for (int i = 0; i < examQuestion.getQuestionAnswer().size(); i++) {
                                                                editor.remove("QuestIndex " + i);
                                                                editor.remove(Integer.toString(examQuestion.getQuestionAnswer().get(i).getExamQuestionId()));
                                                            }
                                                            editor.commit();

                                                            Intent intent = new Intent(ExamQuestionsActivity.this, ModuleListActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    });

                                            sDialog.setCancelable(false);
                                            sDialog.show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                                        Toast.makeText(ExamQuestionsActivity.this, "Failed 5000", Toast.LENGTH_SHORT).show();
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
                confirmSubmit.show();


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
                for (int i = 0; i < examQuestion.getQuestionAnswer().size(); i++) {
                    String answer = sharedPreferences.getString(Integer.toString(examQuestion.getQuestionAnswer().get(i).getExamQuestionId()), "");
                    if (answer != null) {
                        answerInputList.add(new StudentAnswerInput(
                                answer,
                                studentId,
                                examQuestion.getQuestionAnswer().get(i).getExamQuestionId()
                        ));
                    } else {
                        continue;
                    }
                }

                //SUBMIT EXAM T???I ????Y (???? c?? ?????y ????? 3 bi???n examId, studentId, answerInputList ch??? c???n g???i l???i ????ng t??n l?? x??i ???????c)

                callSubmit = service.submitExam(examId, studentId, answerInputList);
                callSubmit.enqueue(new Callback<ResponseDTO>() {
                    @Override
                    public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                        if (response.isSuccessful()) {
                            // n???u submit th??nh c??ng s??? hi???n popup ????? th??ng b??o ??i???m
                            SweetAlertDialog sDialog = new SweetAlertDialog(ExamQuestionsActivity.this);
                            sDialog.setTitleText("Successful")
                                    .setContentText(response.body().getMessage())

                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        // b???m Ok ????? chuy???n trang
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                            //D???i nh???ng d??ng d?????i v??o ch??? sau khi submit exam v?? ch???m ??i???m th??nh c??ng
                                            for (int i = 0; i < examQuestion.getQuestionAnswer().size(); i++) {
                                                editor.remove("QuestIndex " + i);
                                                editor.remove(Integer.toString(examQuestion.getQuestionAnswer().get(i).getExamQuestionId()));
                                            }
                                            editor.commit();

                                            Intent intent = new Intent(ExamQuestionsActivity.this, ModuleListActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                            sDialog.setCancelable(false);
                            sDialog.show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDTO> call, Throwable t) {
                        Toast.makeText(ExamQuestionsActivity.this, "Some error happen", Toast.LENGTH_SHORT).show();
                    }
                });


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

        AnswerAdapter answerAdapter = new AnswerAdapter(this, examQuestion.getQuestionAnswer().get(currentQuestionIndex), currentQuestionIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvAnswer.setLayoutManager(linearLayoutManager);
        rcvAnswer.setAdapter(answerAdapter);
    }

    private int calculateNoOfColumns(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    //Show warning when user try to click back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SweetAlertDialog warningBackClick = new SweetAlertDialog(ExamQuestionsActivity.this, SweetAlertDialog.WARNING_TYPE);
            warningBackClick.setTitleText("Warning")
                    .setContentText("You cannot do this while doing the exam. Only click \"Finish\" button when you finish")
                    .setCancelButton("OK", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    });
            warningBackClick.show();
            warningBackClick.getButton(SweetAlertDialog.BUTTON_CONFIRM).setVisibility(View.GONE);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}