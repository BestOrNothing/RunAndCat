package com.example.lucas.running_cat1;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchi on 4/27/16.
 */
public class Rankmem {
    private String id;
    private String nickname;
    private int level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static List<Rankmem> getRankmemList(int count, JSONArray array) {
        List<Rankmem> list = new ArrayList<Rankmem>();
        try {
            final int CNT = 4;
            for (int i = 0; i<count*CNT; i += CNT)  {
                list.add(new Rankmem(array.getString(i+3), array.getString(i), array.getInt(i+2)));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Rankmem(String id,String nickname,int level){
        this.id = id;
        this.nickname = nickname;
        this.level = level;
    }
}
