package com.example.lucas.running_cat1;

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
    private CurUser() {}
    public static CurUser getInstance() {
        if(_instance == null) {
            _instance = new CurUser();
        }
        return _instance;
    }
}
