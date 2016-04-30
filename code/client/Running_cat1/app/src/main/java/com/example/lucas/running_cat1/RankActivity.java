package com.example.lucas.running_cat1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created by yanzhensong on 4/26/16.
 */
public class RankActivity extends Activity {

    private ListView lv;
    private RankAdapter adapter;

    protected void onCreate(Bundle saveInstanceState) {


        super.onCreate(saveInstanceState);
        setContentView(R.layout.rank);

        lv = (ListView)findViewById(R.id.rank_lv);
        RankList.getRankmemList();
        adapter = new RankAdapter(RankList.list,RankActivity.this);
        lv.setAdapter(adapter);
    }
}
