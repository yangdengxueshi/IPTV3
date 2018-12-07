package com.dexin.wanchuan.iptv3.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory2(LayoutInflater.from(this), new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }

            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);

                if (view != null && (view instanceof TextView)) {
                    ((TextView) view).setTypeface(BaseApplication.typeface);
                }
                return view;
            }

        });
        super.onCreate(savedInstanceState);

        initView();
        initListener();
        initDate();
    }

    protected abstract void initDate();

    protected abstract void initListener();

    protected abstract void initView();

    protected abstract void processCilck(View v);

    @Override
    public void onClick(View v) {
        processCilck(v);
    }


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshUI(msg);
        }
    };

    public abstract void refreshUI(Message msg);
}
