package com.tomsbee.channelmanage.DragHelper;

import android.view.View;

/**
 * Created by Administrator on 2016/1/8.
 */
public interface OnRecyclerItemClickListener
{

    void onItemClick(View view, String data);
    void onItemLongClick(View view, String data);
}
