package com.example.examedu_android.exam_questions;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.R;

import java.util.List;

import models.Answer;

public class AnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Answer> answerList;
    private int currentQuestionIndex;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int selectedPosition;

    public AnswerAdapter(Context context, List<Answer> answerList, int currentQuestionIndex) {
        this.context = context;
        this.answerList = answerList;
        this.currentQuestionIndex = currentQuestionIndex;
    }

    @Override
    public int getItemViewType(int position) {
        if (answerList.size() == 1) {
            return R.layout.answer_input;
        } else {
            return R.layout.mcq_answer_card;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPreferences = context.getSharedPreferences("answerChecked", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        try {
            selectedPosition = sharedPreferences.getInt(Integer.toString(currentQuestionIndex), -1);
        } catch (Exception e) {
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == R.layout.answer_input) {
            viewHolder = new InputAnswerViewHolder(view);
        } else {
            viewHolder = new MCQAnswerViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Answer answer = answerList.get(position);
        if (answer == null) {
            return;
        }

        if (holder.getItemViewType() == R.layout.mcq_answer_card) {
            MCQAnswerViewHolder mcqAnswerViewHolder = (MCQAnswerViewHolder) holder;
            mcqAnswerViewHolder.tvAnswerContent.setText(answer.getAnswerContent());

            mcqAnswerViewHolder.rdBtnAnswer.setChecked(position == selectedPosition);

            mcqAnswerViewHolder.rdBtnAnswer.setTag(position);
            mcqAnswerViewHolder.rdBtnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = Integer.parseInt(view.getTag().toString());
                    editor.putInt(Integer.toString(currentQuestionIndex), selectedPosition);
                    editor.commit();
                    notifyDataSetChanged();
                }
            });

            mcqAnswerViewHolder.lnAnswer.setTag(position);
            mcqAnswerViewHolder.lnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = Integer.parseInt(view.getTag().toString());
                    editor.putInt(Integer.toString(currentQuestionIndex), selectedPosition);
                    editor.commit();
                    notifyDataSetChanged();
                }
            });
        } else {
            InputAnswerViewHolder inputAnswerViewHolder = (InputAnswerViewHolder) holder;

            try {
                inputAnswerViewHolder.edtAnswer.setText(sharedPreferences.getString(Integer.toString(currentQuestionIndex), ""));
            } catch (Exception ex) {
            }

            inputAnswerViewHolder.edtAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editor.putString(Integer.toString(currentQuestionIndex), inputAnswerViewHolder.edtAnswer.getText().toString());
                    editor.commit();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    public class MCQAnswerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAnswerContent;
        private RadioButton rdBtnAnswer;
        private LinearLayout lnAnswer;

        public MCQAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnswerContent = itemView.findViewById(R.id.tv_answer_content);
            lnAnswer = itemView.findViewById(R.id.ln_answer);
            rdBtnAnswer = itemView.findViewById(R.id.rd_btn_answer);
        }
    }

    public class InputAnswerViewHolder extends RecyclerView.ViewHolder {

        private EditText edtAnswer;

        public InputAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            edtAnswer = itemView.findViewById(R.id.edt_answer_input);
        }
    }

    @Override
    public int getItemCount() {
        if (answerList != null) {
            return answerList.size();
        }
        return 0;
    }
}
