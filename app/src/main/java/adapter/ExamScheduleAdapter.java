package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.R;
import com.example.examedu_android.TestActivity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.ExamSchedule;

public class ExamScheduleAdapter extends RecyclerView.Adapter<ExamScheduleAdapter.ExamScheduleViewHolder> {
    private List<ExamSchedule.Exam> mExamScheduleList;
    private Context mContext;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String dateTime;
    public ExamScheduleAdapter(Context context, List<ExamSchedule.Exam> mExamScheduleList) {
        this.mContext = context;
        this.mExamScheduleList = mExamScheduleList;
    }

    @NonNull
    @Override
    public ExamScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_schedule,parent,false);
        return new ExamScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamScheduleViewHolder holder, int position) {
        ExamSchedule.Exam examSchedule = mExamScheduleList.get(position);
        if(examSchedule == null){
            return;
        }

        dateTime =examSchedule.getExamDay();
        Date date =null;
        SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try {
            date = sdfSource.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdfSource = new SimpleDateFormat("hh:mm");
        String finalHour = sdfSource.format(date);
        holder.hour.setText(finalHour);

        sdfSource = new SimpleDateFormat("dd-MMMM");
        String finalDate = sdfSource.format(date);

        holder.dateTime.setText(finalDate);
        holder.testName.setText((examSchedule.getExamName()));
        holder.description.setText(examSchedule.getDescription());
        holder.moduleName.setText(examSchedule.getModuleCode());
        holder.duration.setText(String.valueOf(examSchedule.getDurationInMinute())+" minutes");
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToExam(examSchedule.getExamId());
            }
        });

    }

    private void onClickGoToExam(String examId) {
        Intent intent = new Intent(mContext, TestActivity.class);
        intent.putExtra("examId",examId);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mExamScheduleList != null){
            return mExamScheduleList.size();
        }
        return 0;
    }

    class ExamScheduleViewHolder extends RecyclerView.ViewHolder{
        private TextView hour;
        private TextView testName;
        private TextView description;
        private TextView moduleName;
        private TextView duration;
        private TextView dateTime;
        private Button btnStart;

        public ExamScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.tvHour);
            testName = itemView.findViewById(R.id.tvTestName);
            description = itemView.findViewById(R.id.tvDescription);
            moduleName = itemView.findViewById(R.id.tvModuleName);
            duration = itemView.findViewById(R.id.tvDuration);
            dateTime = itemView.findViewById(R.id.tvDateTime);
            btnStart = itemView.findViewById(R.id.btnStart);
        }
    }
}
