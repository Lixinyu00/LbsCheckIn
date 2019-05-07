package com.lxy.lbscheckin.ui.wifiCheckIn;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

import com.lxy.lbscheckin.common.services.WifiCheckServices;
import com.lxy.lbscheckin.data.model.CheckIn;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by LXY on 2018/5/29.
 */

public class CheckInPresenter implements CheckInContract.Presenter {

    public CheckInContract.View view;

    private List<ScanResult> results;
    private String name;
    private String command;
    private String checkInf;

    private WifiCheckServices.WifiCheckBinder wificheckbinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            wificheckbinder = (WifiCheckServices.WifiCheckBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public CheckInPresenter(CheckInContract.View view) {
        this.view = view;
        view.setPresenter(this);
        refresh();
        setData();
    }

    private void setData() {
        Intent intent = view.getMyIntent();
        name = intent.getStringExtra("name");
        command = intent.getStringExtra("command");
        checkInf = intent.getStringExtra("checkInf");
    }

    @Override
    public void refresh() {
        WifiManager wifiManager = (WifiManager) view.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        results = wifiManager.getScanResults();
        view.setList(results);
    }

    @Override
    public void checkIn(final int pos, final int type) {
        final Intent bindItent = new Intent(view.getContext(), WifiCheckServices.class);
        bindItent.putExtra("BSSID", results.get(pos).BSSID);
        bindItent.putExtra("name", name);
        bindItent.putExtra("command", command);
        view.getContext().bindService(bindItent, connection, BIND_AUTO_CREATE);
        if (results.get(pos).BSSID.equals(checkInf)) {
            CheckIn checkIn = new CheckIn();
            checkIn.setUser(name);
            checkIn.setType(type);
            checkIn.setCheckInf(results.get(pos).BSSID);
            checkIn.setCommand(command);
            checkIn.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {

                    if (e == null) {
                        if (type == 0) {
                            Toast.makeText(view.getContext(), "签退成功！", Toast.LENGTH_SHORT).show();
                            view.getContext().unbindService(connection);
                            view.getContext().stopService(bindItent);
                            view.finishActivity();
                        }
                        if (type == 1) {
                            wificheckbinder.ScanCheck(view.getContext());
                            Toast.makeText(view.getContext(), "签到成功！", Toast.LENGTH_SHORT).show();
                            view.changeChecked();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "服务器开小差了！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(view.getContext(), "wifi选择不正确，签到/签退失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


}
