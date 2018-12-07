package com.dexin.wanchuan.iptv3.activity.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.adapter.LiveProgramListAdapter;
import com.dexin.wanchuan.iptv3.application.BaseApplication;
import com.dexin.wanchuan.iptv3.bean.LiveTV;
import com.dexin.wanchuan.iptv3.bean.Settings;
import com.dexin.wanchuan.iptv3.util.HttpUtil;
import com.dexin.wanchuan.iptv3.util.IptvSP;
import com.dexin.wanchuan.iptv3.util.IptvUtil;
import com.dexin.wanchuan.iptv3.util.WebTag;
import com.dexin.wanchuan.iptv3.widget.live.MyMediaPlayer;
import com.dexin.wanchuan.iptv3.widget.live.ijkplayer.IjkVideoView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LivePlayerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnKeyListener {

    private Timer timer;
    private TimerTask timerTask;
    private Timer updateListTimer;
    private TimerTask updateListTask;

    private IptvSP sp;
    private Context context;
    private int packageId = 0;
    private int retryTimes = 0;
    private String playUrl = "";
    private boolean userOperateBusy;
    private int currentPosition = 0;
    private ListView programsListView;
    private MyMediaPlayer myMediaplayer;
    private IjkVideoView myIjkVideoPlayer;
    private LiveProgramListAdapter programsAdapter;
    private List<LiveTV> programsList = new ArrayList<LiveTV>();
    private boolean useIjkPlayerFlag = true;//是否用ijkplayer播放

    private final int HANDLER_NOTIFY_PROGRAM_LIST = 0x0001;
    private final int HANDLER_NOTIFY_PROGRAM_LIST_AND_PLAY = 0x0002;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_NOTIFY_PROGRAM_LIST:
                    programsAdapter.notifyDataSetChanged();
                    programsListView.setSelection(currentPosition);
                    break;
                case HANDLER_NOTIFY_PROGRAM_LIST_AND_PLAY:
                    programsAdapter.notifyDataSetChanged();
                    programsListView.setSelection(currentPosition);
                    playTV(currentPosition);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        if (useIjkPlayerFlag) {
            setContentView(R.layout.activity_live_player_ijk);
            myIjkVideoPlayer = findViewById(R.id.my_ijkplayer_view);
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } else {
            setContentView(R.layout.activity_live_player);
            myMediaplayer = findViewById(R.id.my_mediaplayer_view);
        }
        context = getApplicationContext();
        sp = new IptvSP(context);
        programsListView = findViewById(R.id.programs_list_view);
        programsAdapter = new LiveProgramListAdapter(context, programsList);
        programsListView.setAdapter(programsAdapter);
    }

    private void initData() {
        String url = sp.getUrl(WebTag.TAG_LIVE_TV) + "?packageId=" + packageId + "&mac=" + IptvUtil.getMacAddress(context);
        //        String url = "http://192.168.200.199:8030/iptv2/GetLiveTV?mac=9A:33:ED:3A:94:37&packageId=0";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    String string = response.body().string();
                    Gson gson = new Gson();
                    List<LiveTV> list = gson.fromJson(string, new TypeToken<List<LiveTV>>() {
                    }.getType());
                    if (list != null && list.size() > 0) {
                        if (programsList != null && programsList.size() != list.size()) {
                            int size = programsList.size();
                            programsList.clear();
                            programsList.addAll(list);
                            if (size == 0) {//首次进入,更新列表并进行播放
                                handler.sendEmptyMessage(HANDLER_NOTIFY_PROGRAM_LIST_AND_PLAY);
                            } else {//更新列表
                                handler.sendEmptyMessage(HANDLER_NOTIFY_PROGRAM_LIST);
                            }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void playTV(int index) {
        if (index >= programsList.size())
            return;
        playUrl = buildUrl(programsList.get(index));
        //        playUrl = programsList.get(index).getHttpUrl();
        if (playUrl != null) {
            startPlay(playUrl);
        }
    }

    private void startPlay(String playUrl) {
        try {
            if (useIjkPlayerFlag) {
                myIjkVideoPlayer.stopPlayback();
                myIjkVideoPlayer.setVideoPath(playUrl);
                myIjkVideoPlayer.start();
            } else {
                myMediaplayer.setDataSourceUrl(playUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildUrl(LiveTV program) {
        String url = program.getUrl();
        try {
            if (BaseApplication.getInstance().getSettings().getProtocol().equalsIgnoreCase(Settings.PROTOCOL_RTSP)) {
                url = program.getUrl();
                if (url.startsWith(Settings.PROTOCOL_UDP)) {
                    url = sp.getLiveRtspUrl(program.getId());
                }
            } else if (BaseApplication.getInstance().getSettings().getProtocol().equalsIgnoreCase(Settings.PROTOCOL_HTTP)) {
                url = program.getHttpUrl();
            } else if (BaseApplication.getInstance().getSettings().getProtocol().equalsIgnoreCase(Settings.PROTOCOL_HLS)) {
                url = program.getHlsUrl();
            } else if (BaseApplication.getInstance().getSettings().getProtocol().equalsIgnoreCase(Settings.PROTOCOL_MSCORE)) {
                url = program.getUrl();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private void setListener() {
        programsListView.setOnItemSelectedListener(this);
        programsListView.setOnItemClickListener(this);
        programsListView.setOnKeyListener(this);

        if (useIjkPlayerFlag) {
            myIjkVideoPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer mp) {//若要添加节目换台加载效果时,该处隐藏加载效果
                    retryTimes = 0;
                }
            });
            myIjkVideoPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(IMediaPlayer mp, int what, int extra) {//play error and try again
                    if (retryTimes < 3) {
                        if (playUrl != null)
                            startPlay(playUrl);
                        retryTimes++;
                    } else {
                        Toast.makeText(getApplicationContext(), "播放错误!", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        } else {
            myMediaplayer.setMediaPlayerOnErrorListener(new MyMediaPlayer.MediaPlayerOnErrorListener() {
                @Override
                public void mediaPlayerOnError() {//play error and try again
                    if (retryTimes < 3) {
                        if (playUrl != null)
                            startPlay(playUrl);
                        retryTimes++;
                    } else {
                        Toast.makeText(getApplicationContext(), "播放错误!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            myMediaplayer.setMediaPlayerOnPrepareListener(new MyMediaPlayer.MediaPlayerOnPrepareListener() {
                @Override
                public void mediaPlayerOnPrepare() {
                    retryTimes = 0;
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroyActivity(false);
        //取消定时器
        resetTimer(true);
        resetTimer(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyActivity(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentPosition = position;
        playTV(currentPosition);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    private void startTimer(final boolean updateListFlag) {
        if (updateListFlag) {
            resetTimer(updateListFlag);
            updateListTimer = new Timer();
            updateListTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    });
                }
            };
            updateListTimer.schedule(updateListTask, 60 * 1000);
        } else {
            resetTimer(updateListFlag);
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (programsListView.getVisibility() != View.GONE) {
                                programsListView.setVisibility(View.GONE);
                                resetTimer(updateListFlag);
                            }
                        }
                    });
                }
            };
            timer.schedule(timerTask, 10 * 1000);
        }

    }

    private void resetTimer(boolean updateListFlag) {
        if (updateListFlag) {
            if (updateListTimer != null) {
                updateListTimer.cancel();
                updateListTimer = null;
            }
            if (updateListTask != null) {
                updateListTask.cancel();
                updateListTask = null;
            }
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    openMenu();
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                case KeyEvent.KEYCODE_CHANNEL_UP:
                    if (userOperateBusy)
                        break;
                    setUserOperateBusy();
                    if (currentPosition == programsList.size() - 1) {
                        currentPosition = 0;
                    } else {
                        currentPosition++;
                    }
                    disableViewOrPlay();
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                case KeyEvent.KEYCODE_CHANNEL_DOWN:
                    if (userOperateBusy)
                        break;
                    setUserOperateBusy();
                    if (currentPosition == 0) {
                        currentPosition = programsList.size() - 1;
                    } else {
                        currentPosition--;
                    }
                    disableViewOrPlay();
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    break;
                case KeyEvent.KEYCODE_BACK:
                    if (programsListView.getVisibility() == View.VISIBLE) {
                        programsListView.setVisibility(View.GONE);
                        resetTimer(false);
                    } else {
                        if (useIjkPlayerFlag) {
                            if (myIjkVideoPlayer.isPlaying()) {
                                exitBy2Click();
                            } else {
                                destroyActivity(true);
                            }
                        } else {
                            if (myMediaplayer != null) {
                                if (myMediaplayer.isPlaying()) {
                                    exitBy2Click();
                                } else {
                                    finish();
                                }
                            } else {
                                finish();
                            }
                        }
                    }
                    return true;
                default:
                    //                    if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
                    //                        int number = keyCode - KeyEvent.KEYCODE_0;
                    //                        if (inputNumberTimer != null)
                    //                            inputNumberTimer.cancel();
                    //                        if (inputNumber == null) {
                    //                            inputNumber = number + "";
                    //                            tvNumber.setText(inputNumber + "--");
                    //                            tvNumber.setVisibility(View.VISIBLE);
                    //                            inputNumberTimer = new Timer();
                    //                            inputNumberTimer.schedule(new TimerTask() {
                    //
                    //                                @Override
                    //                                public void run() {
                    //                                    handler.sendEmptyMessage(HANDLE_INPUT_NUMBER);
                    //                                }
                    //                            }, 2000);
                    //                        } else if (inputNumber.length() == 1) {
                    //                            inputNumber = inputNumber + number;
                    //                            tvNumber.setText(inputNumber + "-");
                    //                            tvNumber.setVisibility(View.VISIBLE);
                    //                            inputNumberTimer = new Timer();
                    //                            inputNumberTimer.schedule(new TimerTask() {
                    //
                    //                                @Override
                    //                                public void run() {
                    //                                    handler.sendEmptyMessage(HANDLE_INPUT_NUMBER);
                    //                                }
                    //
                    //                            }, 2000);
                    //                        } else if (inputNumber.length() == 2) {
                    //                            inputNumber = inputNumber + number;
                    //                            handler.sendEmptyMessage(HANDLE_INPUT_NUMBER);
                    //                        }
                    //                    }
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void disableViewOrPlay() {
        if (programsListView.getVisibility() == View.GONE) {
            programsListView.setSelection(currentPosition);
            playTV(currentPosition);
        } else {
            startTimer(false);
        }
    }

    private void destroyActivity(boolean flag) {
        if (myIjkVideoPlayer != null) {
            myIjkVideoPlayer.stopPlayback();
            myIjkVideoPlayer = null;
        }
        if (flag)
            finish();
    }

    private void openMenu() {
        if (programsListView.getVisibility() == View.GONE) {
            programsListView.setVisibility(View.VISIBLE);
        }
        startTimer(false);
    }

    private void setUserOperateBusy() {
        userOperateBusy = true;
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                userOperateBusy = false;
            }
        }, 1000);
    }

    private boolean isExit = false;

    private void exitBy2Click() {
        Timer timer = null;
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, R.string.live_player_click_exit, Toast.LENGTH_LONG).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            if (useIjkPlayerFlag)
                destroyActivity(true);
            else
                finish();
        }
    }
}
