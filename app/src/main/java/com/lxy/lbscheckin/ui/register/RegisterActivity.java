package com.lxy.lbscheckin.ui.register;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lxy.lbscheckin.R;

/**
 * Created by LXY on 2018/5/30.
 */

public class RegisterActivity extends Activity implements RegisterContract.View, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RegisterContract.Presenter presenter;

    private TextView register_id;
    private TextView register_psd;
    private TextView register_name;
    private TextView register_email;
    private RadioGroup rg_register_user;
    private Button btn_register;

    private int userType = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        new RegisterPresenter(this);
    }

    @Override
    public void init() {
        register_id=findViewById(R.id.register_id);
        register_psd=findViewById(R.id.register_psd);
        register_name=findViewById(R.id.register_name);
        register_email=findViewById(R.id.register_email);
        rg_register_user=findViewById(R.id.rg_register_user);
        btn_register=findViewById(R.id.btn_register);
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {
        this.presenter=presenter;
        btn_register.setOnClickListener(this);
        rg_register_user.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle=new Bundle();
        bundle.putString("id",register_id.getText().toString());
        bundle.putString("psd",register_psd.getText().toString());
        bundle.putString("name",register_name.getText().toString());
        bundle.putString("email",register_email.getText().toString());
        bundle.putInt("type",userType);
        presenter.register(bundle);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rb_register_manger) {
            userType = 1;
        } else {
            userType = 0;
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
}
