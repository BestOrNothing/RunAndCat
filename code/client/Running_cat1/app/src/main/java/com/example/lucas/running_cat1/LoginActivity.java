package com.example.lucas.running_cat1;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by lucas on 2016/3/19.
 */

public class LoginActivity extends Activity{
    private String strPassword;   //密码字符串
    private String strID;            //账号字符串


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //发送和接收json数据
    private void sendRequestWithHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    //发送json对象给服务器
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Url.basePath + "login.php");
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject.put("id",strID );
                    jsonObject.put("password",strPassword );
                    jsonObject2.put("para", jsonObject);
                    nameValuePair.add(new BasicNameValuePair("request", jsonObject2
                            .toString()));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        //请求和响应都成功了
                        //从服务器接收json对象
                        HttpEntity entity = httpResponse.getEntity();
                        String strResponse = EntityUtils.toString(entity, "utf-8");
                        parseJSONWITHJSONObject(strResponse);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

        }).start();
    }

    //解析JSON格式数据
    private void parseJSONWITHJSONObject(String jsonDate){
        try{
            JSONObject jsonObject1 = new JSONObject(jsonDate);

            JSONObject jsonObject2 = jsonObject1.getJSONObject("head");
            String str = jsonObject2.getString("code");
            if(str.equals("003"))   //登录成功
            {
                Looper.prepare();

                Toast toast = Toast.makeText(LoginActivity.this, "登录成功",
                        Toast.LENGTH_LONG);
                //可以控制toast显示的位置
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                JSONObject para = jsonObject1.getJSONObject("para");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                CurUser curUser = CurUser.getInstance();
                curUser.nickname = para.getString("nickname");
                curUser.allDist = (float)para.getDouble("allDist");
                curUser.allTime = (float)para.getDouble("allTime");
                curUser.catExp = para.getInt("catExp");
                curUser.catFood = para.getInt("catFood");
                curUser.id = para.getString("id");
                curUser.level = para.getInt("level");
                curUser.location = para.getString("location");
                curUser.maxDist = (float) para.getDouble("maxDist");
                curUser.maxTime = (float) para.getDouble("maxTime");
                startActivity(intent);
                Looper.loop();
            } else {
                Looper.prepare();

                Toast toast = Toast.makeText(LoginActivity.this, "账号不存在或密码错误！",
                        Toast.LENGTH_LONG);
                //可以控制toast显示的位置
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Looper.loop();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final EditText IdText = (EditText) findViewById(R.id.account);
        final EditText passwordText = (EditText) findViewById(R.id.password);

        Button button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPassword = passwordText.getText().toString();
                strID = IdText.getText().toString();

                sendRequestWithHttpClient();

                //如果密码正确进入到开始界面
            }
        });


        Button button1 = (Button) findViewById(R.id.register_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

}
