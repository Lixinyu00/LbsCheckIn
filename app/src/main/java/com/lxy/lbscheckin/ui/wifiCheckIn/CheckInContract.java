package com.lxy.lbscheckin.ui.wifiCheckIn;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

import com.lxy.lbscheckin.ui.BasePresenter;
import com.lxy.lbscheckin.ui.BaseView;

import java.util.List;

/**
 * Created by LXY on 2018/5/29.
 */

public class CheckInContract {

    interface View extends BaseView<Presenter> {
        void setList(List<ScanResult> list);
        Context getContext();
        Intent getMyIntent();
        void finishActivity();
        void changeChecked();
    }

    interface Presenter extends BasePresenter {
        void refresh();
        void checkIn(int pos,int type);
    }
}
