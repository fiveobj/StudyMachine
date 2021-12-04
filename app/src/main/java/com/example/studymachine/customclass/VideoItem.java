package com.example.studymachine.customclass;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;

import java.util.HashMap;

public class VideoItem {
    private String name,intro;
    private Bitmap image;
    private String url;
    private String status;

    public VideoItem(){

    }

    public VideoItem(String name,String intro,String url,String status){
        this.intro=intro;
        this.name=name;
        this.url=url;
        this.status=status;
        image=getBitmapFormUrl(url);
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


    public String getUrl() {
        return url;
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
}
