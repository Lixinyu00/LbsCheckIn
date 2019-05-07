package com.lxy.lbscheckin.ui.register;

import android.content.Context;
import android.os.Bundle;

import com.lxy.lbscheckin.ui.BasePresenter;
import com.lxy.lbscheckin.ui.BaseView;

/**
 * Created by LXY on 2018/5/30.
 */

public class RegisterContract {

    interface View extends BaseView<RegisterContract.Presenter> {
        Context getContext();
        void finishActivity();
    }

    interface Presenter extends BasePresenter {
        void register(Bundle bundle);
    }
}

