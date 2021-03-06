package com.example.studymachine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private HorizonTalListView VideoNameList;//????????????
    private RecyclerView VideoList;//?????????????????????
    private List<String> VideoName = new ArrayList<>();//????????????
    private List<String> VideoNamePayStatus=new ArrayList<>();//????????????
    private VideoNameAdapter NameAdapter;//?????????????????????
    private VideoAdapter videoAdapter;
    private ArrayList<VideoItem> dataList = new ArrayList<>();//????????????
    private List<Map<String,Object>> datalist1=new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private Context context;
    private String res;
    private String url,title,intro,status;
    private AlertDialog alertDialog;
    private ImageButton back;


    //??????
    private ImageView search_image;
    private EditText search;
    private RelativeLayout search_layout;
    private AutoTransition autoTransition;
    private Boolean isSearch=false;


    private Handler getPucturehandler;

    private OkHttpClient client;

    private LayoutAnimationController controller;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d("handler-1", msg.obj.toString());
                    dataList=(ArrayList<VideoItem>) msg.obj;
                    //??????
                    //controller=new LayoutAnimationController(AnimationUtils.loadAnimation(context,R.anim.animate));
                    //VideoList.setLayoutAnimation(controller);
                    videoAdapter = new VideoAdapter(context, (ArrayList<VideoItem>) msg.obj);
                    VideoList.setLayoutManager(gridLayoutManager);
                    VideoList.setAdapter(videoAdapter);
                case 2:
                    Log.d("handler-2", msg.obj.toString());
                    dataList=(ArrayList<VideoItem>) msg.obj;
                    //??????
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

        getVideo("????????????");

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
                    textView1.setText("?????????????????????????????????");
                    textView2.setText("???????????????");
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
                                textViewyes.setText("??????");
                                textViewno.setText("??????");
                                textView1.setText("??????????????????????????????????????????????????????");
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
                    Log.d("videoadapter.getposi", ""+NameAdapter.getposi());
                    NameAdapter.notifyDataSetChanged();
                }
                Log.i("test", "?????????" + position);
                NameAdapter.setSelectItem(position);
                Log.d("position.name", VideoName.get(position));

            }
        });


       //??????

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams) search_layout.getLayoutParams();
                layoutParams.width=dip2px(470);
                layoutParams.setMargins(dip2px(0),dip2px(25),dip2px(41),dip2px(0));
                search_layout.setLayoutParams(layoutParams);
                VideoNameList.setVisibility(View.GONE);
                isSearch=true;
                //????????????
                beginDelayedTransition(search_layout);
                return false;
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchVideo(search.getText().toString());
                Log.d("search onclick", "true");
                return true;
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearch)
                {
                    RelativeLayout.LayoutParams layoutParams=(RelativeLayout.LayoutParams) search_layout.getLayoutParams();
                    layoutParams.width=dip2px(260);
                    layoutParams.setMargins(dip2px(0),dip2px(25),dip2px(41),dip2px(0));
                    search_layout.setLayoutParams(layoutParams);


                    isSearch=false;
                    //????????????
                    beginDelayedTransition(search_layout);
                    search.setText(" ");
                    VideoNameList.setVisibility(View.VISIBLE);
                    getVideo(VideoName.get(NameAdapter.getposi()));
                    Log.d("videoadapter.getposi", ""+NameAdapter.getposi());

                }
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
        search_image=(ImageView) findViewById(R.id.search_image);
        search_layout=(RelativeLayout) findViewById(R.id.video_search_layout);
        back=(ImageButton) findViewById(R.id.back);

    }



    //????????????
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

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //???????????????

        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //???????????????
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //???????????????
        }

        super.onConfigurationChanged(newConfig);

    }


    //??????????????????
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
                            payState=videonameitem.optString("isFree");
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

    //????????????
    private void searchVideo(String keyword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                okhttpClass tools = new okhttpClass();
                String result = tools.searchVideo(keyword);
                Log.d("searchVideo", result);
                String title = null;
                String intro=null;
                String videoUrl=null;
                String coverUrl=null;
                String status=null;
                try {
                    ArrayList<VideoItem> data=new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(result);
                    String VideoNames = jsonObject.getString("data");
                    JSONArray VideoNamearrray = new JSONArray(VideoNames);
                    for (int i = 0; i < VideoNamearrray.length(); i++) {
                        JSONObject videonameitem = VideoNamearrray.getJSONObject(i);
                        if (videonameitem != null) {
                            title = videonameitem.optString("title");
                            intro=videonameitem.optString("introduce");
                            videoUrl=videonameitem.optString("videoUrl");
                            coverUrl=videonameitem.optString("coverUrl");
                            status=videonameitem.optString("status");
                            VideoItem videoItem=new VideoItem(title,intro,videoUrl,status,coverUrl);
                            data.add(videoItem);
                        }
                    }
                    Message message = new Message();
                    message.what = 2;
                    message.obj = data;
                    handler.sendMessage(message);
                    Log.d("videoname", VideoName.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }

    //??????????????????
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void beginDelayedTransition(ViewGroup view) {
        autoTransition = new AutoTransition();
        autoTransition.setDuration(500);
        TransitionManager.beginDelayedTransition(view, autoTransition);
    }

    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

}