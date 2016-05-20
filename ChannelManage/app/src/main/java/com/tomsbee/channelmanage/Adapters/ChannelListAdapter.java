package com.tomsbee.channelmanage.Adapters;

import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tomsbee.channelmanage.DragHelper.ItemTouchHelperAdapter;
import com.tomsbee.channelmanage.DragHelper.ItemTouchHelperViewHolder;
import com.tomsbee.channelmanage.DragHelper.OnStartDragListener;
import com.tomsbee.channelmanage.Model.Channel;
import com.tomsbee.channelmanage.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter
{
    public List<Channel> list;
    private final OnStartDragListener mDragStartListener;
    private OnRecyclerViewListener onRecyclerViewListener;
    private boolean editable;


    public static interface OnRecyclerViewListener
    {
        void onItemClick(int position);

        boolean onItemLongClick(int position);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener)
    {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public List<Channel> getList()
    {
        return list;
    }

    public void setList(List<Channel> list)
    {
        this.list = list;
    }


    public ChannelListAdapter(OnStartDragListener mDragStartListener, List<Channel> list, boolean editable)
    {
        this.list = list;
        this.mDragStartListener = mDragStartListener;
        this.editable = editable;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.topic_category_list_item, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);


        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, int position)
    {
        viewHolder.position = position;
        viewHolder.categoryName.setText(list.get(position).getChannelName());
        viewHolder.categoryName.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_MOVE)
                {
                    mDragStartListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
        if (editable)
        {
            viewHolder.categoryEditable.setVisibility(View.VISIBLE);
        }else{
            viewHolder.categoryEditable.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition)
    {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position)
    {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener, View.OnLongClickListener
    {
        public View rootView;
        public TextView categoryName;
        public TextView categoryEditable;
        public int position;

        public ItemViewHolder(View itemView)
        {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.topic_category_item_category_name);
            categoryEditable = (TextView) itemView.findViewById(R.id.topic_category_item_category_editable);
            rootView = itemView.findViewById(R.id.topic_category_root_view);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);

        }

        @Override
        public void onItemSelected()
        {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear()
        {
            itemView.setBackgroundColor(0);
        }


        @Override
        public void onClick(View v)
        {
            if (null != onRecyclerViewListener)
            {
                onRecyclerViewListener.onItemClick(position);
            }
        }

        @Override
        public boolean onLongClick(View v)
        {
            if (null != onRecyclerViewListener)
            {
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }

}
