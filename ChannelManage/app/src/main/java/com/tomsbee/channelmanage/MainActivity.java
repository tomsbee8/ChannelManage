package com.tomsbee.channelmanage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Button;
import android.widget.TextView;

import com.tomsbee.channelmanage.Adapters.ChannelListAdapter;
import com.tomsbee.channelmanage.Adapters.SpareListAdapter;
import com.tomsbee.channelmanage.DragHelper.OnStartDragListener;
import com.tomsbee.channelmanage.DragHelper.SimpleItemTouchHelperCallback;
import com.tomsbee.channelmanage.Model.Channel;
import com.tomsbee.channelmanage.Util.GridSpacingItemDecoration;
import com.tomsbee.channelmanage.Util.WrappableGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements OnStartDragListener, ChannelListAdapter.OnRecyclerViewListener, SpareListAdapter.OnBottomItemClickListener {

    @Bind(R.id.main_toolbar)
    Toolbar mainToolbar;
    @Bind(R.id.topic_category_manage_save_button)
    Button topicCategoryManageSaveButton;
    @Bind(R.id.topic_category_manage_top_recycler_view)
    RecyclerView topicCategoryManageTopRecyclerView;
    @Bind(R.id.topic_category_manage_middle_text)
    TextView topicCategoryManageMiddleText;
    @Bind(R.id.topic_category_manage_bottom_recycler_view)
    RecyclerView topicCategoryManageBottomRecyclerView;
    private ItemTouchHelper mItemTouchHelper;
    private ChannelListAdapter adapter;
    private SpareListAdapter spareAdapter;
    private boolean editable = false;
    private List<Channel> myChannels;
    private List<Channel> spareChannels;
    private List<Channel> wholeChannels;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0x111:
                    if (mItemTouchHelper == null) {
                        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                        mItemTouchHelper = new ItemTouchHelper(callback);
                        mItemTouchHelper.attachToRecyclerView(topicCategoryManageTopRecyclerView);
                    }
                    if (!adapter.isEditable()) {
                        topicCategoryManageSaveButton.setText("完成");
                        adapter.setEditable(true);
                        adapter.notifyDataSetChanged();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolsBar();
        initViews();
        initData();
    }

    @OnClick(R.id.topic_category_manage_save_button)
    public void saveOnclick() {
        if (adapter.isEditable()) {
            topicCategoryManageSaveButton.setText("编辑");
            editable = false;
            adapter.setEditable(false);
            adapter.notifyDataSetChanged();

        } else {
            topicCategoryManageSaveButton.setText("完成");
            editable = true;
            adapter.setEditable(true);
            adapter.notifyDataSetChanged();
        }
    }

    public void initToolsBar() {
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void initViews() {

        myChannels = new ArrayList<>();
        spareChannels = new ArrayList<>();
        myChannels.add(new Channel(1, "教育"));
        myChannels.add(new Channel(2, "科技"));
        myChannels.add(new Channel(3, "娱乐"));
        myChannels.add(new Channel(4, "读书"));
        myChannels.add(new Channel(5, "安全"));
        spareChannels.add(new Channel(5, "幸福"));
        spareChannels.add(new Channel(5, "快乐"));
        spareChannels.add(new Channel(5, "健身"));
        spareChannels.add(new Channel(5, "逗比"));
        spareChannels.add(new Channel(5, "电影"));
        spareChannels.add(new Channel(5, "逗比"));
        spareChannels.add(new Channel(5, "音乐"));
        spareChannels.add(new Channel(5, "游戏"));
        spareChannels.add(new Channel(5, "美女"));

        final int spanCount = 3;

        adapter = new ChannelListAdapter(this, myChannels, false);
        adapter.setOnRecyclerViewListener(this);
        topicCategoryManageTopRecyclerView.setHasFixedSize(true);
        //使RecyclerView自适应高度
        final WrappableGridLayoutManager layoutManager = new WrappableGridLayoutManager(this, spanCount);
        topicCategoryManageTopRecyclerView.setLayoutManager(layoutManager);
        //设置边距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        topicCategoryManageTopRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, true));
        topicCategoryManageTopRecyclerView.setAdapter(adapter);

        //备用频道
        spareAdapter = new SpareListAdapter(spareChannels);
        spareAdapter.setBottomItemClickListener(this);
        topicCategoryManageBottomRecyclerView.setHasFixedSize(true);
        WrappableGridLayoutManager bottomlayoutManager = new WrappableGridLayoutManager(this, spanCount);
        topicCategoryManageBottomRecyclerView.setLayoutManager(bottomlayoutManager);
        topicCategoryManageBottomRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, true));
        topicCategoryManageBottomRecyclerView.setAdapter(spareAdapter);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemClick(int position) {
        if (editable) {
            Channel toAddItem = adapter.list.get(position);
            adapter.list.remove(position);
            adapter.notifyDataSetChanged();


            int location = spareAdapter.getItemCount();
            spareAdapter.list.add(location, toAddItem);
            spareAdapter.notifyDataSetChanged();

        } else {
            //进入该频道
            //.....
        }
    }

    @Override
    public boolean onItemLongClick(int position) {
        editable = true;
        Message mag = new Message();
        mag.what = 0x111;
        mHandler.sendMessage(mag);
        return false;
    }

    @Override
    public void onBottomListItemClick(int position) {
        //   Toast.makeText(this, "添加频道:" + spareChannels.get(position), Toast.LENGTH_SHORT).show();
        Channel toAddItem = spareAdapter.list.get(position);
        spareAdapter.notifyItemRemoved(position);
        spareAdapter.list.remove(position);
        spareAdapter.notifyItemRangeChanged(position, spareAdapter.getItemCount());
        spareAdapter.notifyDataSetChanged();


        int location = adapter.getItemCount();
        adapter.list.add(location, toAddItem);
        adapter.notifyDataSetChanged();


    }

    private void initData() {


    }
}
