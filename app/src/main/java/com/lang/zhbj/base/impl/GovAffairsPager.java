package com.lang.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.lang.zhbj.base.BasePager;

/**
 * 政务实现
 * Created by Lang on 2015/7/12.
 */
public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public void initData() {
        super.initData();
        setSlidingMenuEnable(true);
        tv_title.setText("人口管理");
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);

        fl_content.addView(textView);   // 添加view
    }
}
