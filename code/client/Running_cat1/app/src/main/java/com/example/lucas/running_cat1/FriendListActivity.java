package com.example.lucas.running_cat1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzhensong on 4/25/16.
 */
public class FriendListActivity extends Activity {

    private List<Friend> list = new ArrayList<Friend>();

    private final int showDelete = 1<<3;
    private final int showAdd = 1<<2;
    private final int showAccept = 1<<1;
    private final int showRefuse = 1;

    private int mutex = 1;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(FriendListActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.friend_list);
        Intent intent = getIntent();
        String id = intent.getStringExtra("searchId");
        if(id != null) {
            searchUserById(id);
        }
        else {
            initList();
        }
        ListView listView = (ListView)findViewById(R.id.friend_list_view);
        FriendAdapter adapter;
        if(id != null) {
            while (mutex <= 0) ;
            adapter = new FriendAdapter(FriendListActivity.this, R.layout.friend_item, list, showAdd);
            ( (TextView) findViewById(R.id.friend_list_title) ).setText("新朋友");
        }
        else {
            while (mutex <= 0) ;
            adapter = new FriendAdapter(FriendListActivity.this, R.layout.friend_item, list, showDelete);
        }
        listView.setAdapter(adapter);
    }

    private void searchUserById(final String id) {
        mutex--;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                try {
                    JSONObject para = new JSONObject();
                    para.put("friendId", id);
                    JSONObject request = new JSONObject();
                    request.put("para", para);
                    params.add(new BasicNameValuePair("request", request.toString()));
                    HttpResponse httpResponse = MyHttp.sendPost(Url.basePath + "friend.php", params);
                    if(httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String strResponse = EntityUtils.toString(entity, "utf-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        JSONObject head = jsonObject.getJSONObject("head");
                        String code = head.getString("code");
                        if(code.equals("007")) {
                            JSONArray array = head.getJSONArray("friendlist");
                            list = Friend.getFriendList(1, array);
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mutex++;
            }
        }).start();
    }

    private void initList() {
        mutex--;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    JSONObject para = new JSONObject();
                    para.put("id", CurUser.getInstance().id);
                    JSONObject request = new JSONObject();
                    request.put("para", para);
                    params.add(new BasicNameValuePair("request", request.toString()));
                    HttpResponse response = MyHttp.sendPost(Url.basePath + "friend.php", params);
                    if(response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        String strResponse = EntityUtils.toString(entity, "utf-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        JSONObject head = jsonObject.getJSONObject("head");
                        list = Friend.getFriendList(head.getInt("count"), head.getJSONArray("friendlist"));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mutex++;
            }
        }).start();
    }
}
