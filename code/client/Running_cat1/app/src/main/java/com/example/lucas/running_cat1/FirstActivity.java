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
import android.widget.Toast;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * Created by lucas on 2016/3/19.
 */
public class FirstActivity extends Activity{

  @Override
   protected void onCreate(Bundle saveInstanceState) {
      super.onCreate(saveInstanceState);
      setContentView(R.layout.one);

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

              }
              return false;
          }
      });

  }


}
