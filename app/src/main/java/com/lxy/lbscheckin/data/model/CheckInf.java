package com.lxy.lbscheckin.data.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by LXY on 2018/5/30.
 */

public class CheckInf extends BmobObject {

    private String user;
    private Integer type;
    private Integer command;
    private String checkinf;

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

    public Integer getCommand() {
        return command;
    }

    public void setCommand(Integer command) {
        this.command = command;
    }

    public String getCheckinf() {
        return checkinf;
    }

    public void setCheckinf(String checkinf) {
        this.checkinf = checkinf;
    }
}
