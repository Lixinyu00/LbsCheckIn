package com.lxy.lbscheckin.ui.mainPage;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lxy.lbscheckin.R;

/**
 * Created by LXY on 2018/5/31.
 */

public class MainPageActivity extends Activity implements MainPageContract.View, View.OnClickListener {

    private MainPageContract.Presenter presenter;

    private TextView tv_main_name;
    private Button btn_main_check;
    private Button btn_main_inf;
    private EditText et_main_command;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        init();
        new MainPagePresenter(this);
    }

    @Override
    public void init() {
        tv_main_name=findViewById(R.id.tv_main_name);
        btn_main_check=findViewById(R.id.btn_main_check);
        btn_main_inf=findViewById(R.id.btn_main_inf);
        et_main_command=findViewById(R.id.et_main_command);
    }

    @Override
    public void setPresenter(MainPageContract.Presenter presenter) {
        this.presenter=presenter;
        btn_main_check.setOnClickListener(this);
        btn_main_inf.setOnClickListener(this);
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
    public void setText(String name,String check,String inf) {
        tv_main_name.setText(name+"，欢迎回来！");
        btn_main_check.setText(check);
        btn_main_inf.setText(inf);
    }

    @Override
    public void setCommand(Boolean hasCommand) {
        if (hasCommand){
            et_main_command.setVisibility(View.VISIBLE);
        }else {
            et_main_command.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public FragmentManager getMyFragmentManager() {
        return getFragmentManager();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_main_check:
                presenter.check(et_main_command.getText().toString());
                break;
            case R.id.btn_main_inf:
                presenter.query();
                break;
        }
    }
}
