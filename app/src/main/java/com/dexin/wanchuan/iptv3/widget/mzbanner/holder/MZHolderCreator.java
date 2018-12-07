package com.dexin.wanchuan.iptv3.widget.mzbanner.holder;

/**
 * Created by zhouwei on 17/5/26.
 */

public interface MZHolderCreator<VH extends MZViewHolder> {
    /**
     * 创建ViewHolder
     *
     * @return
     */
    VH createViewHolder();
}
