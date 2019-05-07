package com.lxy.lbscheckin.ui.mainPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.lxy.lbscheckin.data.model.CheckInf;
import com.lxy.lbscheckin.ui.checkedInf.CheckedInfActivity;
import com.lxy.lbscheckin.ui.lbsCheckIn.LbsCheckInActivity;
import com.lxy.lbscheckin.ui.setCheck.SetCheckActivity;
import com.lxy.lbscheckin.ui.wifiCheckIn.CheckInActivity;
import com.lxy.lbscheckin.view.ItemsDialog;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by LXY on 2018/5/31.
 */

public class MainPagePresenter implements MainPageContract.Presenter {

    private MainPageContract.View view;

    private String name;
    private int userType;

    public MainPagePresenter(MainPageContract.View view) {
        this.view = view;
        view.setPresenter(this);
        checkUserType();
    }

    private void checkUserType() {
        Intent intent = view.getMyIntent();
        name = intent.getStringExtra("name");
        userType = intent.getIntExtra("userType", 0);
        if (userType == 0) {
            view.setText(name, "开始签到", "个人签到信息");
            view.setCommand(true);
        } else if (userType == 1) {
            view.setText(name, "签到设置", "签到信息");
            view.setCommand(false);
        }

    }

    @Override
    public void check(final String command) {
        if (userType == 0) {
            if (command.equals("")) {
                Toast.makeText(view.getContext(), "请输入口令", Toast.LENGTH_SHORT);
            } else {
                BmobQuery<CheckInf> query = new BmobQuery<CheckInf>();
                query.addWhereEqualTo("command", command);
                query.findObjects(new FindListener<CheckInf>() {
                    @Override
                    public void done(List<CheckInf> list, BmobException e) {
                        if (e == null) {
                            Intent intent;
                            if (list.get(0).getType()==0){
                                intent=new Intent(view.getContext(), LbsCheckInActivity.class);
                            }else {
                                intent=new Intent(view.getContext(),CheckInActivity.class);
                            }
                            intent.putExtra("name",name);
                            intent.putExtra("checkInf",list.get(0).getCheckInf());
                            intent.putExtra("command",command);
                            view.getContext().startActivity(intent);
                        } else {
                            Toast.makeText(view.getContext(), "查询失败！" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else if (userType == 1) {
            ItemsDialog itemsDialog = new ItemsDialog();
            String[] items = {"定位签到", "WIFI签到"};
            itemsDialog.show("请选择签到方式", items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(view.getContext(), SetCheckActivity.class);
                    intent.putExtra("type",which);
                    intent.putExtra("name",name);
                    view.getContext().startActivity(intent);
                }
            },view.getMyFragmentManager());
        }
    }

    @Override
    public void query() {
        Intent intent=new Intent(view.getContext(), CheckedInfActivity.class);
        intent.putExtra("userType",userType);
        intent.putExtra("name",name);
        view.getContext().startActivity(intent);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


}
