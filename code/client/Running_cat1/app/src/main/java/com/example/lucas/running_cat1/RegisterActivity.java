package com.example.lucas.running_cat1;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
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
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by lucas on 2016/3/19.
 */
public class RegisterActivity extends Activity {

    private int flag;  //判断用户是否已经存在
    private String strPassword1;   //密码字符串
    private String strPassword2;    //密码确认字符串
    private String strID;            //账号字符串

    //发送和接收json数据
    private void sendRequestWithHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //发送json对象给服务器
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Url.basePath + "register.php");
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject.put("id",strID );
                    jsonObject.put("password",strPassword1 );
                    jsonObject2.put("para", jsonObject);
                    nameValuePair.add(new BasicNameValuePair("request", jsonObject2
                            .toString()));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                    httpClient.execute(httpPost);


                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        //请求和响应都成功了
                        //从服务器接收json对象
                        HttpEntity entity = httpResponse.getEntity();
                        String strResponse = EntityUtils.toString(entity,"utf-8");
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
            JSONObject jsonCode = jsonObject2.getJSONObject("code");
            String str = jsonCode.toString();
            if(str.equals("000"))
                flag = 1;
            else flag = 0;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final EditText IdText = (EditText) findViewById(R.id.account1);
        final EditText password1Text = (EditText) findViewById(R.id.password1);
        final EditText password2Text = (EditText) findViewById(R.id.password2);

        Button button = (Button) findViewById(R.id.register_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;

                strPassword1 = password1Text.getText().toString();
                strPassword2 = password2Text.getText().toString();
                strID = IdText.getText().toString();

                if(!strPassword1.equals(strPassword2)) flag = -1;

                sendRequestWithHttpClient();

                if(strID.length() > 20 || strID.length() < 6){
                    Toast toast = Toast.makeText(RegisterActivity.this, "用户名不合法",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if(strPassword1.length() > 20 || strPassword1.length() < 6) {
                    Toast toast = Toast.makeText(RegisterActivity.this, "密码不合法",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if(flag == 1){
                    Toast toast = Toast.makeText(RegisterActivity.this, "您已注册成功，请登录",
                            Toast.LENGTH_LONG);
                    //可以控制toast显示的位置
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else if(flag == 0){
                    Toast toast = Toast.makeText(RegisterActivity.this, "用户已存在，请重新注册",
                            Toast.LENGTH_LONG);
                    //可以控制toast显示的位置
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(RegisterActivity.this, "两次输入的密码不一致",
                            Toast.LENGTH_LONG);
                    //可以控制toast显示的位置
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }

}
