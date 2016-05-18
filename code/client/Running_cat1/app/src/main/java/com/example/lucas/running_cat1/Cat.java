package com.example.lucas.running_cat1;

/**
 * Created by yanzhensong on 4/29/16.
 */
public class Cat {
    private static Cat _instance = null;
    public int[] levelExp = {0, 10, 20, 40, 80, 160, 320, 640, 1280, 2560, 5120, 10240};
    private Cat() {}
    public static Cat getInstance() {
        if (_instance == null) {
            _instance = new Cat();
        }
        return _instance;
    }
    public void levelUp() {
        CurUser user = CurUser.getInstance();
        while(user.catExp >= levelExp[user.level]) {
            user.catExp -= levelExp[user.level];
            user.level++;
        }
    }
}
