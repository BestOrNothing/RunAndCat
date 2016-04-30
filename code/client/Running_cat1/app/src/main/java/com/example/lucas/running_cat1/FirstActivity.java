package com.example.lucas.running_cat1;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
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
import android.widget.TextView;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * Created by lucas on 2016/3/19.
 */
public class FirstActivity extends Activity{

    private int mutex = 1;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.one);

        CurUser.getInstance().sync();

        final TextView editText = (TextView) findViewById(R.id.cat_food);
        editText.setText(" " + CurUser.getInstance().catFood);
        final TextView catLevel = (TextView) findViewById(R.id.cat_level);
        catLevel.setText(" "+CurUser.getInstance().level);
        final TextView catExp = (TextView) findViewById(R.id.cat_exp);
        catExp.setText(CurUser.getInstance().catExp + " / " + Cat.getInstance().levelExp[CurUser.getInstance().level]);


        //添加图片按钮点击事件
        ImageButton feedButton = (ImageButton) findViewById(R.id.feedButton);
        feedButton.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
              //更改为按下时的背景图片
              v.setBackgroundResource(R.drawable.feed1);

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
              //改为抬起时的图片
                v.setBackgroundResource(R.drawable.feed);
                final CurUser user = CurUser.getInstance();
                user.catExp += user.catFood;
                user.catFood = 0;
                Cat.getInstance().levelUp();
                mutex--;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            JSONObject requset = new JSONObject();
                            requset.put("catExp", user.catExp);
                            requset.put("catFood", user.catFood);
                            requset.put("id", user.id);
                            params.add(new BasicNameValuePair("request", requset.toString()));
                            HttpResponse httpResponse = MyHttp.sendPost(Url.basePath + "food.php", params);
                            if(httpResponse.getStatusLine().getStatusCode() == 200) {
                                HttpEntity entity = httpResponse.getEntity();
                                String s = EntityUtils.toString(entity, "utf-8");
                                System.out.print(s);
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        mutex++;
                    }
                }).start();
                while (mutex <= 0) ;
                catExp.setText(String.valueOf(user.catExp));
                editText.setText(String.valueOf(user.catFood));
                catLevel.setText(String.valueOf(user.level));
            }
            return false;
          }
        });

    }
}
