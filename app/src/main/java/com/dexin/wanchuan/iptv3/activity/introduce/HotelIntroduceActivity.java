package com.dexin.wanchuan.iptv3.activity.introduce;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dexin.wanchuan.iptv3.R;
import com.dexin.wanchuan.iptv3.application.BaseActivity;
import com.dexin.wanchuan.iptv3.bean.BGMusicBean;
import com.dexin.wanchuan.iptv3.bean.HotelIntroSinglePageBean;
import com.dexin.wanchuan.iptv3.util.GsonParser;
import com.dexin.wanchuan.iptv3.util.IptvSP;
import com.dexin.wanchuan.iptv3.util.IptvUtil;
import com.dexin.wanchuan.iptv3.util.WebTag;
import com.dexin.wanchuan.iptv3.widget.mzbanner.MZBannerView;
import com.dexin.wanchuan.iptv3.widget.mzbanner.holder.MZViewHolder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotelIntroduceActivity extends BaseActivity {
    private IptvSP mIptvSP;

    @Override
    protected void onPause() {
        pauseBgMusic();
        pausePPT();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        releaseBgMusic();
        super.onDestroy();
    }

    @NonNull
    public static Intent createIntent(Context context) {
        return new Intent(context, HotelIntroduceActivity.class);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_hotel_introduce);
        ButterKnife.bind(this);
        mIptvSP = new IptvSP(getApplicationContext());
        initPPT();
    }

    @Override
    protected void initListener() {
    }

    @Override
    protected void initDate() {
        playPPT();
        playBgMusic();
    }

    @Override
    protected void processCilck(View v) {
    }

    @Override
    public void refreshUI(Message msg) {
    }


    //---------------------------------------------------------------------酒店介绍幻灯片逻辑--------------------------------------------------------------------------------------
    //---------------------------------------------------------------------↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓---------------------------------------------------------------------------------------
    @BindView(R.id.mzbv_hotel_intro)
    MZBannerView<HotelIntroSinglePageBean> mMzbvHotelIntro;

    private void initPPT() {
        mMzbvHotelIntro.setDelayedTime(8000);
        mMzbvHotelIntro.setIndicatorVisible(false);
    }

    private void pausePPT() {
        mMzbvHotelIntro.pause();
    }

    private void playPPT() {
        OkGo.<List<HotelIntroSinglePageBean>>get(mIptvSP.getUrl(WebTag.TAG_INTRO_URL)).tag(this).execute(new AbsCallback<List<HotelIntroSinglePageBean>>() {
            @Override
            public void onSuccess(Response<List<HotelIntroSinglePageBean>> response) {
                final List<HotelIntroSinglePageBean> lHotelIntroSinglePageBeanList = response.body();
                if (lHotelIntroSinglePageBeanList != null) {
                    lHotelIntroSinglePageBeanList.removeAll(Collections.singleton(null));
                    if (!lHotelIntroSinglePageBeanList.isEmpty()) {
                        mMzbvHotelIntro.setPages(lHotelIntroSinglePageBeanList, HotelIntroBannerViewHolder::new);
                        mMzbvHotelIntro.start();
                    }
                }
            }

            @Override
            public List<HotelIntroSinglePageBean> convertResponse(okhttp3.Response response) throws Throwable {
                return GsonParser.toObjectOrList(GsonParser.getJsonStr(response), new TypeToken<List<HotelIntroSinglePageBean>>() {
                }.getType());
            }
        });
    }

    private final class HotelIntroBannerViewHolder implements MZViewHolder<HotelIntroSinglePageBean> {
        private ImageView mIvSiteImage;
        private TextView mTvSiteNameZh;
        private TextView mTvSiteNameEn;
        private TextView mTvSiteOpeningTime;
        private TextView mTvSiteAddress;
        private TextView mTvSiteDetail;

        @Override
        public View createView(Context context) {
            final View itemView = LayoutInflater.from(context).inflate(R.layout.item_hotel_intro, null, false);
            mIvSiteImage = itemView.findViewById(R.id.iv_site_image);
            mTvSiteNameZh = itemView.findViewById(R.id.tv_site_name_zh);
            mTvSiteNameEn = itemView.findViewById(R.id.tv_site_name_en);
            mTvSiteOpeningTime = itemView.findViewById(R.id.tv_site_opening_time);
            mTvSiteAddress = itemView.findViewById(R.id.tv_site_address);
            mTvSiteDetail = itemView.findViewById(R.id.tv_site_detail);
            return itemView;
        }

        @Override
        public void onBind(Context context, int position, @NonNull HotelIntroSinglePageBean hotelIntroSinglePageBean) {
            Glide.with(context).load(mIptvSP.getImagePath(hotelIntroSinglePageBean.getImagePath())).into(mIvSiteImage);
            mTvSiteNameZh.setText(hotelIntroSinglePageBean.getName());
            mTvSiteNameEn.setText("English");// TODO
            mTvSiteOpeningTime.setText("开放时间: 9:20-21:20");// TODO
            mTvSiteAddress.setText("地点: 酒店5楼");// TODO
            mTvSiteDetail.setText(hotelIntroSinglePageBean.getContent());
        }
    }


    //---------------------------------------------------------------------酒店介绍背景音乐逻辑-------------------------------------------------------------------------------------
    //---------------------------------------------------------------------↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-------------------------------------------------------------------------------------
    private MediaPlayer mMediaPlayer;

    private void pauseBgMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    private void releaseBgMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    private void playBgMusic() {
        OkGo.<List<BGMusicBean>>get(mIptvSP.getUrl(WebTag.TAG_BG_MUSIC_LIST) + "?mac=" + IptvUtil.getMacAddress(getApplicationContext())).tag(this).execute(new AbsCallback<List<BGMusicBean>>() {
            @Override
            public void onSuccess(Response<List<BGMusicBean>> response) {
                final List<BGMusicBean> lBGMusicBeanList = response.body();
                if (lBGMusicBeanList != null) {
                    lBGMusicBeanList.removeAll(Collections.singleton(null));
                    if (!lBGMusicBeanList.isEmpty()) playMusicLoop(lBGMusicBeanList);
                }
            }

            @Override
            public List<BGMusicBean> convertResponse(okhttp3.Response response) throws Throwable {
                return GsonParser.toObjectOrList(GsonParser.getJsonStr(response), new TypeToken<List<BGMusicBean>>() {
                }.getType());
            }

            private int mMusicIndex;

            private void playMusicLoop(@NonNull final List<BGMusicBean> bgMusicBeanList) {
                try {
                    if (mMediaPlayer == null) mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(mIptvSP.getUrl(WebTag.TAG_BG_MUSIC_SINGLE) + bgMusicBeanList.get(mMusicIndex % bgMusicBeanList.size()).getUrl());
                    mMediaPlayer.setOnPreparedListener(MediaPlayer::start);
                    mMediaPlayer.setOnCompletionListener(mediaPlayer -> {
                        mMusicIndex++;
                        playMusicLoop(bgMusicBeanList);
                    });
                    mMediaPlayer.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
