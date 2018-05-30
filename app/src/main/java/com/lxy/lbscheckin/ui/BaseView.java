package com.lxy.lbscheckin.ui;

/**
 * Created by LXY on 2018/5/29.
 */

public interface BaseView<T> {
    void init();
    void setPresenter(T presenter);
}
