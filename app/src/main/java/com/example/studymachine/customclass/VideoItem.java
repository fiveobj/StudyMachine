package com.example.studymachine.customclass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.studymachine.R;
import com.example.studymachine.tool.SerializableBitmap;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class VideoItem implements Parcelable {

    private String intro;
    private String name;
    private Bitmap image;
    private String url;
    private String status;
    private String url_img;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what)
            {
                case 1:
                    Looper.prepare();
                    image=(Bitmap) msg.obj;
                    Looper.loop();
            }
            return false;
        }
    });

    public VideoItem(){

    }

    public VideoItem(String name,String intro,String url,String status,String url_img){
        this.intro=intro;
        this.name=name;
        this.url=url;
        this.status=status;
        this.url_img=url_img;
        //getPicture(url_img);
        //image=getBitmapFormUrl(url);
    }


    public String getIntro() {
        return intro;
    }

    public String getName() {
        return name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getUrl_img() {
        return url_img;
    }

    //视频帧作为封面
    public static Bitmap getBitmapFormUrl(String url){
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
        /*getFrameAtTime()--->在setDataSource()之后调用此方法。 如果可能，该方法在任何时间位置找到代表性的帧，
         并将其作为位图返回。这对于生成输入数据源的缩略图很有用。**/
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(this.intro);
        dest.writeString(this.name);
        //dest.writeParcelable(this.image, flags);
        dest.writeString(this.url);
        dest.writeString(this.status);
    }

    public void readFromParcel(Parcel source) {
        //this.intro = source.readString();
        this.name = source.readString();
        //this.image = source.readParcelable(Bitmap.class.getClassLoader());
        this.url = source.readString();
        this.status = source.readString();
    }

    protected VideoItem(Parcel in) {
        //this.intro = in.readString();
        this.name = in.readString();
        //this.image = in.readParcelable(Bitmap.class.getClassLoader());
        this.url = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel source) {
            VideoItem videoItem=new VideoItem();
            videoItem.name=source.readString();
            videoItem.url=source.readString();
            videoItem.status=source.readString();
            return videoItem;
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };


    private void getPicture(String path){
        new Thread(){
            private HttpURLConnection conn;
            private Bitmap bitmap;

            @Override
            public void run() {
                Looper.prepare();
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
                        msg.what = 1;
                        msg.obj = bitmap;
                        handler.sendMessage(msg);
                        Log.d("getPicture", "true");
                    } else {
                        Log.d("getPicture", "FW");
                        //返回码不等于200 请求服务器失败
                        //Message msg = new Message();
                        //msg.what = 3;
                        //handler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("picture-e1:",e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("picture-e2:",e.toString());
                }
                conn.disconnect();
                Looper.loop();
            }
        }.start();

    }



}
