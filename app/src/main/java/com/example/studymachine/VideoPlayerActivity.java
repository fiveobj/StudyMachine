package com.example.studymachine;

import static com.example.studymachine.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.studymachine.adapter.VideoPlayerAdapter;
import com.example.studymachine.customclass.ScrollCalculatorHelper;
import com.example.studymachine.customclass.VideoItem;
import com.example.studymachine.customclass.Video_Bean;
import com.example.studymachine.tool.DpTools;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoPlayerActivity extends AppCompatActivity {

    private RecyclerView videoPlayer_recyc;
    private final String mp4_a = "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4";//玩具总动员
    private final String mp4_b = "http://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4";  //抓小偷
    private ArrayList<VideoItem> list;
    private Intent intent=new Intent();

    //控制滚动播放
    ScrollCalculatorHelper scrollCalculatorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_video_player);
        //StatusBarUtil.setColor(this, getResources().getColor(R.color.HaiPaiBlack));

        Bundle bundle=this.getIntent().getExtras();
        list=bundle.getParcelableArrayList("videobundle");
        Log.d("videobundle",list.toString());

        //initData();
        init();

    }
    private void initData() {
        //视频数据
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            VideoItem video_bean = new VideoItem();
            if (i % 2 == 0) {
                video_bean.setUrl(mp4_a);
            } else {
                video_bean.setUrl(mp4_b);
            }
            //video_bean.setImage(ContextCompat.getDrawable(this, mipmap.ic_launcher));
            video_bean.setName("傀儡偶段のVideo  " + i);

            list.add(video_bean);
        }

    }


    private void init() {





        videoPlayer_recyc = findViewById(id.video_player_recyc);

        VideoPlayerAdapter list_video_adapter = new VideoPlayerAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //自定播放帮助类 限定范围为屏幕一半的上下偏移180   括号里不用在意 因为是一个item一个屏幕
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.list_video_player
                , dm.heightPixels / 2 - DpTools.dip2px(this, 180)
                , dm.heightPixels / 2 + DpTools.dip2px(this, 180));

        //让RecyclerView有ViewPager的翻页效果
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(videoPlayer_recyc);
        //设置LayoutManager和Adapter
        videoPlayer_recyc.setLayoutManager(linearLayoutManager);
        videoPlayer_recyc.setAdapter(list_video_adapter);
        //设置滑动监听
        videoPlayer_recyc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //第一个可见视图,最后一个可见视图
            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //如果newState的状态==RecyclerView.SCROLL_STATE_IDLE;
                //播放对应的视频
                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Log.e("有几个item", firstVisibleItem + "    " + lastVisibleItem);
                //一屏幕显示一个item 所以固定1
                //实时获取设置 当前显示的GSYBaseVideoPlayer的下标
                scrollCalculatorHelper.onScroll(recyclerView, firstVisibleItem, lastVisibleItem, 1);

            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        GSYVideoManager.onPause();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        GSYVideoManager.releaseAllVideos();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {


        Configuration mConfiguration = this.getResources().getConfiguration();
        int ori = mConfiguration.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //当前为竖屏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(new Bundle());
    }
}