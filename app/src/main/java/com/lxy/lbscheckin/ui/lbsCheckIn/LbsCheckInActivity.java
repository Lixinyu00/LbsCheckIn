package com.lxy.lbscheckin.ui.lbsCheckIn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.utils.DistanceUtil;
import com.lxy.lbscheckin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LXY on 2018/5/31.
 */

public class LbsCheckInActivity extends Activity implements LbsCheckInContract.View, SensorEventListener, View.OnClickListener {

    private LbsCheckInContract.Presenter presenter;

    private MapView mv_lbs;
    private Button btn_check_lbs;
    private SensorManager mSensorManager;//方向传感器
    private Double lastX = 0.0;

    private Boolean isChecked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_checkin_lbs);
        init();
        new LbsCheckInPresenter(this);
    }

    @Override
    public void init() {
        mv_lbs = findViewById(R.id.mv_lbs);
        btn_check_lbs = findViewById(R.id.btn_check_lbs);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    }

    @Override
    public void setPresenter(LbsCheckInContract.Presenter presenter) {
        this.presenter = presenter;
        presenter.load();
        btn_check_lbs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (isChecked) {
            presenter.checkIn(0);
        } else {
            presenter.checkIn(1);
        }
    }


    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            presenter.setCurrentDirection((int) x);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
    public void finishActivity() {
        this.finish();
    }

    @Override
    public MapView getMyView() {
        return mv_lbs;
    }

    @Override
    public void changeText() {
        isChecked=true;
        btn_check_lbs.setText("签退并退出");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mv_lbs.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mv_lbs.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mv_lbs.onPause();
        mSensorManager.unregisterListener(this);
    }
}

