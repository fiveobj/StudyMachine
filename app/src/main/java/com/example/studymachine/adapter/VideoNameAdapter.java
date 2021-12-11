package com.example.studymachine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.studymachine.R;

import java.util.List;
import java.util.Map;

public class VideoNameAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> list;
    private int selectItem = -1;

    public VideoNameAdapter(Context context, List<Map<String, Object>> maps) {
        this.context = context;
        this.list = maps;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null && list.size() != 0) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.videoname_item, null);
            viewHolder.itemTextView = (TextView) convertView.findViewById(R.id.Videoname_tv);
            viewHolder.itemImage=(ImageView)convertView.findViewById(R.id.Videoname_im);

            //viewHolder.portalLinearLayout = (LinearLayout) convertView.findViewById(R.id.Videoname_ll);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.itemTextView.setText(list.get(position).get("name").toString());
        if(list.get(position).get("payStatus").toString().equals("0")){
            viewHolder.itemImage.setVisibility(View.VISIBLE);
        }else
        {
            viewHolder.itemImage.setVisibility(View.GONE);
        }
        if (selectItem == position) {
            viewHolder.itemTextView.setSelected(true);
            viewHolder.itemTextView.setPressed(true);
            viewHolder.itemTextView.setTextColor(Color.parseColor("#FFFFFF"));
            //viewHolder.itemTextView.setTextSize(24);
            //viewHolder.portalLinearLayout.setBackgroundColor(Color.YELLOW);
        } else {
            viewHolder.itemTextView.setSelected(false);
            viewHolder.itemTextView.setPressed(false);
            //viewHolder.portalLinearLayout.setBackgroundColor(Color.TRANSPARENT);
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView itemTextView;
        private ImageView itemImage;
    }

}