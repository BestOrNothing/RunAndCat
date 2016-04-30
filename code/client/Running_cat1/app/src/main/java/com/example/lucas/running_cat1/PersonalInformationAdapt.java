package com.example.lucas.running_cat1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

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
 * Created by lucas on 2016/4/30.
 */

public class PersonalInformationAdapt extends Activity{

    private EditText nicknameEditView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.information_adapte);

        nicknameEditView = (EditText) findViewById(R.id.nickname);

        Button button = (Button) findViewById(R.id.confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //修改昵称
                        Looper.prepare();
                        String s = nicknameEditView.getText().toString();
                        //发送json对象给服务器
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(Url.basePath + "person.php");
                        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject2 = new JSONObject();
                        try {
                            jsonObject.put("nickname", s);
                            jsonObject.put("Rid", "001");
                            jsonObject.put("id", CurUser.getInstance().id);
                            jsonObject2.put("para", jsonObject);

                            nameValuePair.add(new BasicNameValuePair("request", jsonObject2
                                    .toString()));
                            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));

                            HttpResponse httpResponse = httpClient.execute(httpPost);
                            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                //请求和响应都成功了
                                //从服务器接收json对象
                                HttpEntity entity = httpResponse.getEntity();
                                String strResponse = EntityUtils.toString(entity, "utf-8");
                                Log.isLoggable("sd",1);
                            }
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Intent intent = new Intent(PersonalInformationAdapt.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
