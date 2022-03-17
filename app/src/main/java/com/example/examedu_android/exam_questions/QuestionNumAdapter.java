package com.example.examedu_android.exam_questions;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.R;


public class QuestionNumAdapter extends RecyclerView.Adapter<QuestionNumAdapter.QuestionNumViewHolder> {

    private int[] questionsNum;
    private final IOnItemClickListener listener;
    private Context context;
    public static volatile int selectedItem = 0, lastSelectedItem = 0;

    public QuestionNumAdapter(Context context,
                              int[] questionsNum,
                              IOnItemClickListener listener) {
        this.context = context;
        this.questionsNum = questionsNum;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionNumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_icon, parent, false);
        return new QuestionNumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionNumViewHolder holder, int position) {
        int backgroundColor;
        if (position == selectedItem) {
            backgroundColor = R.color.holder_gray;
        } else if (position == lastSelectedItem) {
            backgroundColor = R.color.green;
        } else {
            backgroundColor = R.color.gray;
        }
        int questionNum = questionsNum[position];
        holder.btnQuestionNum.setText(Integer.toString(questionNum));
        holder.btnQuestionNum.getBackground().setColorFilter(ContextCompat.getColor(context, backgroundColor), PorterDuff.Mode.MULTIPLY);
        holder.btnQuestionNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelectedItem = selectedItem;
                selectedItem = holder.getAdapterPosition();
                notifyItemChanged(lastSelectedItem);
                notifyItemChanged(selectedItem);

                listener.onItemClick(questionNum);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (questionsNum != null) {
            return questionsNum.length;
        }
        return 0;
    }

    public class QuestionNumViewHolder extends RecyclerView.ViewHolder {
        private Button btnQuestionNum;

        public QuestionNumViewHolder(@NonNull View itemView) {
            super(itemView);
            btnQuestionNum = itemView.findViewById(R.id.btn_question_num);
        }
    }

    public interface IOnItemClickListener {
        void onItemClick(int questionNum);
    }
}
