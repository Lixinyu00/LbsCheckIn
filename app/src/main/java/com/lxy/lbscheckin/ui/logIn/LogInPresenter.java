package com.lxy.lbscheckin.ui.logIn;


import android.content.Intent;
import android.widget.Toast;

import com.lxy.lbscheckin.data.model.MyUser;
import com.lxy.lbscheckin.ui.forgetPsd.ForgetPsdActivity;
import com.lxy.lbscheckin.ui.mainPage.MainPageActivity;
import com.lxy.lbscheckin.ui.register.RegisterActivity;
import com.lxy.lbscheckin.utils.Md5;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by LXY on 2018/5/30.
 */

public class LogInPresenter implements LogInContract.Presenter {
    private final String TAG = LogInPresenter.this.getClass().getSimpleName();

    private LogInContract.View view;

    public LogInPresenter(LogInContract.View view) {
        this.view = view;
        view.setPresenter(this);
        Bmob.initialize(view.getContext(), "6fc9c3926b56ad155d6687919fb7a5db");
    }

    @Override
    public void forget() {
        Intent intent = new Intent(view.getContext(), ForgetPsdActivity.class);
        view.getContext().startActivity(intent);
    }

    @Override
    public void logIn(String account, String psd, final int type) {
        final String passWord = Md5.getmd5(psd);
        if (account.equals("") || psd.equals("")) {
            Toast.makeText(view.getContext(), "请输入账号密码", Toast.LENGTH_SHORT).show();
        } else {
            MyUser.loginByAccount(account, passWord, new LogInListener<MyUser>() {
                @Override
                public void done(MyUser myUser, BmobException e) {
                    if (e == null) {
                        Intent intent = new Intent(view.getContext(), MainPageActivity.class);
                        intent.putExtra("name", myUser.getName());
                        if (type == 1 && myUser.getUserType() != 1) {
                            Toast.makeText(view.getContext(), "您还不是管理员！", Toast.LENGTH_SHORT).show();
                        } else {
                            intent.putExtra("userType", type);
                            view.getContext().startActivity(intent);
                            view.finishActivity();
                        }
                    } else {
                        if (e.getErrorCode() == 101) {
                            Toast.makeText(view.getContext(), "账号密码错误，请检查！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(), "查询失败" + e, Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            });
        }
    }

    @Override
    public void register() {
        Intent intent = new Intent(view.getContext(), RegisterActivity.class);
        view.getContext().startActivity(intent);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
