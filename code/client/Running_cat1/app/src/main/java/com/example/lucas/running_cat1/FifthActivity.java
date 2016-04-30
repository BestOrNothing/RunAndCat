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
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 2016/3/19.
 */
public class FifthActivity extends Activity {
    private String nickname; //昵称
    private float allDist;   //总距离
    private float allTime;    //总时间
    private int catLevel;
    private float longestDist;            //最长距离
    private float longestTime;     //最长时间
    private String personflag;  //标识符
    private String StrID;
    private TextView allDisttextview;
    private TextView allTimetextview;
    private TextView longestDisttextview;
    private TextView longestTimetextview;
    private TextView nicknameView;
    private TextView catLevelView;

    private int mutex = 1;

    //发送和接收json数据
    private void sendRequestWithHttpClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //发送json对象给服务器
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Url.basePath +"person.php");
                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject2 = new JSONObject();
                    personflag="003";
                    StrID = CurUser.getInstance().id;
                    if(personflag.equals("003")) {
                        jsonObject.put("Rid", personflag);
                        jsonObject.put("id", StrID);
                        jsonObject2.put("para", jsonObject);
                    }

                    nameValuePair.add(new BasicNameValuePair("request", jsonObject2
                            .toString()));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    int a = httpResponse.getStatusLine().getStatusCode();
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
                mutex++;
            }
        }).start();
    }


    private void parseJSONWITHJSONObject(String jsonDate){
        try{
            JSONObject jsonObject1 = new JSONObject(jsonDate);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("head");
            String str = jsonObject2.getString("code");
            if(str.equals("007")){
                nickname = jsonObject2.getString("nickname");
                allDist =(float) jsonObject2.getDouble("allDist");
                allTime =(float) jsonObject2.getDouble("allTime");
                longestDist =(float) jsonObject2.getDouble("MaxDist");
                longestTime =(float) jsonObject2.getDouble("MaxTime");
                catLevel = jsonObject2.getInt("level");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle saveInstanceState){

        super.onCreate(saveInstanceState);
        setContentView(R.layout.five);

        mutex--;

        sendRequestWithHttpClient();

        while(mutex<=0) ;

        allDisttextview = (TextView) findViewById(R.id.UserDistance);
        allTimetextview = (TextView) findViewById(R.id.UserTotalTime);
        longestDisttextview = (TextView) findViewById(R.id.longestDistanceTextView);
        longestTimetextview = (TextView) findViewById(R.id.longestTimeTextView);
        nicknameView = (TextView)findViewById(R.id.Nickname);
        catLevelView = (TextView)findViewById(R.id.catLevel);

        allDisttextview.setText(DoubleToString(allDist)+"公里");
        allTimetextview.setText(DoubleToString(allTime)+"小时");
        longestTimetextview.setText(DoubleToString(longestTime)+"小时");
        longestDisttextview.setText(DoubleToString(longestDist)+"公里");
        catLevelView.setText(String.valueOf(catLevel)+"级");
        nicknameView.setText(nickname);


        //头像点击更改昵称
        ImageButton imageButton = (ImageButton) findViewById(R.id.picture);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FifthActivity.this, PersonalInformationAdapt.class);
                startActivity(intent);
            }
        });

        //退出登录
        Button button = (Button) findViewById(R.id.quit);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(FifthActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public String DoubleToString(double e){
        String s = String.format("%.2f",e);
        return s;
    }

}
