package com.dexin.wanchuan.iptv3.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(this);
        dispose();
        super.onDestroy();
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


    //---------------------------------------------------------Note 统一管理所有的'订阅生命周期'-----------------------------------------------------
    //---------------------------------------------------------↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓-----------------------------------------------------
    private CompositeDisposable mCompositeDisposable;

    public void addDisposable(@NonNull final Disposable disposable) {
        if (mCompositeDisposable == null) mCompositeDisposable = new CompositeDisposable();
        if (!disposable.isDisposed()) mCompositeDisposable.add(disposable);
    }

    private void dispose() {
        if ((mCompositeDisposable != null) && !mCompositeDisposable.isDisposed()) mCompositeDisposable.dispose();
    }
}
