package com.example.studymachine.customclass;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.studymachine.R;
import com.example.studymachine.tool.SerializableBitmap;

import java.io.Serializable;
import java.util.HashMap;

public class VideoItem implements Parcelable {

    private String intro;
    private String name;
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

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
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
}
