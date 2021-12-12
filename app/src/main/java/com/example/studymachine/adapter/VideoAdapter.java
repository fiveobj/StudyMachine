package com.example.studymachine.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.studymachine.R;
import com.example.studymachine.customclass.VideoItem;
import com.example.studymachine.VideoPlayerActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<VideoItem> list=new ArrayList<VideoItem>();
    private ArrayList<Bitmap> bitmaps=new ArrayList<>();
    private ArrayList<String> bitmaps_s=new ArrayList<>();

    public static final String TAG="ListNormalAdapter2";

    public VideoAdapter(Context context,ArrayList<VideoItem> list){
        this.context=context;
        this.list=list;

    }

    private Handler getPictureHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    bitmaps.add((Bitmap) msg.obj);
                    Log.d("adapter-bitmap", msg.obj.toString());
            }

            return false;
        }
    });;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder=new ViewHolder(LayoutInflater.from(context).inflate(R.layout.videolist_item,parent,false));
        /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"点击了",Toast.LENGTH_SHORT).show();
            }
        });*/
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh=(ViewHolder) holder;

        vh.name.setText(list.get(position).getName());
        vh.intro.setText(list.get(position).getIntro());
        //vh.image.setImageBitmap(list.get(position).getImage());
        vh.status=list.get(position).getStatus();

        //vh.image.setImageBitmap(bitmaps.get(position));
        RoundedCorners roundedCorners=new RoundedCorners(20);
        RequestOptions options=RequestOptions.bitmapTransform(roundedCorners).override(300,300);

        Glide.with(context).load(list.get(position).getUrl_img()).apply(options).into(vh.image);

        //new ImageViewLoadAndSetSizeAndCircle().setImage(vh.image,list.get(position).getUrl_img(),167,167,35);
        Map<String,String> header=new HashMap<>();
        header.put("ee","33");

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, VideoPlayerActivity.class);

                intent.putParcelableArrayListExtra("videobundle", list);
                intent.putExtra("clickitem",position);
                Log.d("Onitem-0"," "+position);
                Log.d("videobundle-put",list.get(0).toString());
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name,intro;
        private RelativeLayout layout;
        private String status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.video_name);
            intro=itemView.findViewById(R.id.video_intro);
            image=itemView.findViewById(R.id.video_ima);
            layout=itemView.findViewById(R.id.video_layout);
            status="0";
        }
    }



}
