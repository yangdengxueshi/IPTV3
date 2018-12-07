package com.dexin.wanchuan.iptv3.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.activity.hotel.SceneryActivity;
import com.dexin.wanchuan.iptv3.activity.live.LivePlayerActivity;
import com.dexin.wanchuan.iptv3.adapter.HotelServiceAdapter;
import com.dexin.wanchuan.iptv3.adapter.MainMenuAdapter;
import com.dexin.wanchuan.iptv3.application.BaseActivity;
import com.dexin.wanchuan.iptv3.bean.HotelServiceMenu;
import com.dexin.wanchuan.iptv3.bean.MainMenu;
import com.dexin.wanchuan.iptv3.widget.CommomDialog;
import com.dexin.wanchuan.iptv3.widget.CustomPopupWindow;
import com.dexin.wanchuan.iptv3.widget.tvrecyclerview.TvRecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {


    private TvRecyclerView mRv_main_menu;
    private LinearLayout mLl_main_menu;
    private boolean flag = false;
    private MediaPlayer mMediaPlayer;
    private CustomPopupWindow mPopupWindow;
    private ListView mLv_service_hotel;
    private ArrayList<HotelServiceMenu> mServiceMenus;
    private CommomDialog mCommomDialog;
    private Class<?> cls;

    @Override
    protected void initDate() {
        initAdapter();
        initMusic();
    }

    @Override
    protected void processCilck(View v) {

    }

    @Override
    public void refreshUI(Message msg) {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        mRv_main_menu = findViewById(R.id.rv_main_menu);
        mLl_main_menu = findViewById(R.id.ll_main_menu);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRv_main_menu.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer.start();
        right_show_action();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        mMediaPlayer.release();
    }

    private int index = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (!flag) {
            right_show_action();
            return true;
        }
        if (mPopupWindow != null && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN && index < mServiceMenus.size()) {
                index++;
                mLv_service_hotel.setSelection(index);
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP && index > 0) {
                index--;
                mLv_service_hotel.setSelection(index);
            }
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) { //确定，跳转页面或者弹出对话框
                Log.e("---", "dispatchKeyEvent: ---------" + index);
                showDialogOrStartActivity();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void showDialogOrStartActivity() {
        if (!mServiceMenus.get(index).isSkip()) {//弹出对话框
            String content = "尊敬的先生/女士，是否需要" + mServiceMenus.get(index).getContent() + "?";
            // 确定，发送请求
            mCommomDialog = new CommomDialog(MainActivity.this, R.style.dialog, content, new CommomDialog.OnCloseListener() {
                @Override
                public void onClick(Dialog dialog, boolean confirm) {
                    if (confirm) { // 确定，发送请求
                        String url = mServiceMenus.get(index).getUrl();
                    } else {
                        dialog.dismiss();
                        mCommomDialog = null;
                    }
                }
            });
            mCommomDialog.show();
        } else {//跳转页面
            cls = mServiceMenus.get(index).getCls();
            startActivity(cls);
        }
    }

    /**
     * 跳转页面前先做动画
     *
     * @param cls
     */
    private void startActivity(Class<?> cls) {
        if (mCommomDialog != null && mCommomDialog.isShowing()) {
            mCommomDialog.dismiss();
        }
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        left_leave_action();
    }

    private void initAdapter() {
        final ArrayList<MainMenu> mainMenus = new ArrayList<>();
        addMenu(mainMenus);
        MainMenuAdapter adapter = new MainMenuAdapter(this, mainMenus);
        mRv_main_menu.setAdapter(adapter);
        mRv_main_menu.setSelectPadding(0, 0, 0, 0);
        mRv_main_menu.setOnItemStateListener(new TvRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                Log.e("---", "onItemViewClick: -------------------222");
                if (mainMenus.get(position).isSkip()) {// 跳转到什么界面
                    cls = mainMenus.get(position).getCls();
                    startActivity(cls);
                }
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
                View tv_name = view.findViewById(R.id.tv_menu_name);
                if (gainFocus) {
                    tv_name.setVisibility(View.VISIBLE);
                    if (!mainMenus.get(position).isSkip()) {// 显示弹窗
                        initPopupwindow(view);
                    }
                } else {
                    tv_name.setVisibility(View.INVISIBLE);
                    if (!mainMenus.get(position).isSkip()) {// 关闭弹窗
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                            mPopupWindow = null;
                        }
                    }
                }
            }
        });
        mRv_main_menu.setItemSelected(0);
    }

    /**
     * 显示弹窗
     *
     * @param view
     */
    private void initPopupwindow(View view) {
        mServiceMenus = new ArrayList<>();
        HotelServiceMenu serviceMenu1 = new HotelServiceMenu(0, "洗衣服务", "洗衣服务", null, false, "http://www.baidu.com");
        mServiceMenus.add(serviceMenu1);
        HotelServiceMenu serviceMenu2 = new HotelServiceMenu(1, "叫醒服务", null, WakeUpActivity.class, true, null);
        mServiceMenus.add(serviceMenu2);
        HotelServiceMenu serviceMenu3 = new HotelServiceMenu(2, "借用物品", null, MainActivity.class, true, null);
        mServiceMenus.add(serviceMenu3);
        HotelServiceMenu serviceMenu4 = new HotelServiceMenu(3, "管家服务", "管家服务", null, false, "http://www.baidu.com");
        mServiceMenus.add(serviceMenu4);
        HotelServiceMenu serviceMenu5 = new HotelServiceMenu(4, "服务查询", null, MainActivity.class, true, null);
        mServiceMenus.add(serviceMenu5);
        mPopupWindow = new CustomPopupWindow.Builder()
                .setContext(MainActivity.this)
                .setwidth(view.getWidth())
                .setheight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setContentView(R.layout.popupwindow_main_service_layout)
                .setOutSideCancel(true)
                .builder();
        mPopupWindow.showAsLaction(view, Gravity.TOP, 0, -view.getHeight() - mServiceMenus.size() * 60);
        mLv_service_hotel = (ListView) mPopupWindow.getItemView(R.id.lv_service_hotel);
        HotelServiceAdapter adapter = new HotelServiceAdapter(MainActivity.this, mServiceMenus);
        mLv_service_hotel.setAdapter(adapter);
        mLv_service_hotel.requestFocus();
        mLv_service_hotel.setSelection(index);
    }


    /**
     * 添加菜单
     *
     * @param mainMenus
     */
    private void addMenu(ArrayList<MainMenu> mainMenus) {
        MainMenu live = new MainMenu(R.mipmap.icon_live, getResources().getString(R.string.live), LivePlayerActivity.class, true);  // 直播
        mainMenus.add(live);
        MainMenu vod = new MainMenu(R.mipmap.icon_vod, getResources().getString(R.string.vod), MainActivity.class, true);  // 点播
        mainMenus.add(vod);
        MainMenu scenery = new MainMenu(R.mipmap.icon_scenery, getResources().getString(R.string.scenery), SceneryActivity.class, true);   // 景点
        mainMenus.add(scenery);
        MainMenu dinner = new MainMenu(R.mipmap.icon_dinner, getResources().getString(R.string.dinner), MainActivity.class, true);  // 餐饮
        mainMenus.add(dinner);
        MainMenu service = new MainMenu(R.mipmap.icon_service, getResources().getString(R.string.service), MainActivity.class, false);  // service
        mainMenus.add(service);
        MainMenu hotel = new MainMenu(R.mipmap.icon_hotel, getResources().getString(R.string.hotel), MainActivity.class, true);   // 酒店指南
        mainMenus.add(hotel);
        MainMenu money = new MainMenu(R.mipmap.icon_money, getResources().getString(R.string.money), MainActivity.class, true);   // 消费查询
        mainMenus.add(money);
    }

    /**
     * 左移离开动画
     */
    private void left_leave_action() {
        if (!flag) {
            return;
        }
        int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        ObjectAnimator translationX_R = ObjectAnimator.ofFloat(mLl_main_menu, "translationX", 0, -width);
        translationX_R.setDuration(2000);
        translationX_R.start();
        translationX_R.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLl_main_menu.setVisibility(View.GONE);
                flag = false;
                Intent intent = new Intent(MainActivity.this, cls);
                startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 右移显示动画
     */
    private void right_show_action() {
        if (flag) {
            return;
        }
        int width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        ObjectAnimator translationX_R = ObjectAnimator.ofFloat(mLl_main_menu, "translationX", -width, 0);
        translationX_R.setDuration(2000);
        translationX_R.start();
        mLl_main_menu.setVisibility(View.VISIBLE);
        flag = true;
    }

    /**
     * 播放音乐
     */
    private void initMusic() {
        mMediaPlayer = new MediaPlayer();
        startPlayMusic();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startPlayMusic();
            }
        });
    }

    private void startPlayMusic() {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource("www.baidu.com");
            mMediaPlayer.prepareAsync();//读取网络歌曲时，异步准备，player无法
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                    Log.e("-----------", "音乐播放");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
