package com.lxy.lbscheckin.ui.mainPage;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;

import com.lxy.lbscheckin.ui.BasePresenter;
import com.lxy.lbscheckin.ui.BaseView;

/**
 * Created by LXY on 2018/5/31.
 */

public class MainPageContract {
    interface View extends BaseView<Presenter> {
        Intent getMyIntent();
        Context getContext();
        void finishActivity();
        void setText(String name,String check,String inf);
        void setCommand(Boolean hasCommand);
        FragmentManager getMyFragmentManager();
    }
    interface Presenter extends BasePresenter {
        void check(String command);
        void query();
    }
}
