package com.lxy.lbscheckin.ui.setCheck;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

import com.baidu.mapapi.map.MapView;
import com.lxy.lbscheckin.ui.BasePresenter;
import com.lxy.lbscheckin.ui.BaseView;

import java.util.List;

/**
 * Created by LXY on 2018/6/1.
 */

public class SetCheckContract {

    interface View extends BaseView<PreStener> {
        Intent getMyIntent();

        Context getContext();

        MapView getMyView();

        void finishActivity();

        void setVisible(int type);

        void setList(List<ScanResult> list);
    }

    interface PreStener extends BasePresenter {
        void setCheck(int pos);
    }
}
