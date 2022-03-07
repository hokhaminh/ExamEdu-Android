package com.example.examedu_android.mark_report_folder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.MarkReport;

public class MarkReportAdapter extends RecyclerView.Adapter<MarkReportAdapter.MarkReportViewHolder>{

    private Context mContext;
    private List<MarkReport> mListMarkReport;

    public MarkReportAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<MarkReport> list){
        this.mListMarkReport = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MarkReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mark_report_card, parent,false);
        return new MarkReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkReportViewHolder holder, int position) {
        MarkReport markReport = mListMarkReport.get(position);
        if(markReport == null){
            return;
        }
        holder.moduleName.setText(markReport.getModuleName());
        //parse date format
        holder.examDate.setText(markReport.getExamDate().substring(0,10));
        holder.examName.setText(markReport.getExamName());
        holder.comment.setText(markReport.getComment());
        holder.mark.setText(String.valueOf(markReport.getMark()));
        holder.markProgress.setProgress(Math.round(markReport.getMark()*10));
    }

    @Override
    public int getItemCount() {
        if(mListMarkReport != null){
            return mListMarkReport.size();
        }
        return 0;
    }

    public class MarkReportViewHolder extends RecyclerView.ViewHolder{

        private TextView moduleName;
        private TextView examDate;
        private TextView examName;
        private TextView comment;
        private TextView mark;
        private ProgressBar markProgress;

        public MarkReportViewHolder(@NonNull View itemView) {
            super(itemView);

            moduleName = itemView.findViewById(R.id.tv_module_name);
            examDate = itemView.findViewById(R.id.tv_exam_date);
            examName = itemView.findViewById(R.id.tv_exam_name);
            comment = itemView.findViewById(R.id.tv_comment);
            mark = itemView.findViewById(R.id.tv_mark);
            markProgress = itemView.findViewById(R.id.progress_bar);
        }
    }
}
