package com.dexin.wanchuan.iptv3.widget.live;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by LGB on 2018/12/3 0003.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyMediaPlayer extends SurfaceView implements MediaController.MediaPlayerControl, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnVideoSizeChangedListener {

    private Context mAppContext;
    private MediaPlayer mMediaPlayer;
    private SurfaceHolder surfaceHolder;

    public MyMediaPlayer(@NonNull Context context) {
        super(context);
        initMediaPlayer(context);
    }

    public MyMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initMediaPlayer(context);
    }

    public MyMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMediaPlayer(context);
    }

    public MyMediaPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initMediaPlayer(context);
    }

    public void initMediaPlayer(Context context) {
        mAppContext = context.getApplicationContext();
        mMediaPlayer = new MediaPlayer();
        surfaceHolder = this.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setDataSourceUrl(String string) {
        try {
            resetMediaPlayer();
            mMediaPlayer.setDataSource(mAppContext, Uri.parse(string));
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
    }

    private void destroyMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
            } catch (IllegalStateException e) {
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void resetMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
            } catch (IllegalStateException e) {
            }
            mMediaPlayer.reset();
        } else {
            mMediaPlayer = new MediaPlayer();
        }
    }


    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(surfaceHolder);
        setListener();
        mMediaPlayer.setScreenOnWhilePlaying(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        destroyMediaPlayer();
        //        mSurfaceDestroyedListener.onSurfaceDestroy();
        Log.e("-----", "on surfaceDestroyed------");
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        int mVideoWidth = mMediaPlayer.getVideoWidth();
        int mVideoHeight = mMediaPlayer.getVideoHeight();
        if (mVideoHeight != 0 && mVideoWidth != 0) {
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
            mMediaPlayer.start();
            mMediaPlayerOnPrepareListener.mediaPlayerOnPrepare();
        } else {//直接报错,在onError回调中再次尝试进行播放
            mMediaPlayerOnErrorListener.mediaPlayerOnError();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mMediaPlayerOnErrorListener.mediaPlayerOnError();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {//直播时不需要处理该回调,点播时.需要处理相应的回调播放下一个视频
        mMediaPlayerOnCompletionListener.mediaPlayerOnCompletion();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }

    private SurfaceDestroyedListener mSurfaceDestroyedListener;
    private MediaPlayerOnErrorListener mMediaPlayerOnErrorListener;
    private MediaPlayerOnPrepareListener mMediaPlayerOnPrepareListener;
    private MediaPlayerOnCompletionListener mMediaPlayerOnCompletionListener;

    public interface SurfaceDestroyedListener {
        void onSurfaceDestroy();
    }

    public interface MediaPlayerOnErrorListener {
        void mediaPlayerOnError();
    }

    public interface MediaPlayerOnPrepareListener {
        void mediaPlayerOnPrepare();
    }

    public interface MediaPlayerOnCompletionListener {
        void mediaPlayerOnCompletion();
    }

    public void setSurfaceDestroyedListener(SurfaceDestroyedListener onSurfaceDestroyListener) {
        mSurfaceDestroyedListener = onSurfaceDestroyListener;
    }

    public void setMediaPlayerOnErrorListener(MediaPlayerOnErrorListener mediaPlayerOnErrorListener) {
        mMediaPlayerOnErrorListener = mediaPlayerOnErrorListener;
    }

    public void setMediaPlayerOnPrepareListener(MediaPlayerOnPrepareListener mediaPlayerOnPrepareListener) {
        mMediaPlayerOnPrepareListener = mediaPlayerOnPrepareListener;
    }

    public void setMediaPlayerOnCompletionListener(MediaPlayerOnCompletionListener mediaPlayerOnCompletionListener) {
        mMediaPlayerOnCompletionListener = mediaPlayerOnCompletionListener;
    }

}
