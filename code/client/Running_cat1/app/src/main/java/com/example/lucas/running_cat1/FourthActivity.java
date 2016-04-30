package com.example.lucas.running_cat1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 2016/3/19.
 */
public class FourthActivity extends Activity {

    private List<Info> infoList = new ArrayList<Info>();

    protected void onCreate(Bundle saveInstaceState) {
        super.onCreate(saveInstaceState);
        setContentView(R.layout.four);
        initInfo();
        InfoAdapter adapter = new InfoAdapter(FourthActivity.this, R.layout.info_item, infoList);
        ListView listView = (ListView) findViewById(R.id.info_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Info info = infoList.get(position);
                Intent intent = new Intent(FourthActivity.this, InfoWebActivity.class);
                String basePath = "http://159.203.225.151/RunningCat/";
                intent.putExtra("url", basePath + "document/" + (position + 1) + ".html");
                startActivity(intent);
            }
        });
    }

    private void initInfo() {
        Info _1 = new Info("运动常识：跑步知识大全", R.drawable.info_image_1);
        infoList.add(_1);
        Info _2 = new Info("你真的会跑步吗？五个小知识教你立刻学会跑步", R.drawable.info_image_2);
        infoList.add(_2);
        Info _3 = new Info("跑步的种类与区分", R.drawable.info_image_3);
        infoList.add(_3);
        Info _4 = new Info("跑步的训练方法", R.drawable.info_image_4);
        infoList.add(_4);
        Info _5 = new Info("跑步成为中国中产阶级最新的信仰", R.drawable.info_image_5);
        infoList.add(_5);
        Info _6 = new Info("跑步热折射中国民众健康观念变迁", R.drawable.info_image_6);
        infoList.add(_6);
    }
}
