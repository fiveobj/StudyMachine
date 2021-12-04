package com.example.studymachine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studymachine.R;
import com.example.studymachine.customclass.VideoItem;
import com.example.studymachine.VideoPlayerActivity;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<VideoItem> list;

    public static final String TAG="ListNormalAdapter2";

    public VideoAdapter(Context context,List<VideoItem> list){
        this.context=context;
        this.list=list;
    }

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
        vh.image.setImageBitmap(list.get(position).getImage());


        //-----动态添加ImageView------
        ImageView lockimage=new ImageView(context);
        lockimage.setImageResource(R.drawable.lock);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 130);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.topMargin=10;
        lockimage.setLayoutParams(lp);
        vh.layout.addView(lockimage);


        Map<String,String> header=new HashMap<>();
        header.put("ee","33");

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, VideoPlayerActivity.class);
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.video_name);
            intro=itemView.findViewById(R.id.video_intro);
            image=itemView.findViewById(R.id.video_ima);
            layout=itemView.findViewById(R.id.video_layout);
        }
    }
}
