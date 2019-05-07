package com.lxy.lbscheckin.ui.lbsCheckIn;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.lxy.lbscheckin.data.model.CheckIn;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by LXY on 2018/5/31.
 */

public class LbsCheckInPresenter implements LbsCheckInContract.Presenter {
    private int DISTANCE = 100;

    private LbsCheckInContract.View view;

    private MapView mv_lbs;

    private BaiduMap mBaiduMap;
    private LatLng mDestinationPoint;//目的地坐标点
    private LocationClient client;//定位监听
    private LocationClientOption mOption;//定位属性
    private MyLocationData locData;//定位坐标
    private InfoWindow mInfoWindow;//地图文字位置提醒
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private int mCurrentDirection = 0;
    private double mDistance = 0;
    private LatLng mCenterPos;
    private float mZoomScale = 0; //比例

    private String name;
    private String command;
    private String checkInf;
    private Double latitude;
    private Double longitude;

    private Boolean isIn = false;

    public LbsCheckInPresenter(LbsCheckInContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void load() {
        setData();
        getLocationClientOption();
    }

    private void setData() {
        String[] location;
        Intent intent = view.getMyIntent();
        name = intent.getStringExtra("name");
        command = intent.getStringExtra("command");
        checkInf = intent.getStringExtra("checkInf");
        location=checkInf.split(",");
        latitude=Double.parseDouble(location[0]);
        longitude=Double.parseDouble(location[1]);
    }

    /***
     * 定位选项设置
     * @return
     */
    @Override
    public void getLocationClientOption() {
        mv_lbs = view.getMyView();
        mBaiduMap = mv_lbs.getMap();
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

    private void setCircleOptions() {
        if (mDestinationPoint == null) return;
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x4057FFF8)
                .center(mDestinationPoint).stroke(new Stroke(1, 0xB6FFFFFF)).radius(DISTANCE);
        mBaiduMap.addOverlay(ooCircle);
    }


    /***
     * 接收定位结果消息，并显示在地图上
     */
    private BDAbstractLocationListener BDAblistener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //定位方向
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            //骑手定位
            locData = new MyLocationData.Builder()
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, null));
            //更改UI
            changeLocation(location);
        }
    };

    /**
     * 处理连续定位的地图UI变化
     */
    private void changeLocation(BDLocation location) {
        LatLng LocationPoint = new LatLng(location.getLatitude(), location.getLongitude());
        //打卡范围
        mDestinationPoint = new LatLng(latitude, longitude);
        setCircleOptions();
        //计算两点距离,单位：米
        mDistance = DistanceUtil.getDistance(mDestinationPoint, LocationPoint);
        if (mDistance <= DISTANCE) {
            isIn = true;
            //显示文字
            setTextOption(LocationPoint, "您已在签到范围内", "#7ED321");
            //目的地图标
            setMarkerOptions(mDestinationPoint, R.mipmap.arrive_icon);
            //按钮颜色
            mBaiduMap.setMyLocationEnabled(true);
        } else {
            isIn = false;
            setTextOption(LocationPoint, "您不在签到范围之内", "#FF6C6C");
            setMarkerOptions(mDestinationPoint, R.mipmap.arrive_icon);
            mBaiduMap.setMyLocationEnabled(true);
        }
        //缩放地图
        setMapZoomScale(LocationPoint);
    }

    /**
     * 添加地图文字
     *
     * @param point
     * @param str
     * @param color 字体颜色
     */
    private void setTextOption(LatLng point, String str, String color) {
        //使用MakerInfoWindow
        if (point == null) return;
        TextView tv = new TextView(view.getContext());
        tv.setBackgroundResource(R.mipmap.map_textbg);
        tv.setPadding(0, 23, 0, 0);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(16);
        tv.setGravity(Gravity.CENTER);
        tv.setText(str);
        tv.setTextColor(Color.parseColor(color));
        mInfoWindow = new InfoWindow(tv, point, 230);
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    /**
     * 设置marker覆盖物
     *
     * @param ll   坐标
     * @param icon 图标
     */
    private void setMarkerOptions(LatLng ll, int icon) {
        if (ll == null) return;
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
        MarkerOptions ooD = new MarkerOptions().position(ll).icon(bitmap);
        mBaiduMap.addOverlay(ooD);
    }

    //改变地图缩放
    private void setMapZoomScale(LatLng ll) {
        if (mDestinationPoint == null) {
            mZoomScale = getZoomScale(ll);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(ll, mZoomScale));//缩放
        } else {
            mZoomScale = getZoomScale(ll);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(mCenterPos, mZoomScale));//缩放
        }
    }

    /**
     * 获取地图的中心点和缩放比例
     *
     * @return float
     */
    private float getZoomScale(LatLng LocationPoint) {
        double maxLong;    //最大经度
        double minLong;    //最小经度
        double maxLat;     //最大纬度
        double minLat;     //最小纬度
        List<Double> longItems = new ArrayList<Double>();    //经度集合
        List<Double> latItems = new ArrayList<Double>();     //纬度集合

        if (null != LocationPoint) {
            longItems.add(LocationPoint.longitude);
            latItems.add(LocationPoint.latitude);
        }
        if (null != mDestinationPoint) {
            longItems.add(mDestinationPoint.longitude);
            latItems.add(mDestinationPoint.latitude);
        }

        maxLong = longItems.get(0);    //最大经度
        minLong = longItems.get(0);    //最小经度
        maxLat = latItems.get(0);     //最大纬度
        minLat = latItems.get(0);     //最小纬度

        for (int i = 0; i < longItems.size(); i++) {
            maxLong = Math.max(maxLong, longItems.get(i));   //获取集合中的最大经度
            minLong = Math.min(minLong, longItems.get(i));   //获取集合中的最小经度
        }

        for (int i = 0; i < latItems.size(); i++) {
            maxLat = Math.max(maxLat, latItems.get(i));   //获取集合中的最大纬度
            minLat = Math.min(minLat, latItems.get(i));   //获取集合中的最小纬度
        }
        double latCenter = (maxLat + minLat) / 2;
        double longCenter = (maxLong + minLong) / 2;
        int jl = (int) getDistance(new LatLng(maxLat, maxLong), new LatLng(minLat, minLong));//缩放比例参数
        mCenterPos = new LatLng(latCenter, longCenter);   //获取中心点经纬度
        int zoomLevel[] = {2500000, 2000000, 1000000, 500000, 200000, 100000,
                50000, 25000, 20000, 10000, 5000, 2000, 1000, 500, 100, 50, 20, 0};
        int i;
        for (i = 0; i < 18; i++) {
            if (zoomLevel[i] < jl) {
                break;
            }
        }
        float zoom = i + 4;
        return zoom;
    }

    /**
     * 缩放比例参数
     *
     * @param var0
     * @param var1
     * @return
     */
    public double getDistance(LatLng var0, LatLng var1) {
        if (var0 != null && var1 != null) {
            Point var2 = CoordUtil.ll2point(var0);
            Point var3 = CoordUtil.ll2point(var1);
            return var2 != null && var3 != null ? CoordUtil.getDistance(var2, var3) : -1.0D;
        } else {
            return -1.0D;
        }
    }

    @Override
    public void setCurrentDirection(int currentDirection) {
        mCurrentDirection = currentDirection;
        locData = new MyLocationData.Builder()
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mCurrentDirection).latitude(mCurrentLat)
                .longitude(mCurrentLon).build();
        mBaiduMap.setMyLocationData(locData);
    }

    @Override
    public void checkIn(final int type) {
        if (isIn) {
            CheckIn checkIn = new CheckIn();
            checkIn.setUser(name);
            checkIn.setType(type);
            checkIn.setCheckInf(checkInf);
            checkIn.setCommand(command);
            checkIn.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        if (type == 0) {
                            Toast.makeText(view.getContext(), "签退成功！", Toast.LENGTH_SHORT).show();
                            view.finishActivity();
                        }
                        if (type == 1) {
                            Toast.makeText(view.getContext(), "签到成功！", Toast.LENGTH_SHORT).show();
                            view.changeText();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "服务器开小差了！" + e, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(view.getContext(), "您还没到达签到范围！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
