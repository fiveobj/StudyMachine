package com.example.studymachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.example.studymachine.adapter.VideoAdapter;
import com.example.studymachine.adapter.VideoNameAdapter;
import com.example.studymachine.customclass.SpaceItemDecoration;
import com.example.studymachine.customclass.VideoItem;
import com.example.studymachine.customclass.okhttpClass;
import com.example.studymachine.view.HorizonTalListView;
import com.google.android.exoplayer2.C;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

public class MainActivity extends AppCompatActivity {

    private HorizonTalListView VideoNameList;//水平标题
    private RecyclerView VideoList;//长视频播放列表
    private List<String> VideoName = new ArrayList<>();//视频类型
    private VideoNameAdapter NameAdapter;//视频类型适配器
    private VideoAdapter videoAdapter;
    ArrayList<VideoItem> dataList = new ArrayList<>();//视频内容
    private GridLayoutManager gridLayoutManager;
    private Context context;
    private String res;
    private String url,title,intro,status;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    //MainActivity.this.res=msg.obj.toString();
                    Log.d("handler", msg.obj.toString());

                    videoAdapter = new VideoAdapter(context, (ArrayList<VideoItem>) msg.obj);
                    VideoList.setLayoutManager(gridLayoutManager);
                    VideoList.setAdapter(videoAdapter);
                    //handlerdata(msg.obj.toString());
            }
            return false;
        }
    });

    private final String mp4_a = "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4";//玩具总动员
    private final String mp4_b = "http://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4";  //抓小偷

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init();
        getVideotype();
        Log.d("VideoName-", VideoName.toString());


        recognitionVideoType();
        getVideo("红色文化");
        //initData();


        gridLayoutManager = new GridLayoutManager(this, 3);


        VideoList.addItemDecoration(new SpaceItemDecoration(10, 20));

        VideoNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("test", "点击了" + position);
                NameAdapter.setSelectItem(position);
                Log.d("position.name", VideoName.get(position));
                getVideo(VideoName.get(position));
                NameAdapter.notifyDataSetChanged();
            }
        });


    }

    private void init() {
        VideoList = (RecyclerView) findViewById(R.id.video_recyc);
        VideoNameList = (HorizonTalListView) findViewById(R.id.videoname_list);
        videoAdapter = new VideoAdapter(context, dataList);
        VideoList.setLayoutManager(gridLayoutManager);
        VideoList.setAdapter(videoAdapter);
    }

    //视频类型
    private List<Map<String, Object>> getName() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < VideoName.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", VideoName.get(i));
            Log.d("VideoName-", VideoName.toString());
            list.add(map);
        }
        return list;
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
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
    //视频数据
    /*private void initData(){
        dataList=new ArrayList<>();
        for(int i=0;i<10;i++){
            VideoItem videoItem=new VideoItem("1"+i,"1"+i,mp4_a);
            dataList.add(videoItem);
        }
    }*/

    //获取视频类别
    private void getVideotype() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                okhttpClass tools = new okhttpClass();
                String result = tools.getViodeType();
                Log.d("getViodeType", result);
                String name = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String VideoNames = jsonObject.getString("data");
                    JSONArray VideoNamearrray = new JSONArray(VideoNames);
                    for (int i = 0; i < VideoNamearrray.length(); i++) {
                        JSONObject videonameitem = VideoNamearrray.getJSONObject(i);
                        if (videonameitem != null) {
                            name = videonameitem.optString("typeName");
                            VideoName.add(name);
                            Log.d("name", name);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NameAdapter = new VideoNameAdapter(context, getName());
                            NameAdapter.setSelectItem(0);
                            VideoNameList.setAdapter(NameAdapter);
                        }
                    });
                    Log.d("videoname", VideoName.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //识别视频类型
    private void recognitionVideoType() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                okhttpClass tools = new okhttpClass();
                String result = tools.searchVideo("红色文化");
                Log.d("searchVideo", result);
            }
        }).start();
    }

    //获取视频数据
    private void getVideo(String typename) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[] result = {null};
                okhttpClass tools = new okhttpClass();
                tools.setData(new okhttpClass.data() {
                    @Override
                    public String getData(String str) {
                        Log.d("getViode", str);
                        try {
                            ArrayList<VideoItem> data=new ArrayList<>();
                            JSONObject jsonObject=new JSONObject(str);
                            String data1=jsonObject.getString("data");
                            JSONArray jsonArray=new JSONArray(data1);
                            Log.d("data1",jsonArray.toString());
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                if(jsonObject1!=null){
                                    String url=jsonObject1.optString("videoUrl");
                                    String title=jsonObject1.optString("title");
                                    String intro=jsonObject1.optString("introduce");
                                    String status=jsonObject1.optString("status");
                                    String url_img=jsonObject1.optString("coverUrl");
                                    //Bitmap image=getPicture(url_img);
                                    VideoItem videoItem=new VideoItem(title,intro,url,status);
                                    data.add(videoItem);
                                }
                            }
                            Message message = new Message();
                            message.what = 1;
                            message.obj = data;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        return str;
                    }
                });

                result[0] = tools.getViode(typename);

                //Log.d("getViode", res);
                /*try {
                    dataList.clear();
                    JSONObject jsonObjectdata = new JSONObject(result[0]);
                    String data = jsonObjectdata.getString("data");
                    JSONArray jsonObject = new JSONArray(data);
                    Log.d("data", jsonObject.toString());
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                        if (jsonObject1 != null) {
                            url = jsonObject1.optString("videoUrl");
                            title = jsonObject1.optString("title");
                            intro = jsonObject1.optString("introduce");
                            status = jsonObject1.optString("status");
                            VideoItem videoItem = new VideoItem(title, intro, url, status);
                            dataList.add(videoItem);
                            Log.d("videoUrl", url);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                videoAdapter = new VideoAdapter(context, dataList);
                                VideoList.setLayoutManager(gridLayoutManager);
                                VideoList.setAdapter(videoAdapter);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("play_rtsp2", e.toString());
                }*/


            }
        }).start();
    }

    private void handlerdata(String s) {
        try {
            dataList.clear();
            JSONObject jsonObjectdata = new JSONObject(s);
            String data = jsonObjectdata.getString("data");
            JSONArray jsonObject = new JSONArray(data);
            Log.d("data", jsonObject.toString());
            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject jsonObject1 = jsonObject.getJSONObject(i);
                if (jsonObject1 != null) {
                    url = jsonObject1.optString("videoUrl");
                    title = jsonObject1.optString("title");
                    intro = jsonObject1.optString("introduce");
                    status = jsonObject1.optString("status");

                    VideoItem videoItem = new VideoItem(title, intro, url, status);
                    dataList.add(videoItem);
                    Log.d("videoUrl", url);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        videoAdapter = new VideoAdapter(context, dataList);
        VideoList.setLayoutManager(gridLayoutManager);
        VideoList.setAdapter(videoAdapter);


    }


    private void getPicture(String path){
        new Thread(){
            private HttpURLConnection conn;
            private Bitmap bitmap;

            @Override
            public void run() {
                try {
                    //创建URL对象
                    URL url=new URL(path);
                    // 根据url 发送 http的请求
                    conn=(HttpURLConnection) url.openConnection();
                    // 设置请求的方式
                    conn.setRequestMethod("GET");
                    //设置超时时间
                    conn.setConnectTimeout(5000);
                    // 得到服务器返回的响应码
                    int code = conn.getResponseCode();
                    //请求网络成功后返回码是200
                    if (code == 200) {
                        //获取输入流
                        InputStream is = conn.getInputStream();
                        //将流转换成Bitmap对象
                        bitmap = BitmapFactory.decodeStream(is);
                        //将更改主界面的消息发送给主线程
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                    } else {
                        //返回码不等于200 请求服务器失败
                        //Message msg = new Message();
                        //msg.what = 3;
                        //handler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            }
        }.start();

    }
}