package com.example.studymachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studymachine.adapter.VideoAdapter;
import com.example.studymachine.adapter.VideoNameAdapter;
import com.example.studymachine.customclass.SpaceItemDecoration;
import com.example.studymachine.customclass.VideoItem;
import com.example.studymachine.customclass.okhttpClass;
import com.example.studymachine.view.HorizonTalListView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private HorizonTalListView VideoNameList;//水平标题
    private RecyclerView VideoList;//长视频播放列表
    private List<String> VideoName = new ArrayList<>();//视频类型
    private List<String> VideoNamePayStatus=new ArrayList<>();//是否支付
    private VideoNameAdapter NameAdapter;//视频类型适配器
    private VideoAdapter videoAdapter;
    private ArrayList<VideoItem> dataList = new ArrayList<>();//视频内容
    private List<Map<String,Object>> datalist1=new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private Context context;
    private String res;
    private String url,title,intro,status;
    private AlertDialog alertDialog;
    private EditText search;


    private Handler getPucturehandler;

    private OkHttpClient client;

    private LayoutAnimationController controller;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d("handler", msg.obj.toString());
                    dataList=(ArrayList<VideoItem>) msg.obj;
                    //动画
                    //controller=new LayoutAnimationController(AnimationUtils.loadAnimation(context,R.anim.animate));
                    //VideoList.setLayoutAnimation(controller);
                    videoAdapter = new VideoAdapter(context, (ArrayList<VideoItem>) msg.obj);
                    VideoList.setLayoutManager(gridLayoutManager);
                    VideoList.setAdapter(videoAdapter);

            }
            return false;
        }

    });



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

        gridLayoutManager = new GridLayoutManager(this, 3);


        VideoList.addItemDecoration(new SpaceItemDecoration(10, 20));

        VideoNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(VideoNamePayStatus.get(position).equals("0")){
                    alertDialog=new AlertDialog.Builder(context,R.style.AlertDialog).create();
                    View view1=View.inflate(MainActivity.this,R.layout.dialog_pay,null);
                    final ImageView close=(ImageView) view1.findViewById(R.id.dialog_pay_close);
                    final TextView textView1=(TextView) view1.findViewById(R.id.dialog_pay_text1);
                    final TextView textView2=(TextView) view1.findViewById(R.id.dialog_pay_text2);
                    final TextView textViewyes=(TextView) view1.findViewById(R.id.dialog_pay_textyes);
                    final TextView textViewno=(TextView) view1.findViewById(R.id.dialog_pay_textno);
                    final ImageView pay_yes=(ImageView) view1.findViewById(R.id.dialog_pay_yes);
                    final ImageView pay_no=(ImageView) view1.findViewById(R.id.dialog_pay_no);
                    final Boolean[] issure = {false};
                    alertDialog.setView(view1,0,0,0,0);
                    textView1.setText("此栏目内容需要付费观看");
                    textView2.setText("是否购买？");
                    alertDialog.show();

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    pay_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (issure[0]==false)
                            {
                                issure[0] =true;
                                textViewyes.setText("确认");
                                textViewno.setText("取消");
                                textView1.setText("消息已发送至家长端，完成支付后可观看");
                                textView2.setVisibility(View.GONE);
                            }
                            else {
                                alertDialog.dismiss();
                            }
                        }
                    });

                    pay_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
                else
                {
                    getVideo(VideoName.get(position));
                    NameAdapter.notifyDataSetChanged();
                }
                Log.i("test", "点击了" + position);
                NameAdapter.setSelectItem(position);
                Log.d("position.name", VideoName.get(position));

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void init() {
        VideoList = (RecyclerView) findViewById(R.id.video_recyc);
        VideoNameList = (HorizonTalListView) findViewById(R.id.videoname_list);
        videoAdapter = new VideoAdapter(context, dataList);
        VideoList.setLayoutManager(gridLayoutManager);
        VideoList.setAdapter(videoAdapter);
        search=(EditText) findViewById(R.id.video_search);


    }

    //视频类型
    private List<Map<String, Object>> getName() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < VideoName.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", VideoName.get(i));
            map.put("payStatus",VideoNamePayStatus.get(i));
            Log.d("VideoName+PayStatus-", VideoName.toString()+VideoNamePayStatus.toString());
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


    //获取视频类别
    private void getVideotype() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                okhttpClass tools = new okhttpClass();
                String result = tools.getViodeType();
                Log.d("getViodeType", result);
                String name = null;
                String payState=null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String VideoNames = jsonObject.getString("data");
                    JSONArray VideoNamearrray = new JSONArray(VideoNames);
                    for (int i = 0; i < VideoNamearrray.length(); i++) {
                        JSONObject videonameitem = VideoNamearrray.getJSONObject(i);
                        if (videonameitem != null) {

                            name = videonameitem.optString("typeName");
                            payState=videonameitem.optString("payState");
                            VideoNamePayStatus.add(payState);
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

    //搜索视频
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
                Looper.prepare();
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
                            List<Map<String,Object>> list_map=new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                if(jsonObject1!=null){
                                    String url=jsonObject1.optString("videoUrl");
                                    String title=jsonObject1.optString("title");
                                    String intro=jsonObject1.optString("introduce");
                                    String status=jsonObject1.optString("status");
                                    String url_img=jsonObject1.optString("coverUrl");
                                    Map<String,Object> map=new HashMap<>();

                                    VideoItem videoItem=new VideoItem(title,intro,url,status,url_img);
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

                Looper.loop();
            }
        }).start();
    }


}