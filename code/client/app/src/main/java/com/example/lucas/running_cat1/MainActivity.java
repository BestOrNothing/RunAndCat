package com.example.lucas.running_cat1;

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

import java.util.List;

public class MainActivity extends TabActivity {

    public TabHost mth;
    public static final String TAB_HOME = "1";
    public static final String TAB_MSG = "2";
    public static final String TAB_MAP = "3";
    public static final String TAB_FRI = "4";
    public static final String TAB_PER = "5";


    public RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
        setContentView(R.layout.main);

        mth = this.getTabHost();

        TabHost.TabSpec ts1 = mth.newTabSpec(TAB_HOME).setIndicator(TAB_HOME);
        ts1.setContent(new Intent(MainActivity.this, FirstActivity.class));
        mth.addTab(ts1);

        TabHost.TabSpec ts2 = mth.newTabSpec(TAB_MSG).setIndicator(TAB_MSG);
        ts2.setContent(new Intent(MainActivity.this, SecondActivity.class));
        mth.addTab(ts2);

        TabHost.TabSpec ts3 = mth.newTabSpec(TAB_MAP).setIndicator(TAB_MAP);
        ts3.setContent(new Intent(MainActivity.this,MapActivity.class));
        mth.addTab(ts3);

        TabHost.TabSpec ts4 = mth.newTabSpec(TAB_FRI).setIndicator(TAB_FRI);
        ts4.setContent(new Intent(MainActivity.this,FourthActivity.class));
        mth.addTab(ts4);

        TabHost.TabSpec ts5 = mth.newTabSpec(TAB_PER).setIndicator(TAB_PER);
        ts5.setContent(new Intent(MainActivity.this,FifthActivity.class));
        mth.addTab(ts5);

        this.radioGroup = (RadioGroup) findViewById(R.id.main_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                switch (checkedId) {
                    case R.id.radio_button0:
                        mth.setCurrentTabByTag(TAB_HOME);
                        break;
                    case R.id.radio_button1:
                        mth.setCurrentTabByTag(TAB_MSG);
                        break;
                    case R.id.radio_button2:
                        mth.setCurrentTabByTag(TAB_MAP);
                        break;
                    case R.id.radio_button3:
                        mth.setCurrentTabByTag(TAB_FRI);
                        break;
                    case R.id.radio_button4:
                        mth.setCurrentTabByTag(TAB_PER);
                        break;
                }
            }
        });
    }

}
