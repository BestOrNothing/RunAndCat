package com.example.lucas.running_cat1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanzhensong on 4/26/16.
 */
public class Friend implements Serializable {

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private String nickname;

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String location;
    private int level;
    private String id;

    public Friend(String nickname, String location, int level, String id) {
        this.nickname = nickname;
        this.location = location;
        this.level = level;
        this.id = id;
    }

    public static List<Friend> getFriendList(int count, JSONArray array) {
        List<Friend> list = new ArrayList<Friend>();
        try {
            final int CNT = 4;
            for (int i = 0; i<count*CNT; i += CNT)  {
                list.add(new Friend(array.getString(i), array.getString(i+1), array.getInt(i+2), array.getString(i+3)));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Friend() {}

    public String getNickname() {
        return nickname;
    }

    public String getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

}
