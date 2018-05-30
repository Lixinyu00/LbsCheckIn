package com.lxy.lbscheckin.ui.checkIn;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lxy.lbscheckin.R;

import java.util.List;

/**
 * Created by LXY on 2018/5/29.
 */

public class CheckInActivity extends Activity implements CheckInContract.View, View.OnClickListener {

    private int pos = -1;
    private Boolean isChecked = false;

    private RecyclerView recyclerView;
    private TextView tv_wifi_name;
    private Button btn_refresh;
    private Button btn_checkin;

    private CheckInAdapter checkInAdapter;

    private CheckInContract.Presenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        init();
        new CheckInPresenter(this);
    }


    @Override
    public void init() {
        recyclerView = findViewById(R.id.recyclerView);
        tv_wifi_name = findViewById(R.id.tv_wifi_name);
        btn_refresh = findViewById(R.id.btn_refresh);
        btn_checkin = findViewById(R.id.btn_check);
    }

    @Override
    public void setPresenter(CheckInContract.Presenter presenter) {
        this.presenter = presenter;
        presenter.refresh();
        btn_refresh.setOnClickListener(this);
        btn_checkin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh:
                presenter.refresh();
                break;
            case R.id.btn_check:
                check();
                break;
        }
    }

    private void check() {
        if (isChecked){
            presenter.checkIn(pos,0);
        }else {
            if (pos != -1) {
                presenter.checkIn(pos,1);
            } else {
                Toast.makeText(this, "还没有选择wifi哦，签到失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setList(final List<ScanResult> list) {
        checkInAdapter = new CheckInAdapter();
        checkInAdapter.setData(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(checkInAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        checkInAdapter.setOnItemClickListener(new CheckInAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                pos = position;
                tv_wifi_name.setText(list.get(position).SSID);
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void changeChecked() {
        btn_checkin.setText("签退并退出");
        isChecked=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("12", "onStop: " );
    }
}
