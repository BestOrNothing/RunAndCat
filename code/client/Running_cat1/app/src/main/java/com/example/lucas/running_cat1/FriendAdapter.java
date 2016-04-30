package com.example.lucas.running_cat1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.preference.DialogPreference;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzhensong on 4/26/16.
 */
public class FriendAdapter extends ArrayAdapter<Friend> {
    private int resourceId;
    private int show;
    private final int showDelete = 1<<3;
    private final int showAdd = 1<<2;
    private final int showAccept = 1<<1;
    private final int showRefuse = 1;
    private Button delete;
    private Button add;
    private Button accept;
    private Button refuse;
    private int mutex = 1;

    public FriendAdapter(Context context, int textViewResourceId, List<Friend> objects, int show) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        this.show = show;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final Friend friend = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView friendImage = (ImageView) view.findViewById(R.id.friend_image);
        TextView nickname = (TextView) view.findViewById(R.id.friend_nickname);
        TextView location = (TextView) view.findViewById(R.id.friend_location);
        TextView level = (TextView) view.findViewById(R.id.friend_level);
        nickname.setText(friend.getNickname());
        location.setText(friend.getLocation());
        location.setVisibility(View.GONE);
        level.setText(friend.getLevel() + "级");
        delete = (Button) view.findViewById(R.id.delete_friend);
        add = (Button) view.findViewById(R.id.add_friend);
        accept = (Button) view.findViewById(R.id.accept_friend);
        refuse = (Button) view.findViewById(R.id.refuse_friend);
        if((show & showDelete) == 0) {
            delete.setVisibility(View.GONE);
        }
        if((show & showAdd) == 0) {
            add.setVisibility(View.GONE);
        }
        if((show & showAccept) == 0) {
            accept.setVisibility(View.GONE);
        }
        if((show & showRefuse) == 0) {
            refuse.setVisibility(View.GONE);
        }

        if((show & showDelete) > 0) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext()).setTitle("删除好友")
                            .setMessage("确定删除好友么？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mutex--;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String id = friend.getId();
                                            try {
                                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                JSONObject para = new JSONObject();
                                                String lowerId = CurUser.getInstance().id;
                                                String upperId = friend.getId();
                                                if (lowerId.compareTo(upperId) > 0) {
                                                    String tmp = lowerId;
                                                    lowerId = upperId;
                                                    upperId = tmp;
                                                }
                                                para.put("lowerId", lowerId);
                                                para.put("upperId", upperId);
                                                JSONObject request = new JSONObject();
                                                request.put("para", para);
                                                params.add(new BasicNameValuePair("request", request.toString()));
                                                HttpResponse httpResponse = MyHttp.sendPost(Url.basePath + "delete.php", params);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            mutex++;
                                        }
                                    }).start();
                                    while (mutex <= 0) ;
                                    Intent intent = new Intent(getContext(), FriendListActivity.class);
                                    Toast toast = Toast.makeText(getContext(), "好友已删除", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    getContext().startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                }
            });
        }
        if((show & showAdd) > 0) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mutex--;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String id = friend.getId();
                            try {
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                JSONObject para = new JSONObject();
                                para.put("sendId", CurUser.getInstance().id);
                                para.put("rcvId", friend.getId());
                                JSONObject request = new JSONObject();
                                request.put("para", para);
                                params.add(new BasicNameValuePair("request", request.toString()));
                                HttpResponse httpResponse = MyHttp.sendPost(Url.basePath + "add.php", params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mutex++;
                        }
                    }).start();
                    while (mutex <= 0) ;
                    Intent intent = new Intent(getContext(), NewFriendActivity.class);
                    Toast toast = Toast.makeText(getContext(), "好友请求已发送", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    getContext().startActivity(intent);
                }
            });
        }
        if((show & showAccept) > 0) {
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mutex--;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String id = friend.getId();
                            try {
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                JSONObject para = new JSONObject();
                                String lowerId = CurUser.getInstance().id;
                                String upperId = friend.getId();
                                if (lowerId.compareTo(upperId) > 0) {
                                    String tmp = lowerId;
                                    lowerId = upperId;
                                    upperId = tmp;
                                }
                                para.put("lowerId", lowerId);
                                para.put("upperId", upperId);
                                JSONObject request = new JSONObject();
                                request.put("para", para);
                                params.add(new BasicNameValuePair("request", request.toString()));
                                HttpResponse httpResponse = MyHttp.sendPost(Url.basePath + "accept.php", params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mutex++;
                        }
                    }).start();
                    while (mutex <= 0) ;
                    Intent intent = new Intent(getContext(), NewFriendActivity.class);
                    Toast toast = Toast.makeText(getContext(), "已接受好友请求", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    getContext().startActivity(intent);
                }
            });
        }
        if((show & showRefuse) > 0) {
            refuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mutex--;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String id = friend.getId();
                            try {
                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                JSONObject para = new JSONObject();
                                String lowerId = CurUser.getInstance().id;
                                String upperId = friend.getId();
                                if(lowerId.compareTo(upperId) > 0) {
                                    String tmp = lowerId;
                                    lowerId = upperId;
                                    upperId = tmp;
                                }
                                para.put("lowerId", lowerId);
                                para.put("upperId", upperId);
                                JSONObject request = new JSONObject();
                                request.put("para", para);
                                params.add(new BasicNameValuePair("request", request.toString()));
                                HttpResponse httpResponse = MyHttp.sendPost(Url.basePath + "refuse.php", params);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mutex++;
                        }
                    }).start();
                    while(mutex <= 0) ;
                    Intent intent = new Intent(getContext(), NewFriendActivity.class);
                    Toast toast = Toast.makeText(getContext(), "已拒绝好友请求", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    getContext().startActivity(intent);
                }
            });
        }
        return view;
    }
}
