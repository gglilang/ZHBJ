package com.lang.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.lang.zhbj.base.BasePager;

/**
 * 首页实现
 * Created by Lang on 2015/7/12.
 */
public class HomePager extends BasePager {
    public HomePager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        setSlidingMenuEnable(false);
        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        fl_content.addView(textView);   // 添加view
    }
}
