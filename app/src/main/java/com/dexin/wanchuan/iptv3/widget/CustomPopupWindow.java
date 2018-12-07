package com.dexin.wanchuan.iptv3.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

public class CustomPopupWindow {
    private PopupWindow mPopupWindow;
    private View contentview;
    private Context mContext;
    private PopupWindow.OnDismissListener mOnDismissListeners;
    private OnDismissListener mOnDismissListener;

    public CustomPopupWindow(Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(builder.contentviewid, null);
        mPopupWindow = new PopupWindow(contentview, builder.width, builder.height, builder.fouse);
        //需要跟 setBackGroundDrawable 结合
        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        //        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        mPopupWindow.setAnimationStyle(builder.animstyle);

    }

    public void setBackgroundDrawable(Drawable drawable) {
        mPopupWindow.setBackgroundDrawable(drawable);
    }

    public boolean isShowing() {
        boolean showing = mPopupWindow.isShowing();
        return showing;
    }

    public void setOnDismissListener(OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }


    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null) {
            try {
                mPopupWindow.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSoftInputMode(int mode) {
        mPopupWindow.setSoftInputMode(mode);
    }

    /**
     * 根据id获取view
     *
     * @param viewid
     * @return
     */
    public View getItemView(int viewid) {
        if (mPopupWindow != null) {
            return this.contentview.findViewById(viewid);
        }
        return null;
    }

    /**
     * 根据父布局，显示位置
     *
     * @param rootviewid
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public CustomPopupWindow showAtLocation(int rootviewid, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            try {
                View rootview = LayoutInflater.from(mContext).inflate(rootviewid, null);
                mPopupWindow.showAtLocation(rootview, gravity, x, y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * 根据id获取view ，并显示在该view的位置
     *
     * @param targetviewId
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CustomPopupWindow showAsLaction(int targetviewId, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
            View targetview = LayoutInflater.from(mContext).inflate(targetviewId, null);
            mPopupWindow.showAsDropDown(targetview, offx, offy, gravity);
        }
        return this;
    }

    /**
     * 显示在 targetview 的不同位置
     *
     * @param targetview
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CustomPopupWindow showAsLaction(View targetview, int gravity, int offx, int offy) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(targetview, offx, offy, gravity);
        }
        return this;
    }

    /**
     * 根据id设置焦点监听
     *
     * @param viewid
     * @param listener
     */
    public void setOnFocusListener(int viewid, View.OnFocusChangeListener listener) {
        View view = getItemView(viewid);
        view.setOnFocusChangeListener(listener);
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    /**
     * builder 类
     */
    public static class Builder {
        private int contentviewid;
        private int width;
        private int height;
        private boolean fouse;
        private boolean outsidecancel;
        private int animstyle;
        private Context context;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setContentView(int contentviewid) {
            this.contentviewid = contentviewid;
            return this;
        }

        public Builder setwidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setheight(int height) {
            this.height = height;
            return this;
        }

        public Builder setFouse(boolean fouse) {
            this.fouse = fouse;
            return this;
        }

        public Builder setOutSideCancel(boolean outsidecancel) {
            this.outsidecancel = outsidecancel;
            return this;
        }

        public Builder setAnimationStyle(int animstyle) {
            this.animstyle = animstyle;
            return this;
        }

        public CustomPopupWindow builder() {
            return new CustomPopupWindow(this);
        }
    }
}
