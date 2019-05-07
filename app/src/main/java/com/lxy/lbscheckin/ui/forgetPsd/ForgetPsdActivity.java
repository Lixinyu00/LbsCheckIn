package com.lxy.lbscheckin.ui.forgetPsd;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lxy.lbscheckin.R;
import com.lxy.lbscheckin.data.model.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by LXY on 2018/5/31.
 */

public class ForgetPsdActivity extends Activity implements View.OnClickListener {
    private EditText et_forget_account;
    private Button btn_forget;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        init();
    }

    private void init() {
        et_forget_account = findViewById(R.id.et_forget_account);
        btn_forget = findViewById(R.id.btn_forget);
        btn_forget.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String email = et_forget_account.getText().toString();
        MyUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "失败，请重试！" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
