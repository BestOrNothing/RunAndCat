package com.example.lucas.running_cat1;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzhensong on 4/26/16.
 */
public class NewFriendActivity extends Activity {


    private final int showDelete = 1<<3;
    private final int showAdd = 1<<2;
    private final int showAccept = 1<<1;
    private final int showRefuse = 1;
    private int mutex = 1;
    private List<Friend> list;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(NewFriendActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_friend);
        final EditText search = (EditText) findViewById(R.id.search_new_friend_edit);
        Button button = (Button) findViewById(R.id.search_new_friend_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewFriendActivity.this, FriendListActivity.class);
                intent.putExtra("searchId", search.getText().toString());
                startActivity(intent);
            }
        });
        getNewFriends();
        while(mutex <= 0) ;
        FriendAdapter adapter = new FriendAdapter(NewFriendActivity.this, R.layout.friend_item, list, showAccept | showRefuse);
        ListView listView = (ListView) findViewById(R.id.new_friend_list_view);
        listView.setAdapter(adapter);
    }

    private void getNewFriends() {
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
                    HttpResponse response = MyHttp.sendPost(Url.basePath + "rcv.php", params);
                    if(response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        String strResponse = EntityUtils.toString(entity, "utf-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        JSONObject head = jsonObject.getJSONObject("head");
                        list = Friend.getFriendList(head.getInt("count"), head.getJSONArray("friend"));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mutex++;
            }
        }).start();
    }
}