package com.lang.zhbj.base.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lang.zhbj.base.BaseMenuDetailPager;

/**
 * 菜单详细页-专题
 * Created by Lang on 2015/7/13.
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {
    public TopicMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initViews() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详细页-专题");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
