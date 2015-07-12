package com.lang.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.lang.zhbj.utils.PrefUtil;


public class SplashActivity extends Activity {

    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        startAnim();
    }

    private void startAnim() {

        // 动画集合
        AnimationSet set = new AnimationSet(false);

        // 旋转动画
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setFillAfter(true); // 保存动画状态
        rotate.setDuration(1000);   // 动画时间

        // 缩放动画
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setFillAfter(true); // 保存动画状态
        scale.setDuration(1000);   // 动画时间

        // 渐变动画
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setFillAfter(true); // 保存动画状态
        alpha.setDuration(1000);   // 动画时间

        set.addAnimation(rotate);
        set.addAnimation(scale);
        set.addAnimation(alpha);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rl_root.setAnimation(set);
    }

    /**
     * 跳转到写一个界面
     */
    private void jumpNextPage(){
        if(PrefUtil.getBoolean(this, "is_user_guide_showed", false)){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        }
        finish();
    }
}

