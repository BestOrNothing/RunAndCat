package com.example.lucas.running_cat1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by lucas on 2016/3/19.
 */
public class SecondActivity extends Activity {

    private View friendListItem;
    private View friendMomentItem;
    private View rankItem;
    private View scanItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two);
        friendListItem = findViewById(R.id.friend_list_item);
        friendListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, FriendListActivity.class);
                startActivity(intent);
            }
        });

        friendMomentItem = findViewById(R.id.friend_moment_item);
        friendMomentItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, NewFriendActivity.class);
                startActivity(intent);
            }
        });

        rankItem = findViewById(R.id.rank_item);
        rankItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });

        scanItem = findViewById(R.id.scan_item);
        scanItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
        scanItem.setVisibility(View.GONE);
    }

}
