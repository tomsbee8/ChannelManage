package com.tomsbee.channelmanage.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tomsbee.channelmanage.Model.Channel;
import com.tomsbee.channelmanage.R;

import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 */
public class SpareListAdapter extends RecyclerView.Adapter
{
    /**
     * 点击事件
     */
    public static interface OnBottomItemClickListener
    {
        void onBottomListItemClick(int position);
    }
    public List<Channel> list;
    private OnBottomItemClickListener bottomItemClickListener;

    public SpareListAdapter(List<Channel> list)
    {
        this.list = list;
    }

    public void setList(List<Channel> list)
    {
        this.list = list;
    }

    public void setBottomItemClickListener(OnBottomItemClickListener bottomItemClickListener)
    {
        this.bottomItemClickListener = bottomItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_category_list_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new BottomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        BottomViewHolder viewHolder = (BottomViewHolder) holder;
        viewHolder.position = position;
        viewHolder.categoryName.setText(list.get(position).getChannelName());
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class BottomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public View rootView;
        public TextView categoryName;
        public int position;

        public BottomViewHolder(View itemView)
        {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.topic_category_item_category_name);
            rootView = itemView.findViewById(R.id.topic_category_root_view);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            if (null != bottomItemClickListener)
            {
                bottomItemClickListener.onBottomListItemClick(position);
            }
        }

    }
}
