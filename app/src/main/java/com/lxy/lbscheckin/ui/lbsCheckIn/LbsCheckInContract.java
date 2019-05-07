package com.lxy.lbscheckin.ui.lbsCheckIn;

import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.map.MapView;
import com.lxy.lbscheckin.ui.BasePresenter;
import com.lxy.lbscheckin.ui.BaseView;

/**
 * Created by LXY on 2018/5/31.
 */

public class LbsCheckInContract {
    interface View extends BaseView<Presenter> {
        Intent getMyIntent();

        Context getContext();

        void finishActivity();

        MapView getMyView();

        void changeText();
    }

    interface Presenter extends BasePresenter {
        void load();
        void getLocationClientOption();
        void setCurrentDirection(int currentDirection);
        void checkIn(int type);
    }
}
