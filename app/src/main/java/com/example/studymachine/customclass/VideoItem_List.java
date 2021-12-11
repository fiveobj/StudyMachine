package com.example.studymachine.customclass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class VideoItem_List implements Parcelable {

    private List<VideoItem> list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
    }

    public void readFromParcel(Parcel source) {
        this.list = source.createTypedArrayList(VideoItem.CREATOR);
    }

    public VideoItem_List() {
    }

    protected VideoItem_List(Parcel in) {
        this.list = in.createTypedArrayList(VideoItem.CREATOR);
    }

    public static final Parcelable.Creator<VideoItem_List> CREATOR = new Parcelable.Creator<VideoItem_List>() {
        @Override
        public VideoItem_List createFromParcel(Parcel source) {
            return new VideoItem_List(source);
        }

        @Override
        public VideoItem_List[] newArray(int size) {
            return new VideoItem_List[size];
        }
    };
}
