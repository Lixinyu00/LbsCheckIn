package com.lxy.lbscheckin.ui.logIn;

import android.app.Activity;
import android.os.Bundle;

import com.lxy.lbscheckin.R;

public class LogInActivity extends Activity implements LogInContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public void setPresenter(LogInContract.Presenter presenter) {

    }
}
