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
        for(int i=0;i<list.size();i++){
            //bitmaps_s.add(list.get(i).getUrl_img());
            getPicture(list.get(i).getUrl_img());
        }
        //getPicture(bitmaps_s);
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
                //Toast.makeText(context,"点击了"+position,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, VideoPlayerActivity.class);
                //Bundle bundle=new Bundle();
                //bundle.putSerializable("videobundle", (Serializable) list);
                //intent.putExtras(bundle);

                //WrapGood<VideoItem> wrapGood=new WrapGood<>();
                //wrapGood.goodsArray=list;

                //bundle.putParcelable("videobundle", (Parcelable) list);
                intent.putParcelableArrayListExtra("videobundle", list);
                intent.putExtra("clickitem",position);
                Log.d("videobundle-put",list.get(0).toString());
                context.startActivity(intent);
            }
        });



        /*if (vh.status.equals("0")){


        }
        else
        {
            //-----动态添加ImageView------
            ImageView lockimage=new ImageView(context);
            lockimage.setImageResource(R.drawable.lock);
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 130);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            lp.topMargin=10;
            lockimage.setLayoutParams(lp);
            vh.layout.addView(lockimage);
            lockimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"该商品需要付费",Toast.LENGTH_SHORT).show();
                }
            });
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"该商品需要付费",Toast.LENGTH_SHORT).show();
                    //Intent intent=new Intent(context, VideoPlayerActivity.class);
                    //context.startActivity(intent);
                }
            });
        }*/






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


    private void getPicture(ArrayList<String> path){
        new Thread(){
            private HttpURLConnection conn;
            private ArrayList<Bitmap> bitmap;

            @Override
            public void run() {

                try {
                        for (int i=0;i<path.size();i++)
                        {
                            //创建URL对象
                            URL url=new URL(path.get(i));
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
                                bitmap.add(BitmapFactory.decodeStream(is));

                                Log.d("getPicture", "true");
                            } else {
                                Log.d("getPicture", "FW");
                                //返回码不等于200 请求服务器失败
                                //Message msg = new Message();
                                //msg.what = 3;
                                //handler.sendMessage(msg);
                            }

                            conn.disconnect();
                        }
                    //将更改主界面的消息发送给主线程
                    Message msg = new Message();
                    //msg.what = 1;
                    msg.obj = bitmap;
                    getPictureHandler.sendMessage(msg);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("picture-e1:",e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("picture-e2:",e.toString());
                }

            }
        }.start();

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
                            bitmap=BitmapFactory.decodeStream(is);
                            //将更改主界面的消息发送给主线程
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bitmap;
                            getPictureHandler.sendMessage(msg);
                            Log.d("getPicture", "true");
                        } else {
                            Log.d("getPicture", "FW");
                            //返回码不等于200 请求服务器失败
                            //Message msg = new Message();
                            //msg.what = 3;
                            //handler.sendMessage(msg);
                        }

                        conn.disconnect();





                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d("picture-e1:",e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("picture-e2:",e.toString());
                }

            }
        }.start();

    }

}
