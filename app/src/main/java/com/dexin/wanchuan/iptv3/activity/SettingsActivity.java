package com.dexin.wanchuan.iptv3.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.application.BaseActivity;
import com.dexin.wanchuan.iptv3.util.IptvSP;
import com.dexin.wanchuan.iptv3.util.IptvUtil;

@SuppressLint("HandlerLeak")
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SettingsActivity";
    private Context context;
    private IptvSP sp;
    private TextView ipView;
    private TextView portView;
    private TextView localeIpView;
    private TextView localeMacView;

    @Override
    protected void initListener() {
        setListener();
    }

    private void setListener() {
        if (IptvUtil.IS_STB) {
            findViewById(R.id.setting_network).setOnClickListener(this);
            findViewById(R.id.setting_network).setVisibility(View.VISIBLE);
            findViewById(R.id.setting_mac).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.setting_server_ip).setOnClickListener(this);
        findViewById(R.id.setting_server_port).setOnClickListener(this);
        findViewById(R.id.setting_app).setOnClickListener(this);
        findViewById(R.id.setting_system).setOnClickListener(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_settings);
        ((TextView) findViewById(R.id.activity_title_name)).setText(getResources().getString(R.string.settings_system));
        context = getApplicationContext();
        sp = new IptvSP(context);
        ipView = (TextView) findViewById(R.id.server_ip_value);
        portView = (TextView) findViewById(R.id.server_port_value);
        localeIpView = (TextView) findViewById(R.id.local_ip_value);
        localeMacView = (TextView) findViewById(R.id.local_mac_value);
        setListener();
    }

    @Override
    protected void initDate() {
        ipView.setText(sp.getServerIP());
        portView.setText(sp.getServerPort() + "");
        localeIpView.setText(IptvUtil.getLocalEthIpAddress());
        localeMacView.setText(IptvUtil.getMacAddress(context));
    }


    @Override
    protected void processCilck(View v) {

    }

    private void editIP() {
        String ip = sp.getServerIP();
        final Dialog dialog = new Dialog(this);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog);
        TextView dialogPositiveButton = window.findViewById(R.id.dialogEnterButton);
        TextView dialogNegativeButton = window.findViewById(R.id.dialogCancelButton);
        TextView dialogTitleTextView = window.findViewById(R.id.dialogTitleTextView);
        final EditText dialogContentEditView = window.findViewById(R.id.dialogContentEditView);
        dialogTitleTextView.setText(R.string.settings_network_ip);
        dialogContentEditView.setVisibility(View.VISIBLE);
        dialogContentEditView.setText(ip);
        dialogContentEditView.setSelection(ip.length());
        dialogPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setServerIp(dialogContentEditView.getText().toString());
                ipView.setText(dialogContentEditView.getText().toString());
                dialog.dismiss();
            }
        });
        dialogNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void editPort() {
        int port = sp.getServerPort();
        final Dialog dialog = new Dialog(this);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.layout_dialog);
        TextView dialogPositiveButton = window.findViewById(R.id.dialogEnterButton);
        TextView dialogNegativeButton = window.findViewById(R.id.dialogCancelButton);
        TextView dialogTitleTextView = window.findViewById(R.id.dialogTitleTextView);
        final EditText dialogContentEditView = window.findViewById(R.id.dialogContentEditView);
        dialogTitleTextView.setText(R.string.settings_network_port);
        dialogContentEditView.setVisibility(View.VISIBLE);
        dialogContentEditView.setText(port + "");
        dialogContentEditView.setSelection((port + "").length());
        dialogPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setServerPort(Integer.valueOf(dialogContentEditView.getText().toString()));
                portView.setText(dialogContentEditView.getText().toString());
                // IptvService.times = 0;
                dialog.dismiss();
            }
        });
        dialogNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_server_ip:
                editIP();
                break;
            case R.id.setting_server_port:
                editPort();
                break;
            case R.id.setting_network:
                openEthernetSettings(this);
                break;
            case R.id.setting_app:
                //                Intent i = new Intent(this, AppActivity.class);
                //                i.putExtra("filter_installer", false);
                //                startActivity(i);
                break;
            case R.id.setting_system:
                openMBoxSettings(this);
                break;
        }
    }

    @Override
    public void refreshUI(Message msg) {

    }

    /**
     * 打开机顶盒配置
     *
     * @param context
     */
    public static void openMBoxSettings(Context context) {
        Intent intent = new Intent();
        try {
            ComponentName name = new ComponentName("com.mbx.settingsmbox", "com.mbx.settingsmbox.SettingsMboxActivity");
            intent.setComponent(name);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(context, R.string.open_setting_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 打开以太网配置
     *
     * @param context
     */
    private void openEthernetSettings(Context context) {
        Intent intent = new Intent();
        try {
            //            intent = new Intent(Settings.ACTION_SETTINGS);
            intent = new Intent("android.settings.ETHERNET_SETTINGS");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                intent = new Intent(Settings.ACTION_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (Exception e1) {
                e1.printStackTrace();
                Toast.makeText(context, R.string.open_setting_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
