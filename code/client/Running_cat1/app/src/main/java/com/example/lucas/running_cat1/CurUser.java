package com.example.lucas.running_cat1;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzhensong on 4/28/16.
 * 单例模式，表示当前用户
 */
public class CurUser {
    private static CurUser _instance = null;
    public String id;
    public String nickname;
    public String location;
    public int catExp;
    public int catFood;
    public float allDist;
    public float allTime;
    public float maxDist;
    public float maxTime;
    public int level;
    private int mutex = 1;
    private CurUser() {}
    public static CurUser getInstance() {
        if(_instance == null) {
            _instance = new CurUser();
        }
        return _instance;
    }

    public void sync() {
        mutex--;
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject para = new JSONObject();
                try {
                    para.put("Rid", "003");
                    para.put("id", id);
                    JSONObject request = new JSONObject();
                    request.put("para", para);
                    params.add(new BasicNameValuePair("request", request.toString()));
                    HttpResponse response = MyHttp.sendPost(Url.basePath + "person.php", params);
                    if (response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        String strResponse = EntityUtils.toString(entity, "utf-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        JSONObject head= jsonObject.getJSONObject("head");
                        //level = head.getInt("level");
                        //nickname = head.getString("nickname");
                        catFood = head.getInt("catFood");
                        //catExp = head.getInt("catExp");
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mutex++;
            }
        }).start();
        while (mutex <= 0) ;
    }

}
