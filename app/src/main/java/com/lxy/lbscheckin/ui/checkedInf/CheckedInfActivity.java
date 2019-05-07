package com.lxy.lbscheckin.ui.checkedInf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.lxy.lbscheckin.R;
import com.lxy.lbscheckin.data.model.CheckIn;
import com.lxy.lbscheckin.data.model.CheckInf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LXY on 2018/5/31.
 */

public class CheckedInfActivity extends Activity implements CheckedInfContract.View, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private CheckedInfContract.Presenter presenter;

    private LinearLayout ll_checked;
    private Spinner sp_checked;
    private Button btn_checked_query;
    private RecyclerView rv_checked;

    private ArrayAdapter<String> adapter;

    private List<CheckInf> list;

    private int pos=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_inf);
        init();
        new CheckedInfPresenter(this);
    }

    @Override
    public void init() {
        sp_checked=findViewById(R.id.sp_checked);
        ll_checked=findViewById(R.id.ll_checked);
        btn_checked_query=findViewById(R.id.btn_checked_query);
        rv_checked=findViewById(R.id.rv_checked);
    }

    @Override
    public void setPresenter(CheckedInfContract.Presenter presenter) {
        this.presenter=presenter;
        presenter.load();
    }

    @Override
    public Intent getMyIntent() {
        return getIntent();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void setVisible(Boolean isVisible) {
        if (isVisible){
            ll_checked.setVisibility(View.VISIBLE);
            btn_checked_query.setVisibility(View.VISIBLE);
        }else {
            ll_checked.setVisibility(View.GONE);
            btn_checked_query.setVisibility(View.GONE);
        }
    }

    @Override
    public void setRecyclerList(List<CheckIn> list) {
        CheckedInfAdapter checkedInfAdapter=new CheckedInfAdapter();
        checkedInfAdapter.setData(list);
        rv_checked.setLayoutManager(new LinearLayoutManager(this));
        rv_checked.setAdapter(checkedInfAdapter);
    }

    @Override
    public void setSpinnerList(List<CheckInf> list) {
        this.list=list;
        List<String> list_command=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            list_command.add(list.get(i).getCommand());
        }
        adapter=new ArrayAdapter<>(this,R.layout.spinner_display_style,R.id.tv_spinner,list_command);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        sp_checked.setAdapter(adapter);
        btn_checked_query.setOnClickListener(this);
        sp_checked.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        pos=i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        btn_checked_query.setText("刷新");
        presenter.query(list.get(pos).getCommand());
    }
}
