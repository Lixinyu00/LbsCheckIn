package com.lxy.lbscheckin.data.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by LXY on 2018/5/31.
 */

public class MyUser extends BmobUser{
    private Integer userType;
    private String name;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
