package com.dexin.wanchuan.iptv3.activity.hotel;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.adapter.SceneryIntroAdapter;
import com.dexin.wanchuan.iptv3.application.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SceneryActivity extends BaseActivity {

    private Context context;
    private List<String> sceneryDataList = new ArrayList<String>();
    private SceneryIntroAdapter sceneryIntroAdapter;

    @Override
    protected void initDate() {
        getSceneryDataFromSer();
        initAdapter();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_scenery);
        context = getApplicationContext();
        ((TextView) findViewById(R.id.activity_title_name)).setText(R.string.check_service);
    }

    private void initAdapter() {
        sceneryIntroAdapter = new SceneryIntroAdapter(context, sceneryDataList);

    }

    private void getSceneryDataFromSer() {
        if (sceneryDataList != null) {
            sceneryDataList.clear();

            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/bg_1080.png");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/BF130D53-30BA-43AF-9B4D-C6AD4FF7A657.png");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/347265A2-EFD4-41E0-AB04-CB5BDF362B2C.png");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/D9FD50F5-1998-4217-8979-56E4689EA899.png");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/7476AC36-155F-46D7-A366-47439EE1038D.png");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/5933E08A-252E-4BC3-8030-3CA8394C3443.jpg");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/05713F36-9775-4CF2-8BFA-B4C0CD0C1F4E.jpg");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/5D284104-FD4E-4224-B859-10FE191B4796.png");
            sceneryDataList.add("192.168.200.199:8030/iptv2/manager/../res/adv/image/469752CD-9484-4580-9A3C-1A0CF7860FC7.png");
        }
    }

    @Override
    protected void processCilck(View v) {

    }

    @Override
    public void refreshUI(Message msg) {

    }
}
