package com.lang.zhbj.base;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lang.zhbj.domain.NewsData;

/**
 * 页签详细页
 * Created by Lang on 2015/7/13.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    
    NewsData.NewsTabData mTabData;
    private TextView tv_text;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData newsTabData) {
        super(mActivity);
        mTabData = newsTabData;
    }

    @Override
    public View initViews() {
        tv_text = new TextView(mActivity);
        tv_text.setText("页签详细页");
        tv_text.setTextSize(25);
        tv_text.setGravity(Gravity.CENTER);
        return tv_text;
    }

    @Override
    public void initData() {
        super.initData();
        tv_text.setText(mTabData.title);
    }
}
