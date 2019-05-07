package com.lxy.lbscheckin.ui.logIn;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lxy.lbscheckin.R;

public class LogInActivity extends Activity implements LogInContract.View, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private LogInContract.Presenter presenter;
    private EditText et_login_account;
    private EditText et_login_psd;
    private Button btn_login;
    private TextView tv_login_forget;
    private TextView tv_login_register;
    private RadioGroup rg_login_user;

    private int userType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        new LogInPresenter(this);
    }

    @Override
    public void init() {
        et_login_account = findViewById(R.id.et_login_account);
        et_login_psd = findViewById(R.id.et_login_psd);
        btn_login = findViewById(R.id.btn_login);
        tv_login_forget = findViewById(R.id.tv_login_forget);
        tv_login_register = findViewById(R.id.tv_login_register);
        rg_login_user = findViewById(R.id.rg_login_user);
    }

    @Override
    public void setPresenter(LogInContract.Presenter presenter) {
        this.presenter = presenter;
        btn_login.setOnClickListener(this);
        tv_login_forget.setOnClickListener(this);
        tv_login_register.setOnClickListener(this);
        rg_login_user.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                presenter.logIn(et_login_account.getText().toString()
                        , et_login_psd.getText().toString(),userType);
                break;
            case R.id.tv_login_forget:
                presenter.forget();
                break;
            case R.id.tv_login_register:
                presenter.register();
                break;
        }
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rb_login_manger) {
            userType = 1;
        } else {
            userType = 0;
        }
    }
}
