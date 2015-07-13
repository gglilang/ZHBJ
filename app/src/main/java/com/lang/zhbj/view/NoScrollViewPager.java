package com.lang.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Lang on 2015/7/13.
 */
public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    // 表示事件是否拦截
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return false;
    }

    /**
     * 重写onTouchEvent事件，什么都不做
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
