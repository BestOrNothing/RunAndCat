package com.example.lucas.running_cat1;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.Trace;
import com.example.lucas.running_cat1.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

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

public class MapActivity extends Activity {

    private Chronometer timer;

    private double distance = 0;
    private double totalTime = 0;
    private double aveSpeed = 0;

    int miss = 0;
    int gatherInterval = 1;  //位置采集周期 (s)
    int packInterval = 5;  //打包周期 (s)
    String entityName = null;  // entity标识
    long serviceId = 114202;// 鹰眼服务ID
    int traceType = 2;  //轨迹服务类型  traceType - ( 0 : 不建立长连接, 1 : 建立长连接但不采集数据, 2 : 建立长连接并采集数据 )
    private static OnStartTraceListener startTraceListener = null;  //开启轨迹服务监听器

    private static MapView mapView = null;
    private static BaiduMap baiduMap = null;
    private static OnEntityListener entityListener = null;
    private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点
    private static MapStatusUpdate msUpdate = null;
    private static BitmapDescriptor realtimeBitmap;  //图标
    private static OverlayOptions overlay;  //覆盖物
    private static List<LatLng> pointList = new ArrayList<LatLng>();  //定位点的集合
    private static PolylineOptions polyline = null;  //路线覆盖物


    private Trace trace;  // 实例化轨迹服务
    private LBSTraceClient client;  // 实例化轨迹服务客户端
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.map1);

        init();

        initOnEntityListener();

        initOnStartTraceListener();

        client.startTrace(trace, startTraceListener);  // 开启轨迹服务


        //添加图片按钮点击事件
        ImageButton startButton = (ImageButton) findViewById(R.id.startButton);
        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改为按下时的背景图片
                    v.setBackgroundResource(R.drawable.start2_button);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //改为抬起时的图片
                    v.setBackgroundResource(R.drawable.start_button);

                    distance = 0;

                    pointList.clear();

                    jumpToLayout2();
                }



                return false;
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void jumpToLayout2() {
        //SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.map);

        final TextView distanceView = (TextView)findViewById(R.id.DistanceTextView);
        final TextView speedView = (TextView)findViewById(R.id.SpeedTextView);

        //获得计时器对象
        timer = (Chronometer)this.findViewById(R.id.chronometer);
        timer.setBase(SystemClock.elapsedRealtime());  //计时器清零
        miss = 0;
        timer.start();
        //输出格式为： 时：分：秒
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer ch) {
                miss++;

                //显示速度和时间
                totalTime = miss * 1.0;
                aveSpeed = distance / totalTime;
                distanceView.setText(DoubleToString(distance));
                speedView.setText(DoubleToString(aveSpeed));

                ch.setText(FormatMiss(miss));
            }
        });

        init();

        initOnEntityListener();

        initOnStartTraceListener();

        client.startTrace(trace, startTraceListener);  // 开启轨迹服务

        ImageButton pauseButton = (ImageButton) findViewById(R.id.finishButton);
        pauseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改为按下时的背景图片
                    v.setBackgroundResource(R.drawable.finish2_button);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //改为抬起时的图片
                    v.setBackgroundResource(R.drawable.finish_button);

                    //停止计时
                    timer.stop();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            //发送json对象给服务器
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost(Url.basePath + "run.php");
                            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonObject2 = new JSONObject();
                            try {
                                jsonObject.put("dist",distance/1000.0);
                                jsonObject.put("time",totalTime/3600.0);
                                jsonObject.put("id", CurUser.getInstance().id);
                                jsonObject2.put("para", jsonObject);

                                nameValuePair.add(new BasicNameValuePair("request", jsonObject2
                                        .toString()));
                                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                                HttpResponse httpResponse = httpClient.execute(httpPost);
                                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                    //请求和响应都成功了
                                }
                                Looper.loop();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();


                    CurUser.getInstance().catFood += distance/100.0;

                    jumpToLayout1();
                }

                return false;
            }
        });
    }

    public String DoubleToString(double e){
        String S = String.format("%.2f",e);
        return S;
    }

    // 将秒转化成小时分钟秒
    public String FormatMiss(int miss){
        String hh=miss/3600>9?miss/3600+"":"0"+miss/3600;
        String  mm=(miss % 3600)/60>9?(miss % 3600)/60+"":"0"+(miss % 3600)/60;
        String ss=(miss % 3600) % 60>9?(miss % 3600) % 60+"":"0"+(miss % 3600) % 60;
        return hh+":"+mm+":"+ss;
    }

    public void jumpToLayout1() {

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.map1);

        init();

        initOnEntityListener();

        initOnStartTraceListener();

        client.startTrace(trace, startTraceListener);  // 开启轨迹服务

        ImageButton startButton = (ImageButton) findViewById(R.id.startButton);
        startButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //更改为按下时的背景图片
                    v.setBackgroundResource(R.drawable.start2_button);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //改为抬起时的图片
                    v.setBackgroundResource(R.drawable.start_button);

                    distance = 0;

                    pointList.clear();

                    jumpToLayout2();
                }

                return false;
            }
        });

    }


    /**
     * 初始化各个参数
     */
    private void init() {
        mapView = (MapView) findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        mapView.showZoomControls(false);

        //设置初始比例
        baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(19f));


        entityName = getImei(getApplicationContext());  //手机Imei值的获取，用来充当实体名

        client = new LBSTraceClient(getApplicationContext());  //实例化轨迹服务客户端

        trace = new Trace(getApplicationContext(), serviceId, entityName, traceType);  //实例化轨迹服务

        client.setInterval(gatherInterval, packInterval);  //设置位置采集和打包周期
    }


    /**
     * 初始化设置实体状态监听器
     */
    private void initOnEntityListener() {

        //实体状态监听器
        entityListener = new OnEntityListener() {

            @Override
            public void onRequestFailedCallback(String arg0) {
                Looper.prepare();
                Toast.makeText(
                        getApplicationContext(),
                        "entity请求失败的回调接口信息：" + arg0,
                        Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }

            @Override
            public void onQueryEntityListCallback(String arg0) {
                /**
                 * 查询实体集合回调函数，此时调用实时轨迹方法
                 */
                showRealtimeTrack(arg0);
            }

        };
    }


    /**
     * 追踪开始
     */
    private void initOnStartTraceListener() {

        // 实例化开启轨迹服务回调接口
        startTraceListener = new OnStartTraceListener() {
            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int arg0, String arg1) {
                Log.i("TAG", "onTraceCallback=" + arg1);
                if (arg0 == 0 || arg0 == 10006) {
                    startRefreshThread(true);
                }
            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte arg0, String arg1) {
                Log.i("TAG", "onTracePushCallback=" + arg1);
            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lucas.running_cat1/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Map Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lucas.running_cat1/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }


    /**
     * 轨迹刷新线程
     */
    private class RefreshThread extends Thread {

        protected boolean refresh = true;

        public void run() {

            while (refresh) {
                queryRealtimeTrack();
                try {
                    Thread.sleep(packInterval * 1000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠失败");
                }
            }

        }
    }

    /**
     * 查询实时线路
     */
    private void queryRealtimeTrack() {

        String entityName = this.entityName;
        String columnKey = "";
        int returnType = 0;
        int activeTime = 0;
        int pageSize = 10;
        int pageIndex = 1;

        this.client.queryEntityList(
                serviceId,
                entityName,
                columnKey,
                returnType,
                activeTime,
                pageSize,
                pageIndex,
                entityListener
        );

    }


    /**
     * 展示实时线路图
     *
     * @param realtimeTrack
     */
    protected void showRealtimeTrack(String realtimeTrack) {

        if (refreshThread == null || !refreshThread.refresh) {
            return;
        }

        //数据以JSON形式存取
        RealtimeTrackData realtimeTrackData = GsonService.parseJson(realtimeTrack, RealtimeTrackData.class);

        if (realtimeTrackData != null && realtimeTrackData.getStatus() == 0) {

            LatLng latLng = realtimeTrackData.getRealtimePoint();

            if (latLng != null) {
                pointList.add(latLng);

                int length = pointList.size();
                double dis;
                if(length>=2){
                    dis = DistanceUtil.getDistance(pointList.get(length-2), pointList.get(length - 1));
                    distance += dis;
                }

                drawRealtimePoint(latLng);
            } else {
                Toast.makeText(getApplicationContext(), "当前无轨迹点", Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * 画出实时线路点
     *
     * @param point
     */
    private void drawRealtimePoint(LatLng point) {

        baiduMap.clear();
        MapStatus mapStatus = new MapStatus.Builder().target(point).build();  //zoom是缩放级别 2-21
        msUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        realtimeBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        overlay = new MarkerOptions().position(point)
                .icon(realtimeBitmap).zIndex(9).draggable(true);

        if (pointList.size() >= 2) {
            polyline = new PolylineOptions().width(10).color(Color.RED).points(pointList);
        }

        addMarker();

    }


    private void addMarker() {

        if (msUpdate != null) {
            baiduMap.setMapStatus(msUpdate);
        }

        if (polyline != null) {
            baiduMap.addOverlay(polyline);
        }

        if (overlay != null) {
            baiduMap.addOverlay(overlay);
        }

        //垃圾回收
        polyline = null;
        overlay = null;
        msUpdate = null;
        realtimeBitmap = null;
        System.gc();

    }


    /**
     * 启动刷新线程
     *
     * @param isStart
     */
    private void startRefreshThread(boolean isStart) {

        if (refreshThread == null) {
            refreshThread = new RefreshThread();
        }

        refreshThread.refresh = isStart;

        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }


    }


    /**
     * 获取手机的Imei码，作为实体对象的标记值
     *
     * @param context
     * @return
     */

    private String getImei(Context context) {
        String mImei = "NULL";
        try {
            mImei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            System.out.println("获取IMEI码失败");
            mImei = "NULL";
        }
        return mImei;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.setMyLocationEnabled(false);

        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

}















