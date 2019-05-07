package com.lxy.lbscheckin.common.services;

/**
 * Created by LXY on 2018/5/29.
 */


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.lxy.lbscheckin.R;
import com.lxy.lbscheckin.data.model.CheckIn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class WifiCheckServices extends Service {

    private WifiManager wifiM;
    private WifiCheckBinder wificheckbinder = new WifiCheckBinder();
    private String BSSID;
    private String user;
    private String command;

    private boolean quit = false;
    private int quit_sign = 0;


    //检测服务的Binder
    public class WifiCheckBinder extends Binder {
        //标识符，用来表示是否扫描到指定的wifi
        public boolean hasscanresult = false;

        //扫描签到检测
        public void ScanCheck(final Context context) {

            //运行一个线程
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    while (!quit) {
                        //扫描附近的wifi
                        wifiM = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
                        wifiM.startScan();
                        List<ScanResult> mData = wifiM.getScanResults();
                        //循环查找是否有符合的wifi
                        for (ScanResult a : mData) {
                            if (a.BSSID.equals(BSSID)) {
                                hasscanresult = true;
                                quit_sign = 0;
                                break;
                            }
                        }
                        //判断是否离场
                        if (!hasscanresult) {
                            if (quit_sign == 0) {
                                CheckIn checkIn=new CheckIn();
                                checkIn.setUser(user);
                                checkIn.setType(0);
                                checkIn.setCheckInf(BSSID);
                                checkIn.setCommand(command);
                                checkIn.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e==null){
                                            quit_sign++;
                                            setNotify(context, 0);
                                        }
                                    }
                                });
                            } else {
                                quit = true;
                                setNotify(context, 1);
                                WifiCheckServices.this.onDestroy();
                            }

                        }
                        try {
                            //每次检测完后，标示符重置
                            hasscanresult = false;
                            Thread.sleep(1000);    //每1分钟检查一次，为了方面演示，先改为10秒检测一次
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }.start();


        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Bundle bundle = (Bundle) intent.getExtras();
        BSSID = bundle.getString("BSSID");
        user=bundle.getString("name");
        command=bundle.getString("command");
        return wificheckbinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void setNotify(Context context, int type) {
        //实例化一个通知
        NotificationManager notificationManager = (NotificationManager) context.
                getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        Notification notify;
        if (type == 0) {
            notify = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("签到提示:" + "您的Wifi不见啦！")
                    .setContentTitle("签到提示")
                    .setContentText("无法扫描到指定Wifi,请检查网络,请勿离场!")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setContentIntent(contentIntent).setNumber(1).build();
        } else {
            notify = new Notification.Builder(context)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("签到提示:" + "您的Wifi不见啦！")
                    .setContentTitle("签到提示")
                    .setContentText("无法扫描到指定Wifi,您已签退!")
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                    .setContentIntent(contentIntent).setNumber(1).build();
        }
        notify.flags = Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        notificationManager.notify(1, notify);
    }
}
