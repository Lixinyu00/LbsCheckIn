package com.lxy.lbscheckin.ui.setCheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.lxy.lbscheckin.R;
import com.lxy.lbscheckin.ui.wifiCheckIn.CheckInAdapter;

import java.util.List;

/**
 * Created by LXY on 2018/5/31.
 */

public class SetCheckActivity extends Activity implements SetCheckContract.View, View.OnClickListener {

    private SetCheckContract.PreStener preStener;

    private SetCheckAdapter checkAdapter;

    private TextView tv_set_check;
    private MapView mv_set_check;
    private RecyclerView rv_set_check;
    private Button btn_set_check;
    private int pos = -1;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_set_check);
        init();
        new SetCheckPresenter(this);
    }

    @Override
    public void init() {
        tv_set_check = findViewById(R.id.tv_set_check);
        mv_set_check = findViewById(R.id.mv_set_check);
        rv_set_check = findViewById(R.id.rv_set_check);
        btn_set_check = findViewById(R.id.btn_set_check);
    }

    @Override
    public void setPresenter(SetCheckContract.PreStener presenter) {
        this.preStener = presenter;
        btn_set_check.setOnClickListener(this);
    }

    @Override
    public Intent getMyIntent() {
        return getIntent();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public MapView getMyView() {
        return mv_set_check;
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void setVisible(int type) {
        this.type = type;
        if (type == 0) {
            mv_set_check.setVisibility(View.VISIBLE);
            rv_set_check.setVisibility(View.GONE);
            tv_set_check.setText("请移动标记选择地点");
        } else {
            mv_set_check.setVisibility(View.GONE);
            rv_set_check.setVisibility(View.VISIBLE);
            tv_set_check.setText("请选择一个Wifi");
        }
    }

    @Override
    public void setList(final List<ScanResult> list) {
        checkAdapter = new SetCheckAdapter();
        checkAdapter.setData(list);
        rv_set_check.setLayoutManager(new LinearLayoutManager(this));
        rv_set_check.setAdapter(checkAdapter);
        rv_set_check.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        checkAdapter.setOnItemClickListener(new CheckInAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                pos = position;
                tv_set_check.setText(list.get(position).SSID);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (type == 0) {
            preStener.setCheck(pos);
        } else {
            if (pos != -1) {
                preStener.setCheck(pos);
            } else {
                Toast.makeText(this, "还没有选择wifi哦，签到失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
