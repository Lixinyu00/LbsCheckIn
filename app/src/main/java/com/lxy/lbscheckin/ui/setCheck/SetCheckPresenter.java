package com.lxy.lbscheckin.ui.setCheck;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lxy.lbscheckin.R;
import com.lxy.lbscheckin.data.model.CheckInf;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by LXY on 2018/6/1.
 */

public class SetCheckPresenter implements SetCheckContract.PreStener {
    private SetCheckContract.View view;
    private MapView mv_set_check;
    private BaiduMap mBaiduMap;
    private LocationClient client;//定位监听
    private LocationClientOption mOption;//定位属性
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private MyLocationData locData;//定位坐标


    private int type;
    private String name;
    private List<ScanResult> results;


    public SetCheckPresenter(SetCheckContract.View view) {
        this.view = view;
        view.setPresenter(this);
        setData();
        setView();
    }


    private void setData() {
        Intent intent = view.getMyIntent();
        type = intent.getIntExtra("type", 0);
        name = intent.getStringExtra("name");
    }

    private void setView() {
        view.setVisible(type);
        if (type == 0) {
            getLocationClientOption();
        } else if (type == 1) {
            refresh();
        }
    }

    /***
     * 接收定位结果消息，并显示在地图上
     */
    private BDAbstractLocationListener BDAblistener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            LatLng LocationPoint = new LatLng(location.getLatitude(), location.getLongitude());
            //定位方向
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            //骑手定位
            locData = new MyLocationData.Builder()
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, null));
            //目的地图标
            setMarkerOptions(LocationPoint, R.mipmap.arrive_icon);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(LocationPoint, 16));
        }
    };

    /***
     * 定位选项设置
     * @return
     */
    private void getLocationClientOption() {
        mv_set_check = view.getMyView();
        mBaiduMap = mv_set_check.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mOption = new LocationClientOption();
        mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        mOption.setScanSpan(2000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
        mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
        mOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mOption.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mOption.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
        mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        client = new LocationClient(view.getContext());
        client.setLocOption(mOption);
        client.registerLocationListener(BDAblistener);
        client.start();
    }

    private void setMarkerOptions(LatLng ll, int icon) {
        if (ll == null) return;
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
        MarkerOptions ooD = new MarkerOptions().position(ll).icon(bitmap).draggable(true);
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.e("1111", "onMarkerDragEnd: " );
                mCurrentLat = marker.getPosition().latitude;
                mCurrentLon = marker.getPosition().longitude;
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.e("1111", "onMarkerDragStart: " );
            }
        });
        mBaiduMap.addOverlay(ooD);
    }

    public void refresh() {
        WifiManager wifiManager = (WifiManager) view.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        results = wifiManager.getScanResults();
        view.setList(results);
    }

    @Override
    public void setCheck(int pos) {
        final int command = 1000 + (int) (Math.random() * 9000);
        CheckInf checkInf = new CheckInf();
        checkInf.setUser(name);
        checkInf.setType(type);
        if (pos == -1) {
            String latLng=String.valueOf(mCurrentLat)+","+String.valueOf(mCurrentLon);
            checkInf.setCheckInf(latLng);
        } else {
            checkInf.setCheckInf(results.get(pos).BSSID);
        }
        checkInf.setCommand(String.valueOf(command));
        checkInf.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(view.getContext(), "签到设置成功！您的签到口令为： " + command, Toast.LENGTH_LONG).show();
                    view.finishActivity();
                } else {
                    Toast.makeText(view.getContext(), "服务器开小差了！" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

}
