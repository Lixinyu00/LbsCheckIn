package com.lxy.lbscheckin.ui.checkedInf;

import android.content.Intent;
import android.widget.Toast;

import com.lxy.lbscheckin.data.model.CheckIn;
import com.lxy.lbscheckin.data.model.CheckInf;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by LXY on 2018/6/1.
 */

public class CheckedInfPresenter implements CheckedInfContract.Presenter {

    private CheckedInfContract.View view;

    private String name;
    private int userType;

    public CheckedInfPresenter(CheckedInfContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void load() {
        getIntentData();
        if (userType==0){
            view.setVisible(false);
            BmobQuery<CheckIn> query=new BmobQuery<>();
            query.addWhereEqualTo("user",name);
            query.findObjects(new FindListener<CheckIn>() {
                @Override
                public void done(List<CheckIn> list, BmobException e) {
                    if (e==null){
                        view.setRecyclerList(list);
                    }else {
                        Toast.makeText(view.getContext(), "查询失败！" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            view.setVisible(true);
            BmobQuery<CheckInf> query=new BmobQuery<>();
            query.addWhereEqualTo("user",name);
            query.findObjects(new FindListener<CheckInf>() {
                @Override
                public void done(List<CheckInf> list, BmobException e) {
                    if (e==null){
                        view.setSpinnerList(list);
                    }else {
                        Toast.makeText(view.getContext(), "查询失败！" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void query(String command) {
        BmobQuery<CheckIn> query=new BmobQuery<>();
        query.addWhereEqualTo("command",command);
        query.findObjects(new FindListener<CheckIn>() {
            @Override
            public void done(List<CheckIn> list, BmobException e) {
                if (e==null){
                    view.setRecyclerList(list);
                }else {
                    Toast.makeText(view.getContext(), "查询失败！" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getIntentData() {
        Intent intent=view.getMyIntent();
        name=intent.getStringExtra("name");
        userType=intent.getIntExtra("userType",0);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


}
