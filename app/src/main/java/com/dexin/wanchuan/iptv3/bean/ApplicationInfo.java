package com.dexin.wanchuan.iptv3.bean;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class ApplicationInfo {
    private boolean isSelected;// 是否被选中状体的标示符

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * The application name.
     */
    public CharSequence title;

    /**
     * A bitmap of the application's text in the bubble.
     */
    public Bitmap titleBitmap;
    /**
     * The intent used to start the application.
     */
    public Intent intent;

    /**
     * A bitmap version of the application icon.
     */
    public Bitmap iconBitmap;
    /**
     * The application icon.
     */
    public Drawable icon;

    /**
     * When set to true, indicates that the icon has been resized.
     */
    public boolean filtered;

    public ComponentName componentName;

    /**
     * Creates the application intent based on a component name and various
     * launch flags.
     *
     * @param className   the class name of the component representing the intent
     * @param launchFlags the launch flags
     */
    public final void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
        componentName = className;
    }

}
