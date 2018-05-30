package com.lxy.lbscheckin.ui.register;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lxy.lbscheckin.R;

/**
 * Created by LXY on 2018/5/30.
 */

public class RegisterActivity extends Activity implements RegisterContract.View {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }

    @Override
    public void init() {

    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {

    }
}
