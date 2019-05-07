package com.lxy.lbscheckin.ui.checkedInf;

import android.content.Context;
import android.content.Intent;

import com.lxy.lbscheckin.data.model.CheckIn;
import com.lxy.lbscheckin.data.model.CheckInf;
import com.lxy.lbscheckin.ui.BasePresenter;
import com.lxy.lbscheckin.ui.BaseView;

import java.util.List;

/**
 * Created by LXY on 2018/6/1.
 */

public class CheckedInfContract {
    interface View extends BaseView<Presenter> {
        Intent getMyIntent();

        Context getContext();

        void finishActivity();

        void setVisible(Boolean isVisible);

        void setRecyclerList(List<CheckIn> list);

        void setSpinnerList(List<CheckInf> list);
    }

    interface Presenter extends BasePresenter {
        void load();
        void query(String command);
    }
}
