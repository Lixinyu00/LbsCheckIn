package com.lxy.lbscheckin.ui.logIn;

import android.content.Context;

import com.lxy.lbscheckin.ui.BasePresenter;
import com.lxy.lbscheckin.ui.BaseView;

/**
 * Created by LXY on 2018/5/29.
 */

public class LogInContract {

    interface View extends BaseView<Presenter>{
        Context getContext();
        void finishActivity();
    }
    interface Presenter extends BasePresenter{
        void forget();
        void logIn(String account,String psd,int type);
        void register();
    }
}
