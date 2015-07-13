package com.lang.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.lang.zhbj.base.BasePager;

/**
 * 设置实现
 * Created by Lang on 2015/7/12.
 */
public class SettingPager extends BasePager {
    public SettingPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        setSlidingMenuEnable(false);
        tv_title.setText("设置");
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        fl_content.addView(textView);   // 添加view
    }
}
