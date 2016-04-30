package com.example.lucas.running_cat1;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;

/**
 * Created by lucas on 2016/3/19.
 */
public class InitActivity extends Activity{

    private ImageView mImageView;
    AlphaAnimation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.init);
        mImageView = (ImageView) findViewById(R.id.startScene);
        animation = new AlphaAnimation(1,1.0f);
        animation.setDuration(3 * 1000);
        animation.setFillAfter(true);
        mImageView.startAnimation(animation);


        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            //动画结束监听
            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(InitActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}
