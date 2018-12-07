package com.dexin.wanchuan.iptv3.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.application.BaseActivity;

import java.io.IOException;
import java.util.Locale;

public class WelcomeActivity extends BaseActivity {

    private MediaPlayer mMediaPlayer;
    private TextView mTv_chinese;
    private TextView mTv_english;

    @Override
    protected void initDate() {
        initMusic();
    }


    @Override
    protected void initListener() {
        mTv_chinese.setOnClickListener(this);
        mTv_english.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_welcome);
        mTv_chinese = findViewById(R.id.tv_chinese);  // 中文
        mTv_chinese.requestFocus();
        mTv_english = findViewById(R.id.tv_english);  // 英文

    }

    @Override
    protected void processCilck(View v) {
        Resources res = null;
        Configuration config = null;
        res = getBaseContext().getResources();
        config = res.getConfiguration();
        if (v.getId() == R.id.tv_chinese) {
            config.locale = Locale.CHINA;

        } else if (v.getId() == R.id.tv_english) {
            config.locale = Locale.ENGLISH;
        }
        Locale.setDefault(config.locale);
        res.updateConfiguration(config, null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void refreshUI(Message msg) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }


    /**
     * 播放音乐
     */
    private void initMusic() {
        mMediaPlayer = new MediaPlayer();
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
