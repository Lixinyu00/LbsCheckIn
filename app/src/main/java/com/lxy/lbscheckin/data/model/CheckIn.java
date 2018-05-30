package com.lxy.lbscheckin.data.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by LXY on 2018/5/30.
 */

public class CheckIn extends BmobObject {

    private String user;
    private Integer type;
    private String mac;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
