package com.example.lucas.running_cat1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wangchi on 4/27/16.
 */
public class RankAdapter extends BaseAdapter {

    private List<Rankmem> list;
    private Context context;
    private LinearLayout layout;

    public RankAdapter(List<Rankmem> list,Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        layout = (LinearLayout) inflater.inflate(R.layout.rank_item,null);
//
//        ImageView icon = (ImageView) layout.findViewById(R.id.rank_icon);
//        TextView id = (TextView) layout.findViewById(R.id.rank_id);
//        TextView nick = (TextView) layout.findViewById(R.id.rank_nick);
//        TextView level = (TextView) layout.findViewById(R.id.rank_level);
//        TextView no = (TextView) layout.findViewById(R.id.rank_no);
//
//
//        int resid;
//
//        if(position == 0) {
//            resid = R.drawable.star_gold;
//        }else if(position == 1) {
//            resid = R.drawable.star_silver;
//        }else if(position == 2) {
//            resid = R.drawable.star_copper;
//        }else{
//            resid = R.drawable.star_iron;
//        }
//
//        icon.setImageResource(resid);
//
//        id.setText("ID:"+list.get(position).getId());
//        nick.setText("姓名:"+list.get(position).getNickname());
//        level.setText(String.valueOf(list.get(position).getLevel()));
//        no.setText("No."+(position+1));

        //使用viewholder进行优化

        int resid;
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.rank_item,null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.rank_icon);
            holder.id = (TextView) convertView.findViewById(R.id.rank_id);
            holder.nickname = (TextView) convertView.findViewById(R.id.rank_nick);
            holder.level = (TextView) convertView.findViewById(R.id.rank_level);
            holder.no = (TextView) convertView.findViewById(R.id.rank_no);

            if(position == 0) {
                resid = R.drawable.star_gold;
            }else if(position == 1) {
                resid = R.drawable.star_silver;
            }else if(position == 2) {
                resid = R.drawable.star_copper;
            }else{
                resid = R.drawable.star_iron;
            }

            holder.icon.setImageResource(resid);

            holder.id.setText("用户名:"+list.get(position).getId());
            holder.nickname.setText("昵称:"+list.get(position).getNickname());
            holder.level.setText(String.valueOf(list.get(position).getLevel()) + "级");
            holder.no.setText("No."+(position+1));

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();

            if(position == 0) {
                resid = R.drawable.star_gold;
            }else if(position == 1) {
                resid = R.drawable.star_silver;
            }else if(position == 2) {
                resid = R.drawable.star_copper;
            }else{
                resid = R.drawable.star_iron;
            }

            holder.icon.setImageResource(resid);

            holder.id.setText("用户名:"+list.get(position).getId());
            holder.nickname.setText("昵称:"+list.get(position).getNickname());
            holder.level.setText(String.valueOf(list.get(position).getLevel()) + "级");
            holder.no.setText("No."+(position+1));
        }

        return convertView;
    }

    private static class ViewHolder{
        ImageView icon;
        TextView id;
        TextView nickname;
        TextView level;
        TextView no;
    }
}
