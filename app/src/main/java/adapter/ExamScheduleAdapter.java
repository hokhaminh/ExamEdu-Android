package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.ExamQuestionsActivity;
import com.example.examedu_android.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

        boolean isAvailable=true;
        //get current daytime
        Date now = new Date();
//        String examDate = sdfSource.format(dateTime);

        holder.dateTime.setText(finalDate);
        holder.testName.setText((examSchedule.getExamName()));
        holder.description.setText(examSchedule.getDescription());
        holder.moduleName.setText(examSchedule.getModuleCode());
        holder.duration.setText(String.valueOf(examSchedule.getDurationInMinute())+" minutes");

        long diffInMillies = now.getTime() - date.getTime();
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

        if(diff > examSchedule.getDurationInMinute()){
            holder.btnStart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
            holder.btnStart.setText("Ended");
            holder.btnStart.setTextColor(Color.parseColor("#ffffff"));
            holder.btnStart.setEnabled(false);

        }else if(date.after(now)){
            isAvailable=false;
//            holder.btnStart.setEnabled(false);
        }
        boolean finalIsAvailable = isAvailable;
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalIsAvailable) {

                    checkPassword(examSchedule.getPassword(), new DialogSingleButtonListener() {
                        @Override
                        public void onButtonClicked(DialogInterface dialog) {
                            //neu nhap dung password
                            goToExam(examSchedule.getExamId());
                        }
                    });
                }else {
                    Toast.makeText(mContext.getApplicationContext(), "You cannot start the exam yet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void checkPassword(String password, final DialogSingleButtonListener dialogSingleButtonListener) {
//        final boolean[] check = new boolean[1];
        AlertDialog.Builder b = new AlertDialog.Builder(this.mContext);
        final EditText editText = new EditText(mContext);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setSelection(editText.getText().length());

        b.setTitle("Password")
                .setView(editText)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String passwordInput =editText.getText().toString();
                        if( passwordInput.equals(password)){
                            dialogSingleButtonListener.onButtonClicked(dialogInterface);
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        b.setCancelable(true);
                    }
                });

        AlertDialog dialog = b.create();
        dialog.show();
    }


    private void goToExam(String examId) {
        Intent intent = new Intent(mContext, ExamQuestionsActivity.class);
        intent.putExtra("examId",examId);
        mContext.startActivity(intent);
    }
    //test
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
        private EditText passwordtv;
        private Button btnConfirm;
        private Button btnCancel;

        public ExamScheduleViewHolder(@NonNull View itemView) {
            super(itemView);

            hour = itemView.findViewById(R.id.tvHour);
            testName = itemView.findViewById(R.id.tvTestName);
            description = itemView.findViewById(R.id.tvDescription);
            moduleName = itemView.findViewById(R.id.tvModuleName);
            duration = itemView.findViewById(R.id.tvDuration);
            dateTime = itemView.findViewById(R.id.tvDateTime);
            btnStart = itemView.findViewById(R.id.btnStart);
            passwordtv = itemView.findViewById(R.id.passwordTv);
//            btnConfirm = itemView.findViewById(R.id.btnConfirm);
//            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
    public interface DialogSingleButtonListener {
        public abstract void onButtonClicked(DialogInterface dialog);
    }
}
