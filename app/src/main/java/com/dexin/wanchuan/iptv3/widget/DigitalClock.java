/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dexin.wanchuan.iptv3.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Like AnalogClock, but digital. Shows seconds.
 * <p>
 * FIXME: implement separate views for hours/minutes/seconds, so proportional
 * fonts don't shake rendering
 */

public class DigitalClock extends TextView {

    static Calendar mCalendar;
    private static long timeAdjust = 0;

    private final static String m12 = "hh:mm aa";
    private final static String m24 = "kk:mm";
    private final static String ms12 = "hh:mm:ss aa";
    private final static String ms24 = "kk:mm:ss";
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mSecondShow = true;
    private boolean mTickerStopped = false;

    String mFormat;

    public static long currentTimeMillis() {
        return System.currentTimeMillis() + timeAdjust;
    }

    public static void adjustTime(Date date) {
        if (date.getTime() > 0)
            timeAdjust = date.getTime() - System.currentTimeMillis();
    }

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    public boolean timeIsAdjusted() {
        return timeAdjust != 0;
    }

    public void showSecond(boolean show) {
        mSecondShow = show;
        setFormat();
    }

    private void initClock(Context context) {
        @SuppressWarnings("unused")
        Resources r = context.getResources();

        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }

        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true,
                mFormatChangeObserver);

        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        /**
         * requests a tick on the next hard-second boundary
         */
        mTicker = new Runnable() {
            public void run() {
                if (mTickerStopped)
                    return;
                mCalendar.setTimeInMillis(System.currentTimeMillis()
                        + timeAdjust);
                setText(DateFormat.format(mFormat, mCalendar));
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    public static String getDateString(String format) {
        mCalendar.setTimeInMillis(System.currentTimeMillis() + timeAdjust);
        return (String) DateFormat.format(format, mCalendar);
    }

    /**
     * Pulls 12/24 mode from system settings
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        String contentDescription = (String) getContentDescription();
        if (contentDescription != null && !contentDescription.isEmpty()) {
            mFormat = contentDescription;
            return;
        }
        if (get24HourMode()) {
            if (mSecondShow)
                mFormat = ms24;
            else
                mFormat = m24;
        } else {
            if (mSecondShow)
                mFormat = ms12;
            else
                mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }
}
