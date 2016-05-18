package com.example.lucas.running_cat1;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wangchi on 4/27/16.
 */
public class RankList {

    public static List<Rankmem> list = new ArrayList<Rankmem>();

    private static int mutex=1;


    public static void getRankmemList() {


        mutex--;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    JSONObject para = new JSONObject();
                    para.put("id", CurUser.getInstance().id);
                    //para.put("id", "000");
                    JSONObject request = new JSONObject();
                    request.put("para", para);
                    params.add(new BasicNameValuePair("request", request.toString()));
                    HttpResponse response = MyHttp.sendPost(Url.basePath + "friend.php", params);
                    if(response.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = response.getEntity();
                        String strResponse = EntityUtils.toString(entity, "utf-8");
                        JSONObject jsonObject = new JSONObject(strResponse);
                        JSONObject head = jsonObject.getJSONObject("head");
                        list = Rankmem.getRankmemList(head.getInt("count"), head.getJSONArray("friendlist"));
                        list.add(new Rankmem(CurUser.getInstance().id, CurUser.getInstance().nickname, CurUser.getInstance().level));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                mutex++;
            }
        }).start();


//        //Grt FriendList From Database : ID,nickname,level
//        list.add(new Rankmem("122", "killer", 132));
//        list.add(new Rankmem("111", "killer", 313));
//        list.add(new Rankmem("311", "killer", 144));
//        list.add(new Rankmem("666", "killer", 993));
//        list.add(new Rankmem("111", "killer", 133));
//        list.add(new Rankmem("434", "killer", 783));
//        list.add(new Rankmem("256", "killer", 111));
//        list.add(new Rankmem("445", "killer", 333));
//        list.add(new Rankmem("109", "killer", 155));
//        list.add(new Rankmem("511", "killer", 133));
//
//        //Sort To Down-List
        while(mutex<=0);

        sort();

        return;
    }

    public static void sort() {
        Collections.sort(list, new Comparator<Rankmem>() {
            @Override
            public int compare(Rankmem lhs, Rankmem rhs) {
                if(lhs.getLevel()<rhs.getLevel())
                    return 1;
                else
                    return -1;
            }
        });
    }
}
