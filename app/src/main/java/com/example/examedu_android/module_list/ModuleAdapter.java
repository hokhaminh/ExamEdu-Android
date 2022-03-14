package com.example.examedu_android.module_list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.R;
import com.example.examedu_android.mark_report;
import com.example.examedu_android.mark_report_folder.MarkReportAdapter;

import java.util.List;

import models.Module;
import models.ModuleResponse;

public class ModuleAdapter extends  RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {
    List<ModuleResponse.Module> moduleList;
    Context Context;
    public ModuleAdapter(Context context, List<ModuleResponse.Module> moduleList) {
        this.moduleList = moduleList;
        notifyDataSetChanged();
        Context=context;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item,
                parent,
                false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        ModuleResponse.Module module = moduleList.get(position);
        if(module==null){
            return;
        }
        holder.moduleCode.setText(module.getModuleCode());
        holder.moduleName.setText(module.getModuleName());
        holder.teacherEmail.setText(module.getTeacherEmail());
        holder.viewMarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Context, mark_report.class);
                intent.putExtra("moduleId",module.getModuleId());
                Context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder{

        private TextView moduleCode;
        private TextView moduleName;
        private TextView teacherEmail;
        private Button viewMarkBtn;


        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);

            moduleCode = itemView.findViewById(R.id.moduleCodeItem);
            moduleName = itemView.findViewById(R.id.moduleNameItem);
            teacherEmail = itemView.findViewById(R.id.teacherMailItem);
            viewMarkBtn = itemView.findViewById(R.id.viewMarkBtn);

        }
    }
}
