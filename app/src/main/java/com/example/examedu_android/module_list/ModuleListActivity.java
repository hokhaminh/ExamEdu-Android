package com.example.examedu_android.module_list;

import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examedu_android.BaseActivity;
import com.example.examedu_android.R;

import java.util.ArrayList;
import java.util.List;

import models.Module;

public class ModuleListActivity extends BaseActivity {
    List<Module> moduleList = new ArrayList<Module>();

    RecyclerView recyclerViewModule;
    ModuleAdapter moduleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        super.onCreateDrawer();
        recyclerViewModule=findViewById(R.id.recycleViewModule);
        setRecycleView();

    }
    private void setRecycleView(){
        moduleList.add(new Module(1,"SWD391", "Software architecture", "nguyendinhvinh@gmail.com"));
        moduleList.add(new Module(1,"SWD391", "Software architecture", "lanltt@gmail.com"));
        moduleList.add(new Module(1,"SWD391", "Software architecture", "vohongkhanh@gmail.com"));

        moduleAdapter= new ModuleAdapter(this,moduleList);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerViewModule.setLayoutManager(layoutManager);
        recyclerViewModule.setItemAnimator(new DefaultItemAnimator());
        recyclerViewModule.setAdapter(moduleAdapter);
    }


}